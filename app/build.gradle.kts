plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.realm.kotlin)
}

android {
    namespace = "com.djavid.bitcoinrate"

    defaultConfig {
        applicationId = "com.djavid.bitcoinrate"
        versionCode = libs.versions.versionCode.get().toInt()
        versionName = libs.versions.versionName.get()

        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        compileSdk = libs.versions.compileSdk.get().toInt()

        vectorDrawables {
            useSupportLibrary = true
        }
        multiDexEnabled = false
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            versionNameSuffix = "-release"
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            resValue("bool", "FIREBASE_ANALYTICS_DEACTIVATED", "false")
            buildConfigField("boolean", "ANALYTICS_DEACTIVATED", "false")
        }
        debug {
            isDebuggable = true
            versionNameSuffix = "-debug"
            resValue("bool", "FIREBASE_ANALYTICS_DEACTIVATED", "true")
            buildConfigField("boolean", "ANALYTICS_DEACTIVATED", "true")
        }
    }

    kotlinOptions {
        jvmTarget = libs.versions.jvmTarget.get()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.toVersion(libs.versions.jvmTarget.get())
        targetCompatibility = JavaVersion.toVersion(libs.versions.jvmTarget.get())
    }

    buildFeatures {
        buildConfig = true
    }

    lint {
        abortOnError = false
    }

//    realm {
//        syncEnabled = true
//    }
}

dependencies {
    //kotlin
    implementation(libs.kotlin.stdlib)
    implementation(libs.rxjava)
    implementation(libs.rxandroid)
    implementation(libs.kodein.generic.jvm)
    implementation(libs.kodein.android.support)
    implementation(libs.room.runtime)

    //android
    implementation(libs.material)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.vectordrawable)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.cardview)
    implementation(libs.constraintlayout)

    //firebase
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.core)
    implementation(libs.firebase.crash)
    implementation(platform(libs.firebase.bom))

    //network
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.okhttp.loggingInterceptor)
    implementation(libs.gson)
    implementation(libs.retrofit.adapter.rxjava2)

    //views
    implementation(libs.graphview)
    implementation(libs.mpandroidchart)
    implementation(libs.tooltips)
    implementation(libs.easypopup)
    implementation(libs.bottomnavigationviewex)
    implementation(libs.searchdialog)
    implementation(libs.glide)
    implementation(libs.circleimageview)
    implementation(libs.segmentedbutton)

    //others
    implementation(libs.joda)
    implementation(libs.stream)
}