package com.github.jackchen.plugin.sample

import com.github.jackchen.gradle.test.toolkit.GradlePluginTest
import com.github.jackchen.gradle.test.toolkit.ext.TestVersion
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.File

class SamplePluginTest : GradlePluginTest() {

    private fun testProjectSetup(closure: GradlePluginTest.() -> Unit) {
        val pluginVersion = System.getProperty("io.github.jackchen365.sample.version")
        val testPackageName = "com.github.jackchen.plugin.test"
        kotlinAndroidTemplate {
            template {
                `package` {
                    packageName = testPackageName
                }
                plugins {
                    id("io.github.jackchen365.sample").version(pluginVersion)
                }
                dependencies {
                    implementation("androidx.core:core-ktx:1.7.0")
                    implementation("androidx.appcompat:appcompat:1.4.1")
                    implementation("io.github.jackchen365:sample-api:$pluginVersion")
                }
            }
            project {
                module("app") {
                    sourceDir(testPackageName) {
                        file("test.md") {
                            """
                                ### Readme
                                This is a test mark down file.
                            """.trimIndent()
                        }
                        file("TestFragment.kt") {
                            """
                            |package ${packageName()}
                            |import androidx.appcompat.app.AppCompatActivity
                            |@com.github.jackchen.android.sample.api.Register(title="The test fragment2")
                            |class TestFragment : AppCompatActivity()
                            """.trimMargin()
                        }
                        file("TestDialog.kt") {
                            """
                            |package $testPackageName
                            |import androidx.appcompat.app.AppCompatDialog
                            |@com.github.jackchen.android.sample.api.Register(title="The test dialog")
                            |class TestDialog
                            """.trimMargin()
                        }
                    }
                }
            }
        }
        apply(closure)
    }

    @Test
    @TestVersion(androidVersion = "7.2.1", gradleVersion = "7.4.1")
    fun `test sample plugin build`() {
        //:app:assembleDebug
        testProjectSetup {
            build(":app:transformDebugClassesWithAsm") {
                Assertions.assertEquals(TaskOutcome.SUCCESS, task(":app:transformDebugClassesWithAsm")?.outcome)
            }
        }
    }

    @Test
    @TestVersion(androidVersion = "7.2.1", gradleVersion = "7.4.1")
    fun `test assemble build`() {
        //:app:assembleDebug
        testProjectSetup {
            build(":app:assembleDebug") {
                Assertions.assertEquals(TaskOutcome.SUCCESS, task(":app:assembleDebug")?.outcome)
            }
        }
    }
}