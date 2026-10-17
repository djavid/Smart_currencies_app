# Remote builds on the Mac mini

Smart currencies (Android) builds on the Mac mini (`mac-mini` on the tailnet), like every Nybs
app: `scripts/remote.sh` mirrors the working tree, uncommitted changes
included, to `~/RemoteBuilds/Smart_currencies_app` there (`Smart_currencies_app--<worktree>` for a git
worktree), builds it, and only the finished app comes back.

- Commands for this app: `android`, `android --local`, `gradle <args>` (e.g. `:app:testDebugUnitTest`); `scripts/remote.sh` alone lists them all,
  `scripts/remote.sh doctor` checks both Macs.
- Compiler errors in the output point at local files.
- `scripts/remote.sh` is the same file in every Nybs app repository; what is
  this app's own is in `scripts/remote.project.sh`. Change `remote.sh` in one
  repository and copy it to the others unchanged.
- Builds of all apps share one queue on the mini, two at a time: a build that
  prints `queued on mac-mini: place N` is waiting, not stuck.
- The full story (network, the queue, sharing the mini with work builds,
  troubleshooting): `.agents/remote-build.md` in PsyTests or SmartSubs-Mobile.
