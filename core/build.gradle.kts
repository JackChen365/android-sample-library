plugins {
  id("com.android.library")
  kotlin("android")
  id("maven-central-publish")
}

android {
  compileSdk = Versions.App.COMPILE_SDK

  defaultConfig {
    minSdk = Versions.App.MIN_SDK
    targetSdk = Versions.App.TARGET_SDK
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    consumerProguardFiles("consumer-rules.pro")
  }

  buildTypes {
    getByName("release") {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }

  buildFeatures{
    viewBinding=true
  }
}

dependencies {
  api(project(":api"))
  implementation(Libs.AndroidX.KTX)
  implementation(Libs.AndroidX.APPCOMPAT)
  implementation(Libs.AndroidX.CONSTRAINTLAYOUT)
  implementation(Libs.AndroidX.RECYCLERVIEW)
  implementation(Libs.AndroidX.START_UP)
  implementation(Libs.GSON)
}
