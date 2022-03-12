// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
  id("io.github.gradle-nexus.publish-plugin") version "1.1.0"
}

group = "com.airsaid"
version = "1.0.0-SNAPSHOT"

nexusPublishing {
  repositories {
    sonatype {
      nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
      snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
    }
  }
}

buildscript {
  val isApplyPlugin by extra(false)

  repositories {
    google()
    jitpack()
    jcenter() // Warning: this repository is going to shut down soon
    maven {
      url = uri(File(rootDir, ".m2/repository"))
    }
  }

  dependencies {
    classpath("com.android.tools.build:gradle:${Versions.ANDROID_GRADLE_PLUGIN}")
    classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.KOTLIN}")
    if (isApplyPlugin) {
      classpath("com.airsaid:sample-plugin:1.0.0-SNAPSHOT")
    }
    // NOTE: Do not place your application dependencies here; they belong
    // in the individual module build.gradle files
  }
}

tasks.register<Delete>("clean") {
  delete(rootProject.buildDir)
}