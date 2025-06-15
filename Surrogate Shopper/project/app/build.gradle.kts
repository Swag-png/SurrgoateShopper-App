plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "project.com2025"
    compileSdk = 35

    defaultConfig {
        applicationId = "project.com2025"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    // OkHttp - latest stable
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    // Google Play services for location and maps
    implementation("com.google.android.gms:play-services-location:21.3.0")
    implementation("com.google.android.gms:play-services-maps:19.2.0")

    // Google Maps Utils library
    implementation("com.google.maps.android:android-maps-utils:3.13.0")

    // Google Places SDK - latest version only
    implementation("com.google.android.libraries.places:places:4.3.1")

    // AndroidX and UI dependencies
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
