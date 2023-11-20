plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
}

android {
    signingConfigs {
        create("config") {
            storeFile = file("/home/ppvan/android-key.jks")
            storePassword = "123456"
            keyAlias = "key0"
            keyPassword = "123456"
        }
    }
    namespace = "me.ppvan.moon"
    compileSdk = 34

    defaultConfig {
        applicationId = "me.ppvan.moon"
        minSdk = 30
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    // youtube-dl fix: see https://github.com/yausername/youtubedl-android/issues/135
    packaging { jniLibs { useLegacyPackaging = true } }


    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("config")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    val media3Version = "1.1.1"
    val materialVersion = "1.2.0-alpha10"

    // Simple Storage
    implementation("com.anggrayudi:storage:1.5.5")

    // ID3 Tagger
    implementation("com.mpatric:mp3agic:0.9.1")
    implementation("net.jthink:jaudiotagger:3.0.1")


    // ExoPlayer
    implementation("androidx.media3:media3-exoplayer:$media3Version")
    implementation("androidx.media3:media3-session:$media3Version")

    // YT android
    implementation("com.github.yausername.youtubedl-android:library:-SNAPSHOT")
    implementation("com.github.yausername.youtubedl-android:ffmpeg:-SNAPSHOT")

    // Networking
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    // Json
    implementation("com.beust:klaxon:5.5")

    // Web Image loader
    implementation("io.coil-kt:coil-compose:2.5.0")

    // DI
    implementation("com.google.dagger:hilt-android:2.48")
    implementation("androidx.navigation:navigation-compose:2.7.5")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")
    kapt("com.google.dagger:hilt-android-compiler:2.44")

    // added by default
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.0")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3:$materialVersion")
    implementation("androidx.compose.material:material-icons-extended:1.5.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}

kapt {
    correctErrorTypes = true
}
