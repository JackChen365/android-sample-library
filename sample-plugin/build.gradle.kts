import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val pluginGroup = "com.github.jackchen.plugin.sample"
group = pluginGroup
version = rootProject.projectDir.resolve("VERSION_CURRENT.txt").readText().trim()

plugins {
    kotlin("jvm")
    `java-gradle-plugin`
    `maven-publish`
}

gradlePlugin {
    plugins {
        register("sample") {
            id = "test.sample"
            implementationClass = "com.github.jackchen.plugin.sample.SamplePlugin"
        }
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

configurations.compileOnly.configure { isCanBeResolved = true }
configurations.api.configure { isCanBeResolved = true }

tasks.withType<PluginUnderTestMetadata>().configureEach {
    pluginClasspath.from(configurations.compileOnly)
}

// Test tasks loods plugin from local maven repository
tasks.named("test").configure {
    dependsOn("publishToMavenLocal")
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

/**
 * A special configuration that add internal project into this plugin
 */
val internalLibs: Configuration by configurations.creating
configurations.compileOnly.configure { extendsFrom(internalLibs) }
tasks.withType<org.gradle.jvm.tasks.Jar>().configureEach {
    internalLibs.files.forEach { file ->
        if (file.isDirectory) {
            from(file)
        } else {
            from(zipTree(file))
        }
    }
}

dependencies {
    compileOnly(gradleApi())
    compileOnly(libs.kotlin.gradle.plugin)
    compileOnly(libs.android.gradle.plugin)
    compileOnly(libs.java.asm)
    compileOnly(libs.java.asm.util)
    compileOnly(libs.jdom2)
    compileOnly(libs.gson)
    compileOnly(libs.commons.io)
    compileOnly(kotlin("stdlib-jdk8"))
    internalLibs(projects.api)

    testImplementation(gradleTestKit())
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.kotlin.reflect)
    testImplementation(libs.commons.io)
    testImplementation(libs.gradle.test.toolkit)
}