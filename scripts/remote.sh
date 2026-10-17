#!/usr/bin/env bash
#
# Builds on the Mac mini and runs the result here, so this laptop spends no CPU
# or memory on Gradle, Kotlin/Native or xcodebuild. The working tree, with
# uncommitted changes, is mirrored one way with rsync (secrets and build output
# stay behind); the mini keeps its Gradle daemons, caches and DerivedData warm,
# and only the finished app comes back.
#
# The same file in every Nybs app repository: what differs between apps (module,
# scheme, bundle id, extra commands) is in scripts/remote.project.sh. Change this
# file in one repository, then copy it to the others unchanged.
#
#   scripts/remote.sh ios               # build there, run on the simulator booted here
#   scripts/remote.sh app               # the same for any Apple app: iOS, watchOS, or a macOS
#                                       # app opened here
#   scripts/remote.sh android           # build there, run on the emulator there, scrcpy window here
#   scripts/remote.sh android --local   # build there, run on an emulator or USB phone here
#   scripts/remote.sh build [ios|android]  # build there only: a compile check, nothing installed
#   scripts/remote.sh gradle <args>     # any Gradle run there, e.g. :app:testDebugUnitTest
#   scripts/remote.sh xcodebuild <args> # any xcodebuild run there, in the iOS project's folder
#   scripts/remote.sh screen            # (re)open the scrcpy window of the remote emulator
#   scripts/remote.sh emu start|stop|status
#   scripts/remote.sh ui                # tappable views on the remote emulator, with centres
#   scripts/remote.sh record <name> 'input tap 540 2230' 'sleep 1' ...
#                                       # screen video + jank numbers + frame sheets per animation
#   scripts/remote.sh frames <video.mp4> [name]
#                                       # the same per-animation timing and frame sheets for any
#                                       # local recording, e.g. `xcrun simctl io booted recordVideo`
#   scripts/remote.sh adb <args>        # adb against the remote emulator (text output only)
#   scripts/remote.sh pull <path>       # copy <path> from the mirror to build/remote/pulled/<path>
#   scripts/remote.sh sync | shell | doctor
#   scripts/remote.sh slot <label> <command>...
#                                       # <command> here, holding a build slot of this Mac's queue
#                                       # (CI on the mini uses it)
#   scripts/remote.sh <command of this app>  # listed in scripts/remote.project.sh
#
# Builds of every app (ios, android, gradle, xcodebuild and the apps' own build
# commands) wait in one queue on the mini, two at a time: see LEASE_SH.
# REMOTE_QUEUE=0 skips it.
#
# Env: REMOTE_HOST (mac-mini, the Tailscale name), REMOTE_DIR (the mirror under
# the remote home: RemoteBuilds/<repo>, or RemoteBuilds/<repo>--<worktree> for a
# git worktree), FLAVOR (the app's default flavor otherwise), REMOTE_AVD
# (Phone_API_35), IOS_SIM_NAME (what to boot here when no simulator is running),
# IOS_SIM_UDID (the simulator to run on when several are booted; the first
# booted one otherwise), IOS_CONFIGURATION (Debug | Release).
# Setup and troubleshooting: .agents/remote-build.md
set -euo pipefail

REPO_DIR="$(cd "$(dirname "$0")/.." && pwd)"
REMOTE_HOST="${REMOTE_HOST:-mac-mini}"
REMOTE_AVD="${REMOTE_AVD:-Phone_API_35}"
MARKER=".remote-build-mirror"
DERIVED_REL="build/remote/DerivedData"
IOS_CONFIGURATION="${IOS_CONFIGURATION:-Debug}"

# What the app's scripts/remote.project.sh may set; empty means the app has no such part.
ANDROID_MODULE=""        # Gradle module of the Android app, e.g. androidApp
FLAVOR_DEFAULT=""        # its default product flavor, empty when it has none
ANDROID_ACTIVITY=""      # launcher, e.g. com.example/.MainActivity
IOS_DIR="."              # folder of the Xcode project or workspace
IOS_WORKSPACE=""         # .xcworkspace in IOS_DIR (CocoaPods); otherwise
IOS_PROJECT=""           # .xcodeproj in IOS_DIR
IOS_SCHEME=""            # scheme to build; empty: the app has no iOS part
IOS_PRODUCT=""           # name of the built .app
IOS_APP_ID=""            # bundle id, to launch it
IOS_PLATFORM="iOS"       # iOS | watchOS | macOS
IOS_SIGNING="adhoc"      # adhoc ("Sign to Run Locally": keychain access works) | none
IOS_SIM_DEFAULT=""       # simulator to boot when none is (default: iPhone 16 Pro / an Apple Watch)
SYNC_ENV=0               # 1: .env goes to the mirror (when the build reads it)
SYNC_EXCLUDES_EXTRA=()   # more rsync --exclude options
PROJECT_COMMANDS=""      # the app's own commands: <name>_cmd here, agent_<name> on the mini
[ -f "$REPO_DIR/scripts/remote.project.sh" ] || { echo "remote: no scripts/remote.project.sh" >&2; exit 1; }
# shellcheck source=/dev/null
. "$REPO_DIR/scripts/remote.project.sh"

FLAVOR="${FLAVOR:-$FLAVOR_DEFAULT}"
case "$IOS_PLATFORM" in
  watchOS) IOS_SDK=watchsimulator IOS_DEST="generic/platform=watchOS Simulator" ;;
  macOS) IOS_SDK=macosx IOS_DEST="generic/platform=macOS" ;;
  *) IOS_SDK=iphonesimulator IOS_DEST="generic/platform=iOS Simulator" ;;
esac
IOS_SIM_NAME="${IOS_SIM_NAME:-${IOS_SIM_DEFAULT:-iPhone 16 Pro}}"
if [ "$IOS_SDK" = macosx ]; then
  APP_REL="$DERIVED_REL/Build/Products/$IOS_CONFIGURATION/$IOS_PRODUCT.app"
else
  APP_REL="$DERIVED_REL/Build/Products/$IOS_CONFIGURATION-$IOS_SDK/$IOS_PRODUCT.app"
fi

say() { printf '== %s\n' "$*" >&2; }
die() { printf 'remote: %s\n' "$*" >&2; exit 1; }
cap() { printf '%s%s' "$(printf %s "${1:0:1}" | tr '[:lower:]' '[:upper:]')" "${1:1}"; }

# The debug build of the Android app and where its APK lands.
assemble_task() { echo ":$ANDROID_MODULE:assemble$(cap "$FLAVOR")Debug"; }
apk_name() { echo "$ANDROID_MODULE${FLAVOR:+-$FLAVOR}-debug.apk"; }
apk_rel() { echo "$ANDROID_MODULE/build/outputs/apk/${FLAVOR:+$FLAVOR/}debug/$(apk_name)"; }
need_android() { [ -n "$ANDROID_MODULE" ] || die "this app has no Android part (scripts/remote.project.sh)"; }
need_ios() { [ -n "$IOS_SCHEME" ] || die "this app has no iOS part (scripts/remote.project.sh)"; }
need_gradle() { [ -x "$REPO_DIR/gradlew" ] || die "this app has no Gradle build"; }

# ================================================================ remote half
# `__agent ...` runs on the remote Mac inside the mirror, started over ssh by the
# local half below. Build output goes to stdout (the local half rewrites paths
# in it), progress notes to stderr.

EMU_LOG="$HOME/Library/Logs/remote-emulator.log"

agent_env() {
  # A non-interactive ssh session has no login PATH and no JAVA_HOME.
  export ANDROID_HOME="${ANDROID_HOME:-$HOME/Library/Android/sdk}"
  PATH="$ANDROID_HOME/platform-tools:$ANDROID_HOME/emulator:$ANDROID_HOME/cmdline-tools/latest/bin:$HOME/.maestro/bin:/opt/homebrew/bin:/usr/local/bin:$PATH"
  export PATH LANG=en_US.UTF-8 LC_ALL=en_US.UTF-8
  if [ -z "${JAVA_HOME:-}" ]; then
    JAVA_HOME="$(/usr/libexec/java_home -v 17 2>/dev/null || /usr/libexec/java_home 2>/dev/null || true)"
    export JAVA_HOME
  fi
  # A Gradle home of our own, next to the mirror: ~/.gradle on the remote may
  # belong to other work (a corporate proxy, repository credentials, bigger
  # daemons), and none of that should leak into this project or fail it.
  export GRADLE_USER_HOME="${REMOTE_GRADLE_HOME:-$(dirname "$REPO_DIR")/gradle-home}"
  # Never synced: sdk.dir differs between machines.
  [ ! -x gradlew ] || [ -f local.properties ] || echo "sdk.dir=$ANDROID_HOME" >local.properties
}

# The remote may run emulators for other work; only $REMOTE_AVD is ours.
emu_serial() {
  local s
  for s in $(adb devices 2>/dev/null | awk '$1 ~ /^emulator-/ && $2 == "device" { print $1 }'); do
    if [ "$(adb -s "$s" emu avd name 2>/dev/null | awk 'NR == 1' | tr -d '\r')" = "$REMOTE_AVD" ]; then
      echo "$s"
      return 0
    fi
  done
}
emu_process() { pgrep -f "[-]avd $REMOTE_AVD( |\$)" >/dev/null; }

# Starts the AVD headless unless one is already up or booting. The picture
# reaches the laptop through scrcpy (`screen`), so there is no window here.
# The GPU is the host's on purpose: without a window, `-gpu auto` falls back
# to software rendering (lavapipe/swangle), and Maestro's screenshots then
# stall the emulator long enough to drop its adb connection mid-flow.
agent_emu_boot() {
  [ -n "$(emu_serial)" ] && return 0
  emu_process && return 0
  emulator -list-avds 2>/dev/null | grep -qx "$REMOTE_AVD" \
    || die "no AVD '$REMOTE_AVD' on $(hostname -s); see .agents/remote-build.md"
  say "booting $REMOTE_AVD headless on $(hostname -s)"
  nohup emulator -avd "$REMOTE_AVD" -no-window -no-audio -no-boot-anim \
    -gpu "${REMOTE_EMU_GPU:-host}" </dev/null >"$EMU_LOG" 2>&1 &
}

# Prints the serial once the system has booted; a dead or stuck emulator fails
# the run instead of hanging it.
agent_emu_wait() {
  local waited=0 serial
  while :; do
    serial="$(emu_serial)"
    if [ -n "$serial" ] && [ "$(adb -s "$serial" shell getprop sys.boot_completed 2>/dev/null | tr -d '\r')" = 1 ]; then
      echo "$serial"
      return 0
    fi
    emu_process || { tail -20 "$EMU_LOG" >&2; die "the emulator exited"; }
    [ "$waited" -lt 300 ] || die "the emulator did not boot in 300 s (log: $EMU_LOG)"
    sleep 2
    waited=$((waited + 2))
  done
}

agent_emu() {
  local serial
  case "${1:-status}" in
    start) agent_emu_boot; agent_emu_wait ;;
    stop)
      serial="$(emu_serial)"
      if [ -n "$serial" ]; then adb -s "$serial" emu kill >/dev/null; say "stopped $serial"; else say "no emulator running"; fi ;;
    status)
      serial="$(emu_serial)"
      if [ -z "$serial" ]; then
        if emu_process; then echo "booting"; else echo "not running"; fi
        return 0
      fi
      echo "$serial $(adb -s "$serial" emu avd name 2>/dev/null | awk 'NR == 1' | tr -d '\r'), /data $(adb -s "$serial" shell df -h /data 2>/dev/null | awk 'NR == 2 { print $5 " used" }')" ;;
    *) die "emu start|stop|status" ;;
  esac
}

agent_android() {
  agent_emu_boot  # boots while Gradle builds
  # AGP 8.11 warns on every run that the mini's newer SDK tools write XML v4.
  ./gradlew --console=plain --quiet "$(assemble_task)" 2>&1 \
    | { grep -v 'only understands SDK XML versions' || true; }
  local serial out
  serial="$(agent_emu_wait)"
  out="$(adb -s "$serial" install -r -t "$(apk_rel)" 2>&1)" || { echo "$out"; die "install failed on $serial"; }
  adb -s "$serial" shell am start -n "$ANDROID_ACTIVITY" >/dev/null
  say "running on $serial"
}

agent_ios() {
  local log="build/remote/ios-build.log" derived="$PWD/$DERIVED_REL" target=() sign=() sdk=()
  mkdir -p build/remote
  if [ -n "$IOS_WORKSPACE" ]; then target=(-workspace "$IOS_WORKSPACE")
  elif [ -n "$IOS_PROJECT" ]; then target=(-project "$IOS_PROJECT"); fi
  # Signed ad hoc by default ("Sign to Run Locally", no team or account): an
  # unsigned app carries no entitlements, so the keychain refuses it (-34018)
  # and whatever keeps a session there (Firebase Auth, say) fails.
  # The destination alone picks the SDK for watchOS: with -sdk watchsimulator
  # Xcode also builds a watch-only app's iOS stub for the watch, and fails.
  [ "$IOS_PLATFORM" = watchOS ] || sdk=(-sdk "$IOS_SDK")
  case "$IOS_SIGNING" in
    adhoc) sign=(CODE_SIGN_STYLE=Manual CODE_SIGN_IDENTITY=- DEVELOPMENT_TEAM= PROVISIONING_PROFILE_SPECIFIER=) ;;
    none) sign=(CODE_SIGNING_ALLOWED=NO) ;;
    *) die "IOS_SIGNING is adhoc or none" ;;
  esac
  # arm64 only: Apple silicon simulators need nothing else, and it halves a
  # Kotlin/Native link. No dSYM: nobody symbolicates these builds, and dsymutil
  # is slow and noisy about prebuilt frameworks. CocoaPods' Pods/ come synced
  # from the laptop as they are; a KMP framework's script phase runs ./gradlew
  # in the mirror with the Gradle home from agent_env.
  if ! ( cd "$IOS_DIR" && xcodebuild ${target[@]+"${target[@]}"} -scheme "$IOS_SCHEME" \
      -configuration "$IOS_CONFIGURATION" ${sdk[@]+"${sdk[@]}"} -destination "$IOS_DEST" \
      -derivedDataPath "$derived" ARCHS=arm64 ONLY_ACTIVE_ARCH=YES "${sign[@]}" \
      COMPILER_INDEX_STORE_ENABLE=NO DEBUG_INFORMATION_FORMAT=dwarf \
      -quiet build ) >"$log" 2>&1; then
    # Kotlin errors come from the Gradle build phase as "e: file://...".
    grep -E -A2 "^e: |error:|What went wrong|BUILD FAILED" "$log" | head -60 || true
    die "iOS build failed; full log: scripts/remote.sh pull $log"
  fi
  [ -d "$APP_REL" ] || die "xcodebuild left no app at $APP_REL"
}

agent_serial() {
  local serial
  serial="$(emu_serial)"
  [ -n "$serial" ] || die "the emulator is not running: scripts/remote.sh emu start"
  echo "$serial"
}

# Clickable and labelled views on the emulator's screen with their centres, so
# a `record` script can tap them without guessing coordinates.
agent_ui() {
  local serial
  serial="$(agent_serial)"
  adb -s "$serial" shell uiautomator dump /sdcard/ui.xml >/dev/null
  adb -s "$serial" exec-out cat /sdcard/ui.xml | python3 -c '
import re, sys
for node in re.findall(r"<node [^>]*>", sys.stdin.read()):
    a = dict(re.findall(r"([\w-]+)=\"([^\"]*)\"", node))
    label = a.get("text") or a.get("content-desc")
    if not label and a.get("clickable") != "true":
        continue
    x1, y1, x2, y2 = map(int, re.findall(r"\d+", a["bounds"]))
    tap = "tap" if a.get("clickable") == "true" else "   "
    print(f"{tap} {(x1 + x2) // 2:>4} {(y1 + y2) // 2:>4}  {label}")'
}

# Records the emulator while a scripted sequence of steps runs, then measures
# it. A step is "sleep <s>" or a shell command on the device ("input tap 540
# 2230", "input keyevent BACK", "am start -n ..."); leave a pause after each
# one so its animation stands apart. screenrecord writes a frame only when the screen changes, so every
# burst of frames is one animation: each gets its timing (a gap over ~34 ms
# inside a burst is a dropped frame) and a contact sheet of its first 0.8 s
# resampled at 60 fps, where a repeated frame is a stall. The app's own jank
# counters from gfxinfo cover the whole run. Debug builds run slower than
# release ones; compare runs with each other, not with a release budget.
agent_record() {
  local name="${1:-}" serial pkg="${ANDROID_ACTIVITY%%/*}" out step rec
  [ -n "$name" ] || die "usage: record <name> <step>..."
  shift
  serial="$(agent_serial)"
  out="build/remote/recordings/$name"
  rm -rf "$out"
  mkdir -p "$out"
  adb -s "$serial" shell dumpsys gfxinfo "$pkg" reset >/dev/null
  adb -s "$serial" shell rm -f /sdcard/rec.mp4
  adb -s "$serial" shell screenrecord --bit-rate 16000000 /sdcard/rec.mp4 &
  rec=$!
  sleep 1.5
  for step in "$@"; do
    case "$step" in
      sleep\ *) sleep "${step#sleep }" ;;
      *) adb -s "$serial" shell "$step" ;;
    esac
  done
  sleep 1
  adb -s "$serial" shell pkill -INT screenrecord || true
  wait "$rec" || true
  sleep 1  # screenrecord finishes the file after SIGINT
  adb -s "$serial" pull /sdcard/rec.mp4 "$out/video.mp4" >/dev/null
  adb -s "$serial" shell dumpsys gfxinfo "$pkg" >"$out/gfxinfo.txt"

  echo "recording $out/video.mp4"
  grep -E "Total frames|Janky frames|percentile|Number (Slow|Frame deadline)" "$out/gfxinfo.txt" \
    | sed 's/^ */  gfxinfo  /' | awk '!seen[$0]++' | head -12
  analyze_video "$out"
}

# Splits $1/video.mp4 into animations and measures each, as described above
# agent_record. Any recording that writes frames only on change works, the
# iOS simulator's `simctl io recordVideo` included.
analyze_video() {
  local out="$1" i start dur frames maxgap drops
  ffprobe -v error -select_streams v:0 -show_entries frame=pts_time -of csv=p=0 "$out/video.mp4" \
    | python3 -c '
import sys
# simctl recordings carry side data, which ffprobe prints as a trailing ",..."
ts = sorted(float(f) for f in (l.split(",")[0].strip() for l in sys.stdin) if f)
bursts, cur = [], [ts[0]] if ts else []
for t in ts[1:]:
    if t - cur[-1] > 0.25:
        bursts.append(cur)
        cur = []
    cur.append(t)
bursts.append(cur)
for b in (b for b in bursts if len(b) > 2):
    gaps = [y - x for x, y in zip(b, b[1:])]
    print(f"{b[0]:.3f} {b[-1] - b[0]:.3f} {len(b)} {max(gaps) * 1000:.0f} {sum(g > 0.034 for g in gaps)}")' \
    >"$out/bursts.txt"

  i=0
  while read -r start dur frames maxgap drops; do
    i=$((i + 1))
    ffmpeg -v error -y -ss "$start" -t 0.8 -i "$out/video.mp4" -vf "fps=60,scale=180:-1,\
drawtext=fontfile=/System/Library/Fonts/Supplemental/Arial.ttf:text='%{eif\:t*1000\:d} ms':\
x=6:y=6:fontsize=20:fontcolor=white:box=1:boxcolor=black@0.6,tile=8x6:padding=2" \
      -frames:v 1 "$out/anim-$i.png"
    printf '  anim %d  at %6.2fs  %5.0f ms  %3d frames  max gap %3d ms  gaps>34ms %d  -> %s\n' \
      "$i" "$start" "$(echo "$dur * 1000" | bc)" "$frames" "$maxgap" "$drops" "$out/anim-$i.png"
  done <"$out/bursts.txt"
}

agent_doctor() {
  echo "host      $(hostname -s): $(sysctl -n machdep.cpu.brand_string), $(( $(sysctl -n hw.memsize) / 1073741824 )) GB, macOS $(sw_vers -productVersion)"
  echo "disk      $(df -h "$HOME" | awk 'NR == 2 { print $4 }') free"
  echo "xcode     $(xcodebuild -version 2>/dev/null | awk 'NR == 1' | grep . || echo MISSING)"
  echo "ios       $(xcrun simctl list runtimes 2>/dev/null | grep -c '^iOS ' || true) simulator runtime(s)"
  echo "java      ${JAVA_HOME:-MISSING}"
  echo "android   $([ -x "$ANDROID_HOME/platform-tools/adb" ] && echo "$ANDROID_HOME" || echo "MISSING platform-tools in $ANDROID_HOME")"
  echo "avd       $(emulator -list-avds 2>/dev/null | grep -v '^INFO' | tr '\n' ' ' || true)(using $REMOTE_AVD)"
  echo "emulator  $(agent_emu status)"
  echo "gradle    $(pgrep -f "$GRADLE_USER_HOME/.*GradleDaemon" | wc -l | tr -d ' ') daemon(s) warm in $GRADLE_USER_HOME"
  echo "power     $(pmset -g | awk '$1 == "sleep" || $1 == "womp" || $1 == "autorestart" { printf "%s=%s ", $1, $2 }')"
}

if [ "${1:-}" = __agent ]; then
  shift
  cd "$REPO_DIR"
  agent_env
  cmd="${1:-}"
  shift || true
  case "$cmd" in
    android) agent_android ;;
    ios) agent_ios ;;
    gradle) ./gradlew --console=plain "$@" 2>&1 ;;
    xcodebuild) cd "$IOS_DIR" && xcodebuild "$@" 2>&1 ;;
    emu) agent_emu "$@" ;;
    adb) adb -s "$(agent_serial)" "$@" ;;
    ui) agent_ui ;;
    record) agent_record "$@" ;;
    frames) analyze_video "build/remote/recordings/$1" ;;
    doctor) agent_doctor ;;
    *)
      case " $PROJECT_COMMANDS " in
        *" $cmd "*) mkdir -p build/remote; "agent_$cmd" "$@" ;;
        *) die "unknown agent command '$cmd'" ;;
      esac ;;
  esac
  exit 0
fi

# ================================================================= local half

# One multiplexed connection serves every ssh and rsync call of a run. ssh keeps
# the first value it sees for an option, so per-call overrides go before these.
SSH_OPTS=(-o BatchMode=yes -o ConnectTimeout=10 -o ServerAliveInterval=15
  -o StrictHostKeyChecking=accept-new
  -o ControlMaster=auto -o "ControlPath=$HOME/.ssh/cm-%C" -o ControlPersist=15m)
LOCAL_ADB="${ADB:-$HOME/Library/Android/sdk/platform-tools/adb}"
SCREEN_PIDFILE="$REPO_DIR/build/remote/screen.pid"
# The remote adb server shows up here on 5038, so the local one on 5037 keeps
# serving USB phones and local emulators; 27200 is scrcpy's own socket (a local
# scrcpy uses 27183).
ADB_TUNNEL_PORT=5038
SCRCPY_PORT=27200
REMOTE_ABS=""
T_SYNC=0

# RemoteBuilds/<repo> for a main checkout, RemoteBuilds/<repo>--<worktree> for a
# git worktree of it: worktrees of different repositories may share a name.
mirror_dir() {
  local common main
  common="$(git -C "$REPO_DIR" rev-parse --path-format=absolute --git-common-dir 2>/dev/null || true)"
  main="$(basename "$(dirname "${common:-$REPO_DIR/.git}")")"
  if [ "$main" = "$(basename "$REPO_DIR")" ]; then echo "RemoteBuilds/$main"; else echo "RemoteBuilds/$main--$(basename "$REPO_DIR")"; fi
}
REMOTE_DIR="${REMOTE_DIR:-$(mirror_dir)}"

q() { printf '%q ' "$@"; }

# Creates the mirror and refuses to reuse a directory it did not create, since
# the sync deletes whatever is not in the local tree.
PREPARE_SH='set -e; mkdir -p "$1"; cd "$1"
if [ -n "$(ls -A)" ] && [ ! -f "$2" ]; then echo "remote: $PWD exists and was not created by scripts/remote.sh; point REMOTE_DIR elsewhere" >&2; exit 3; fi
touch "$2"; pwd -P'
prepare() {
  REMOTE_ABS="$(ssh "${SSH_OPTS[@]}" "$REMOTE_HOST" "bash -c $(q "$PREPARE_SH") prepare $(q "$REMOTE_DIR" "$MARKER")")" \
    || die "no ssh access to $REMOTE_HOST, or the mirror is unusable (see .agents/remote-build.md)"
}

# Local edits go over, uncommitted ones included; build output, IDE state and
# secrets do not (.env only with SYNC_ENV=1, for a build that reads it).
# Excluded paths are also never deleted on the remote, which is what keeps its
# caches warm.
SYNC_EXCLUDES=(
  --exclude=.git/ --exclude=build/ --exclude=.gradle/ --exclude=.kotlin/
  --exclude=.idea/ --exclude='*.iml' --exclude=xcuserdata/ --exclude=.DS_Store
  --exclude=/reports/ --exclude=node_modules/ --exclude=/local.properties
  --exclude=/.claude/worktrees/  # each worktree syncs to a mirror of its own
  --exclude=keystore.properties --exclude='*.jks' --exclude='*.keystore' --exclude='*.p8'
  --exclude="/$MARKER"
  ${SYNC_EXCLUDES_EXTRA[@]+"${SYNC_EXCLUDES_EXTRA[@]}"}
)
[ "$SYNC_ENV" = 1 ] || SYNC_EXCLUDES+=(--exclude=/.env)

sync_tree() {
  local t=$SECONDS
  [ -n "$REMOTE_ABS" ] || prepare
  rsync -a --delete "${SYNC_EXCLUDES[@]}" -e "ssh ${SSH_OPTS[*]}" "$REPO_DIR/" "$REMOTE_HOST:$REMOTE_DIR/"
  T_SYNC=$((SECONDS - t))
}

# Compressed and incremental: a Wi-Fi link between the two Macs can be as slow
# as 1 MB/s, and a relinked app binary is mostly unchanged blocks.
fetch() { # <path in the mirror> <local destination>, rsync semantics
  mkdir -p "$(dirname "$2")"
  rsync -az --delete -e "ssh ${SSH_OPTS[*]}" "$REMOTE_HOST:$REMOTE_DIR/$1" "$2"
}

# Build slots on the remote. Every build there, from any checkout and from CI
# (`slot`), holds one while it runs; the rest wait in line, oldest first. With
# more builds at once than the remote has cores and memory for, all of them
# slow down several times over (load 30 on 12 cores, gigabytes of swap) instead
# of finishing one after another. Two slots by default; the size is read from
# ~/RemoteBuilds/.queue/size on the remote at every turn of the wait.
# A slot is a directory, made atomically, with its holder's pid in it, so the
# slot of a killed run is taken back. The holder keeps it until its stdin
# closes: here that is the ssh connection of the run that asked, so a run that
# dies or loses the network frees its slot as well.
LEASE_SH='q="$HOME/RemoteBuilds/.queue"; label="$1"; me="$(date +%s)-$$"; held=""; said=""; t0=$(date +%s)
mkdir -p "$q/slots" "$q/waiting"; echo "$label" >"$q/waiting/$me"
trap '\''rm -f "$q/waiting/$me"; [ -z "$held" ] || rm -rf "$held"'\'' EXIT; trap "exit 1" HUP INT TERM PIPE
alive() { [ -n "$1" ] && kill -0 "$1" 2>/dev/null; }
while :; do
  size=$(cat "$q/size" 2>/dev/null || echo 2)
  for f in "$q/waiting"/*; do [ -e "$f" ] && ! alive "${f##*-}" && rm -f "$f"; done
  for d in "$q/slots"/*; do
    [ -e "$d" ] || continue
    if [ -f "$d/pid" ]; then alive "$(cat "$d/pid")" || rm -rf "$d"
    elif [ -n "$(find "$d" -maxdepth 0 -mmin +1)" ]; then rm -rf "$d"; fi
  done
  if [ "$(ls "$q/waiting" | sort -n | head -1)" = "$me" ]; then
    i=1
    while [ "$i" -le "$size" ]; do
      if mkdir "$q/slots/$i" 2>/dev/null; then
        held="$q/slots/$i"; echo "$label" >"$held/label"; echo $$ >"$held/pid"; break 2
      fi
      i=$((i + 1))
    done
  fi
  now="$(ls "$q/waiting" | sort -n | grep -n -x "$me" | cut -d: -f1) $(cat "$q"/slots/*/label 2>/dev/null | paste -sd, -)"
  if [ "$now" != "$said" ]; then
    echo "== queued on $(hostname -s): place ${now%% *}, building now: ${now#* }" >&2; said="$now"
  fi
  sleep 1
done
rm -f "$q/waiting/$me"
echo "slot ${held##*/} $(( $(date +%s) - t0 ))"
cat >/dev/null'

LEASE_PID=""
HOLDER_PID=""
T_QUEUE=0
# <label>: waits for a build slot (on the remote, or here with REMOTE_HOST=local)
# and keeps it until release or exit. REMOTE_QUEUE=0 skips the line.
lease() {
  [ -z "$LEASE_PID" ] && [ "${REMOTE_QUEUE:-1}" != 0 ] || return 0
  local dir line main=$$
  dir="$(mktemp -d "${TMPDIR:-/tmp}/remote-lease.XXXXXX")"
  mkfifo "$dir/in"
  # A connection of its own: a shared one outlives this run by ControlPersist.
  if [ "$REMOTE_HOST" = local ]; then
    bash -c "$LEASE_SH" lease "$1" <"$dir/in" >"$dir/out" &
  else
    ssh -o ControlPath=none "${SSH_OPTS[@]}" "$REMOTE_HOST" "bash -c $(q "$LEASE_SH") lease $(q "$1")" \
      <"$dir/in" >"$dir/out" &
  fi
  LEASE_PID=$!
  # The fifo's only writer is a process of its own, not this shell: anything
  # this shell starts would inherit the descriptor (an ssh master that rsync
  # forks, say) and keep the slot taken after the run. It also goes when this
  # run is killed outright.
  ( exec 3>"$dir/in" 2>/dev/null; while kill -0 "$main" 2>/dev/null; do sleep 1; done ) &
  HOLDER_PID=$!
  until line="$(grep -m1 '^slot ' "$dir/out" 2>/dev/null)"; do
    kill -0 "$LEASE_PID" 2>/dev/null || { release; die "could not get a build slot on $REMOTE_HOST"; }
    sleep 0.3
  done
  rm -rf "$dir"
  set -- $line
  T_QUEUE="$3"
  [ "$T_QUEUE" -lt 2 ] || say "got build slot $2 on $REMOTE_HOST after ${T_QUEUE}s in the queue"
}

release() {
  [ -n "$LEASE_PID" ] || return 0
  pkill -P "$HOLDER_PID" 2>/dev/null || true  # its sleep holds the fifo too
  kill "$HOLDER_PID" 2>/dev/null || true
  wait "$LEASE_PID" 2>/dev/null || true
  LEASE_PID=""
}

label() { printf "%s %s" "${REMOTE_DIR#RemoteBuilds/}" "$*" | cut -c1-80; }

# Runs the remote half inside the mirror; remote paths in its output become
# local ones, so compiler errors stay clickable here. ssh does not carry the
# environment, so the settings the remote half reads go along explicitly.
agent() {
  local vars
  vars="$(q "FLAVOR=$FLAVOR" "REMOTE_AVD=$REMOTE_AVD" "IOS_CONFIGURATION=$IOS_CONFIGURATION")"
  [ -z "${REMOTE_EMU_GPU:-}" ] || vars="$vars$(q "REMOTE_EMU_GPU=$REMOTE_EMU_GPU")"
  [ -z "${REMOTE_GRADLE_HOME:-}" ] || vars="$vars$(q "REMOTE_GRADLE_HOME=$REMOTE_GRADLE_HOME")"
  ssh -n "${SSH_OPTS[@]}" "$REMOTE_HOST" "cd $(q "$REMOTE_DIR") && env $vars./scripts/remote.sh __agent $(q "$@")" \
    | sed -l "s#$REMOTE_ABS#$REPO_DIR#g"
}

# Simulators of the app's platform: the booted one, or the one to boot.
booted_sim() {
  xcrun simctl list devices booted -j 2>/dev/null | PLATFORM="$IOS_PLATFORM" python3 -c '
import json, os, sys
d = json.load(sys.stdin)["devices"]
print(next((x["udid"] for k, v in d.items() if "." + os.environ["PLATFORM"] + "-" in k for x in v), ""))'
}

# $IOS_SIM_NAME, else a Pro iPhone (any Apple Watch) from the newest runtime, else any.
pick_sim() {
  xcrun simctl list devices available -j | IOS_SIM_NAME="$IOS_SIM_NAME" PLATFORM="$IOS_PLATFORM" python3 -c '
import json, os, re, sys
d = json.load(sys.stdin)["devices"]
plat = os.environ["PLATFORM"]
kind, pro = ("Apple Watch", "Ultra") if plat == "watchOS" else ("iPhone", "Pro")
ver = lambda k: [int(n) for n in re.findall(r"\d+", k.split(plat + "-")[-1])]
runtimes = [d[k] for k in sorted((k for k in d if "." + plat + "-" in k), key=ver, reverse=True)]
devs = [x for rt in runtimes for x in rt if x["name"].startswith(kind)]
newest = [x for x in (runtimes[0] if runtimes else []) if x["name"].startswith(kind)]
pick = ([x for x in devs if x["name"] == os.environ["IOS_SIM_NAME"]]
        or [x for x in newest if pro in x["name"]] or newest or devs)
print(pick[0]["udid"] if pick else "")'
}

ios_cmd() {
  local udid="" t t_build t_pull app
  need_ios
  [ "$IOS_PLATFORM" != macOS ] || { mac_cmd; return; }
  # IOS_SIM_UDID keeps a session on its own simulator when another session has one booted too
  udid="${IOS_SIM_UDID:-$(booted_sim)}"
  if [ -z "$udid" ]; then
    udid="$(pick_sim)"
    [ -n "$udid" ] || die "no $IOS_PLATFORM simulator here (Xcode > Settings > Components)"
    say "booting simulator $udid here while the remote builds"
    xcrun simctl boot "$udid" 2>/dev/null || true  # already booting is fine
  fi
  # The slot is held until the app is back: the next build in this mirror would
  # replace it
  lease "$(label ios)"
  sync_tree
  say "building iOS on $REMOTE_HOST"
  t=$SECONDS
  agent ios
  t_build=$((SECONDS - t))
  t=$SECONDS
  app="$REPO_DIR/build/remote/ios/$IOS_PRODUCT.app"
  fetch "$APP_REL/" "$app/"
  t_pull=$((SECONDS - t))
  release
  # Xcode 27 replaced Simulator.app with DeviceHub; older Xcodes still have it.
  # The window is a convenience: the app runs either way.
  open -b com.apple.iphonesimulator --args -CurrentDeviceUDID "$udid" 2>/dev/null \
    || open -b com.apple.dt.Devices 2>/dev/null || say "no simulator window app found"
  xcrun simctl bootstatus "$udid" -b >/dev/null 2>&1 || true
  xcrun simctl install "$udid" "$app"
  xcrun simctl terminate "$udid" "$IOS_APP_ID" 2>/dev/null || true
  xcrun simctl launch "$udid" "$IOS_APP_ID" >/dev/null
  say "iOS: queue ${T_QUEUE}s, sync ${T_SYNC}s, build ${t_build}s, pull ${t_pull}s; running on simulator $udid"
}

# A macOS app: built there like the others, opened here.
mac_cmd() {
  local t t_build t_pull app
  lease "$(label app)"
  sync_tree
  say "building $IOS_PRODUCT (macOS) on $REMOTE_HOST"
  t=$SECONDS
  agent ios
  t_build=$((SECONDS - t))
  t=$SECONDS
  app="$REPO_DIR/build/remote/mac/$IOS_PRODUCT.app"
  fetch "$APP_REL/" "$app/"
  t_pull=$((SECONDS - t))
  release
  osascript -e "tell application id \"$IOS_APP_ID\" to quit" >/dev/null 2>&1 || true
  open "$app"
  say "macOS: queue ${T_QUEUE}s, sync ${T_SYNC}s, build ${t_build}s, pull ${t_pull}s; opened $app"
}

screen_open() { [ -f "$SCREEN_PIDFILE" ] && kill -0 "$(cat "$SCREEN_PIDFILE")" 2>/dev/null; }

android_cmd() {
  local t serial apk
  need_android
  if [ "${1:-}" = --local ]; then
    [ -x "$LOCAL_ADB" ] || die "adb not found at $LOCAL_ADB (set ADB)"
    serial="${ANDROID_SERIAL:-$("$LOCAL_ADB" devices | awk 'NR > 1 && $2 == "device" && !s { print $1; s = 1 }')}"
    [ -n "$serial" ] || die "no emulator or phone connected here; start one, or drop --local to use the remote emulator"
    lease "$(label android --local)"
    sync_tree
    say "building Android${FLAVOR:+ ($FLAVOR)} on $REMOTE_HOST"
    t=$SECONDS
    agent gradle --quiet "$(assemble_task)"
    apk="$REPO_DIR/build/remote/android/$(apk_name)"
    fetch "$(apk_rel)" "$apk"
    release
    "$LOCAL_ADB" -s "$serial" install -r -t "$apk" >/dev/null
    "$LOCAL_ADB" -s "$serial" shell am start -n "$ANDROID_ACTIVITY" >/dev/null
    say "Android: queue ${T_QUEUE}s, sync ${T_SYNC}s, build + pull $((SECONDS - t))s; running on $serial here"
    return 0
  fi
  lease "$(label android)"
  sync_tree
  say "building Android${FLAVOR:+ ($FLAVOR)} on $REMOTE_HOST, emulator $REMOTE_AVD there"
  t=$SECONDS
  agent android
  release
  say "Android: queue ${T_QUEUE}s, sync ${T_SYNC}s, build + install $((SECONDS - t))s"
  screen_open && return 0
  if command -v scrcpy >/dev/null; then
    say "opening the scrcpy window"
    mkdir -p "$(dirname "$SCREEN_PIDFILE")"
    nohup "$REPO_DIR/scripts/remote.sh" screen >"$REPO_DIR/build/remote/screen.log" 2>&1 &
  else
    say "to see it here: brew install scrcpy, then scripts/remote.sh screen"
  fi
}

screen_cmd() {
  command -v scrcpy >/dev/null || die "scrcpy is not installed here: brew install scrcpy"
  [ -x "$LOCAL_ADB" ] || die "adb not found at $LOCAL_ADB (set ADB)"
  if screen_open; then say "the scrcpy window is already open"; return 0; fi
  # Claimed before the slow part, released only by its owner.
  mkdir -p "$(dirname "$SCREEN_PIDFILE")"
  echo $$ >"$SCREEN_PIDFILE"
  local release='[ "$(cat "$SCREEN_PIDFILE" 2>/dev/null)" = '"$$"' ] && rm -f "$SCREEN_PIDFILE"'
  trap "$release" EXIT
  sync_tree
  local serial
  serial="$(agent emu start | tail -1)"
  [ -n "$serial" ] || die "the remote emulator did not come up"
  ssh -o ControlPath=none -o ExitOnForwardFailure=yes "${SSH_OPTS[@]}" -N \
    -L "$ADB_TUNNEL_PORT:127.0.0.1:5037" -L "$SCRCPY_PORT:127.0.0.1:$SCRCPY_PORT" "$REMOTE_HOST" &
  local tunnel=$!
  trap "kill $tunnel 2>/dev/null; $release" EXIT
  until nc -z 127.0.0.1 "$ADB_TUNNEL_PORT" 2>/dev/null; do
    kill -0 "$tunnel" 2>/dev/null || die "the ssh tunnel to $REMOTE_HOST failed"
    sleep 0.2
  done
  # adb forward, not reverse: the device socket opens on the remote end of the
  # tunnel, so nothing but the tunnel has to listen on this laptop. The video
  # is capped to fit a slow Wi-Fi link; pass scrcpy options to override.
  ADB="$LOCAL_ADB" ADB_SERVER_SOCKET="tcp:127.0.0.1:$ADB_TUNNEL_PORT" scrcpy \
    --serial "$serial" --force-adb-forward --port "$SCRCPY_PORT" --no-audio \
    --video-bit-rate "${SCRCPY_BIT_RATE:-4M}" --max-size "${SCRCPY_MAX_SIZE:-1280}" \
    --window-title "$REMOTE_AVD @ $REMOTE_HOST" "$@"
}

doctor_cmd() {
  say "here"
  echo "ssh       $REMOTE_HOST: $(ssh "${SSH_OPTS[@]}" "$REMOTE_HOST" true 2>/dev/null && echo 'key login ok' || echo "NO KEY LOGIN (ssh-copy-id $REMOTE_HOST)")"
  echo "scrcpy    $(command -v scrcpy || echo 'MISSING: brew install scrcpy')"
  echo "adb       $([ -x "$LOCAL_ADB" ] && echo "$LOCAL_ADB" || echo "MISSING at $LOCAL_ADB")"
  echo "simulator $(booted_sim | grep . || echo 'none booted')"
  sync_tree
  # What pulling an app back costs: under ~5 MB/s the Wi-Fi link, not the
  # build, sets the pace (see .agents/remote-build.md).
  echo "link      $({ /usr/bin/time -p ssh "${SSH_OPTS[@]}" "$REMOTE_HOST" 'head -c 4000000 /dev/zero' >/dev/null; } 2>&1 | awk '/^real/ { printf "%.1f MB/s from the remote", 4 / ($2 > 0 ? $2 : 0.01) }')"
  say "$REMOTE_HOST:$REMOTE_ABS"
  agent doctor
}

# Parsed whole before it runs, so editing this file mid-run (bash reads scripts
# lazily) cannot feed a running build half a line.
main() {
  local t
  trap release EXIT
  case "${1:-}" in
    ios | app) shift; ios_cmd "$@" ;;
    android) shift; android_cmd "$@" ;;
    screen) shift; screen_cmd "$@" ;;
    emu) shift; sync_tree; agent emu "$@" ;;
    gradle) shift; need_gradle; lease "$(label gradle "$@")"; sync_tree; agent gradle "$@" ;;
    build)
      case "${2:-}" in
        ios | app) need_ios; lease "$(label build ios)"; sync_tree; t=$SECONDS; agent ios
          say "built $IOS_PRODUCT.app ($IOS_PLATFORM) in $((SECONDS - t))s, queue ${T_QUEUE}s, sync ${T_SYNC}s" ;;
        android) need_android; lease "$(label build android)"; sync_tree; t=$SECONDS; agent gradle --quiet "$(assemble_task)"
          say "built $(apk_name) in $((SECONDS - t))s, queue ${T_QUEUE}s, sync ${T_SYNC}s" ;;
        *) die "usage: scripts/remote.sh build ios|android" ;;
      esac ;;
    xcodebuild) shift; need_ios; lease "$(label xcodebuild "$@")"; sync_tree; agent xcodebuild "$@" ;;
    slot)
      [ -n "${3:-}" ] || die "usage: scripts/remote.sh slot <label> <command>..."
      REMOTE_HOST=local; lease "$2"; shift 2; "$@" ;;
    adb | ui) sync_tree; agent "$@" ;;
    record)
      [ -n "${2:-}" ] || die "usage: scripts/remote.sh record <name> <step>..."
      sync_tree
      agent "$@"
      fetch "build/remote/recordings/$2/" "$REPO_DIR/build/remote/recordings/$2/"
      say "fetched to build/remote/recordings/$2" ;;
    frames)
      [ -f "${2:-}" ] || die "usage: scripts/remote.sh frames <video.mp4> [name]"
      local video="$2" name="${3:-$(basename "${2%.*}")}"
      sync_tree
      # build/ is excluded from the sync, so the video goes over on its own.
      ssh "${SSH_OPTS[@]}" "$REMOTE_HOST" "mkdir -p $(q "$REMOTE_DIR/build/remote/recordings/$name")"
      rsync -az -e "ssh ${SSH_OPTS[*]}" "$video" "$REMOTE_HOST:$REMOTE_DIR/build/remote/recordings/$name/video.mp4"
      agent frames "$name"
      fetch "build/remote/recordings/$name/" "$REPO_DIR/build/remote/recordings/$name/"
      say "fetched to build/remote/recordings/$name" ;;
    pull)
      [ -n "${2:-}" ] || die "usage: scripts/remote.sh pull <path in the repo>"
      prepare
      fetch "${2%/}" "$REPO_DIR/build/remote/pulled/$(dirname "$2")/"
      say "pulled to build/remote/pulled/${2%/}" ;;
    sync) sync_tree; say "synced to $REMOTE_HOST:$REMOTE_ABS in ${T_SYNC}s" ;;
    shell) prepare; ssh -t "${SSH_OPTS[@]}" "$REMOTE_HOST" "cd $(q "$REMOTE_DIR") && exec \$SHELL -l" ;;
    doctor) doctor_cmd ;;
    *)
      if [ -n "${1:-}" ] && [[ " $PROJECT_COMMANDS " == *" $1 "* ]]; then
        "${1}_cmd" "${@:2}"
      else
        awk 'NR > 2 && /^set -euo/ { exit } NR > 2' "$0" | sed 's/^# \{0,1\}//'
        [ -z "$PROJECT_COMMANDS" ] || { echo; awk '/^#/ { sub(/^# ?/, ""); print; next } { exit }' "$REPO_DIR/scripts/remote.project.sh"; }
        exit 2
      fi ;;
  esac
}
main "$@"; exit
