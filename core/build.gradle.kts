plugins {
  id("com.android.library")
  id("org.jetbrains.kotlin.android")
  id("com.vanniktech.maven.publish")
  id("org.jlleitschuh.gradle.ktlint")
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

  buildFeatures {
    viewBinding = true
  }
}

dependencies {
  api(projects.api)
  implementation(libs.androidx.ktx)
  implementation(libs.androidx.appcompat)
  implementation(libs.androidx.constraintlayout)
  implementation(libs.androidx.recyclerview)
  implementation(libs.androidx.startup.runtime)
  implementation(libs.gson)
}

ktlint {
  debug.set(true)
  android.set(true)
  additionalEditorconfigFile.set(file("$rootDir/.editorconfig"))
}
