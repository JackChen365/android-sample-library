plugins {
    `java-library`
    `maven-publish`
}

group = "com.github.jackchen.android.sample.api"
version = rootProject.projectDir.resolve("VERSION_CURRENT.txt").readText().trim()

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}