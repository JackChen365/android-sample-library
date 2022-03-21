plugins {
  id("com.android.library")
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
}

dependencies {
  implementation(Libs.AndroidX.APPCOMPAT)
  implementation(Libs.AndroidX.CONSTRAINTLAYOUT)
  implementation(Libs.MATERIAL)
  implementation("com.github.momodae.RecyclerViewLibrary2:adapter:1.0.1")
  api(project(":core"))
//  api(Libs.SAMPLE_CORE)
}
