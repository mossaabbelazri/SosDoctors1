plugins {
    alias(libs.plugins.androidApplication)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.sosdoctors"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.sosdoctors"
        minSdk = 26
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {

    

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation (libs.play.services.maps)


    implementation (platform(libs.firebase.bom))
    implementation (libs.firebase.auth)
    implementation("com.google.firebase:firebase-analytics")

    implementation (libs.firebase.messaging)
    implementation (libs.gson)

    implementation (libs.guava.v2701android)

    implementation (libs.retrofit)
    implementation(libs.converter.gson)
    implementation (libs.firebase.firestore)
    implementation (libs.okhttp)
    implementation ("org.java-websocket:Java-WebSocket:1.5.6")



}