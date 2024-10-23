plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.myapplication"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.myapplication"
        minSdk = 24
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.0")

    implementation ("com.microsoft.signalr:signalr:9.0.0-preview.7.24406.2")
    implementation ("org.slf4j:slf4j-simple:1.7.30")

    implementation ("com.squareup.picasso:picasso:2.8")

    testImplementation ("junit:junit:4.12")

    // required-for-robolectric (1)
    testImplementation ("org.robolectric:robolectric:4.2")

    // mockito for unit tests
    testImplementation ("org.mockito:mockito-core:2.23.4")
    // mockito for android tests
    androidTestImplementation ("org.mockito:mockito-android:2.23.4")

    // for instrument / integration test
    testImplementation ("androidx.test:core:1.1.0")
    androidTestImplementation ("androidx.test.ext:junit:1.1.0")
    androidTestImplementation ("androidx.test:runner:1.2.0-beta01")
    androidTestImplementation ("androidx.test:rules:1.1.1")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.2.0-beta01")
    androidTestImplementation ("androidx.annotation:annotation:1.0.2")

    // for UI test
    androidTestImplementation ("androidx.test.espresso:espresso-intents:3.1.1")


    implementation ("com.google.firebase:firebase-firestore:24.4.0")
    implementation ("com.google.gms:google-services:4.4.2")
    implementation(platform("com.google.firebase:firebase-bom:33.2.0"))
    implementation("com.google.firebase:firebase-database")
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.junit.junit)
}