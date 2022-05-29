plugins {
    id("com.android.application") version Versions.ANDROID_GRADLE_PLUGIN apply false
    id("com.android.library") version Versions.ANDROID_GRADLE_PLUGIN apply false
    id("org.jetbrains.kotlin.android") version Versions.KOTLIN apply false
    id("test.sample") version("1.0.0-SNAPSHOT") apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}