import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.hilt.android)
    kotlin("plugin.serialization") version "1.9.22"
    id("androidx.navigation.safeargs.kotlin")
    kotlin("kapt")
}

// local.properties 사용을 위함
val properties = Properties()
properties.load(FileInputStream(rootProject.file("local.properties")))

android {
    namespace = "com.skymilk.wallpaperapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.skymilk.wallpaperapp"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String","WALLHAVEN_API_KEY", properties.getProperty("wallhaven_api_key"))
        buildConfigField("String","PIXABAY_API_KEY", properties.getProperty("pixabay_api_key"))
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //serialization
    implementation(libs.kotlinx.serialization.json)

    //layout
    implementation(libs.androidx.swiperefreshlayout)

    //size
    implementation(libs.ssp.android)
    implementation(libs.sdp.android)

    //navigation
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // Retrofit
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.converter.gson)
    implementation (libs.logging.interceptor)

    //dagger hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

    //paging3
    implementation(libs.androidx.paging.runtime.ktx)

    //Glide
    implementation(libs.glide)
    kapt(libs.glide.compiler)
    implementation(libs.glide.transformations)

    //progress bar
    implementation(libs.android.spinkit)
    implementation(libs.kenburnsview)

    //permission
    implementation(libs.tedpermission.normal)

    //shimmer
    implementation (libs.shimmer)

    //image Editor
    implementation(libs.photoeditor)
}