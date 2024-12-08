plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-kapt")
    kotlin("plugin.serialization") version libs.versions.kotlin
}

val baseUrlUserContent = "baseUrlUserContent"
val baseUrlGoogle = "baseUrlGoogle"

android {
    namespace = "com.example.effectivemobiletest"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.effectivemobiletest"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", baseUrlUserContent, "\"https://drive.usercontent.google.com/u/0/\"")
        buildConfigField("String", baseUrlGoogle, "\"https://drive.google.com/\"")

    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    /** Android */
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.fragment)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.constraintlayout)

    /** Test */
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    /** Network, Data and Security*/
    implementation(libs.squareup.retrofit)
    implementation(libs.squareup.okhttp)
    implementation(libs.squareup.logging.interceptor)

    /** Serialization */
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.kotlinx.serialization.converter)

    /** ViewModel */
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    /** imageview */
    implementation(libs.circleimageview)

    /** Glide */
    implementation(libs.glide)

    /** Keyboard visibility event */
    implementation(libs.keyboardvisibilityevent)

    /** Koin */
    implementation(libs.koin)

    /** Joda */
    implementation(libs.android.joda)

}