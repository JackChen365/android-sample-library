package com.github.jackchen.plugin.sample

import com.github.jackchen.gradle.test.toolkit.GradlePluginTest
import com.github.jackchen.gradle.test.toolkit.ext.TestVersion
import com.github.jackchen.gradle.test.toolkit.ext.TestWithCache
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.File

class SamplePluginTest : GradlePluginTest() {

    private fun testProjectSetup(closure: GradlePluginTest.() -> Unit) {
        val testPackageName = "com.github.jackchen.plugin.test"
        kotlinAndroidTemplate {
            template {
                `package` {
                    packageName = testPackageName
                }
                plugins {
                    id("test.sample").version(testPluginVersion())
                }
                dependencies {
                    implementation("androidx.core:core-ktx:1.7.0")
                    implementation("androidx.appcompat:appcompat:1.4.1")
                    files(File("libs/api-1.0.0-SNAPSHOT.jar").absolutePath)
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
    @TestVersion(androidVersion = "7.1.3", gradleVersion = "7.4.2")
    fun `test sample plugin transform classes`() {
        //:app:transformClassesWithSampleForDebug
        testProjectSetup {
            build(":app:compileReleaseKotlin") {
                Assertions.assertEquals(TaskOutcome.SUCCESS, task(":app:compileReleaseKotlin")?.outcome)
            }
        }
    }

    @Test
    @TestVersion(androidVersion = "7.1.3", gradleVersion = "7.2")
    fun `test sample plugin build`() {
        //:app:assembleDebug
        testProjectSetup {
            build(":app:assembleDebug") {
                Assertions.assertEquals(TaskOutcome.SUCCESS, task(":app:assembleDebug")?.outcome)
            }
        }
    }
}