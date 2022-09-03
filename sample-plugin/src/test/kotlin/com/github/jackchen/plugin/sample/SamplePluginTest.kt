package com.github.jackchen.plugin.sample

import com.github.jackchen.gradle.test.toolkit.GradlePluginTest
import com.github.jackchen.gradle.test.toolkit.ext.TestVersion
import com.github.jackchen.gradle.test.toolkit.ext.TestWithCache
import com.github.jackchen.gradle.test.toolkit.testdsl.TestProjectIntegration
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.math.absoluteValue
import kotlin.random.Random

/**
 * @author JackChen
 */
class SamplePluginTest : GradlePluginTest() {

  private fun testProjectSetup(
    isEnableDebug: Boolean = false,
    closure: TestProjectIntegration.TestProject.() -> Unit
  ) {
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
          file("build.gradle.kts") {
            """
                            plugins {
                            	id("com.android.application")
                            	id("org.jetbrains.kotlin.android")
                            	id("io.github.jackchen365.sample")
                            }
                            sample {
                               enableDebug.set($isEnableDebug)
                            }
                            android {
                                compileSdk = 31
                                defaultConfig {
                                    applicationId = "$testPackageName"
                                    minSdk = 21
                                    targetSdk = 31
                                    versionCode = 1
                                    versionName = "1.0"
                                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                                }
                                compileOptions {
                                    sourceCompatibility = JavaVersion.VERSION_1_8
                                    targetCompatibility = JavaVersion.VERSION_1_8
                                }
                                kotlinOptions {
                                    jvmTarget = "1.8"
                                }
                            }
                            dependencies {
                            	implementation("androidx.core:core-ktx:1.7.0")
                            	implementation("androidx.appcompat:appcompat:1.4.1")
                             implementation("io.github.jackchen365:sample-api:$pluginVersion")
                            }
                        """.trimIndent()
          }
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
        apply(closure)
      }
    }
  }

  @Test
  @TestVersion(androidVersion = "7.2.1", gradleVersion = "7.4.1")
  fun `test sample plugin build`() {
    // :app:assembleDebug
    testProjectSetup {
      build(":app:transformDebugClassesWithAsm") {
        Assertions.assertEquals(
          TaskOutcome.SUCCESS,
          task(":app:transformDebugClassesWithAsm")?.outcome
        )
      }
    }
  }

  @Test
  @TestVersion(androidVersion = "7.2.1", gradleVersion = "7.4.1")
  fun `test assemble build`() {
    // :app:assembleDebug
    testProjectSetup {
      build(":app:assembleDebug") {
        Assertions.assertEquals(TaskOutcome.SUCCESS, task(":app:assembleDebug")?.outcome)
      }
    }
  }

  @Test
  @TestVersion(androidVersion = "7.2.1", gradleVersion = "7.4.1")
  fun `test debug enable extension`() {
    testProjectSetup(isEnableDebug = true) {
      build(":app:transformDebugClassesWithAsm") {
        Assertions.assertEquals(
          TaskOutcome.SUCCESS,
          task(":app:transformDebugClassesWithAsm")?.outcome
        )
        Assertions.assertTrue(output.contains("+---The test dialog"))
        Assertions.assertTrue(output.contains("+---The test fragment2"))
      }
    }
  }

  private fun buildTask(
    gradleRunner: GradleRunner,
    task: String,
    assertions: BuildResult.() -> Unit = {}
  ) {
    val buildArgumentList = mutableListOf<String>()
    buildArgumentList.add(task)
//    buildArgumentList.add("--info")
    gradleRunner
      .withArguments(buildArgumentList)
      .withDebug(true)
      .build()
      .run { assertions() }
  }

  @Test
  @TestWithCache(true)
  @TestVersion(androidVersion = "7.2.1", gradleVersion = "7.4.1")
  fun `test plugin with incremental`() {
    testProjectSetup(isEnableDebug = true) {
      val gradleRunnerProvider = SampleGradleRunnerProvider()
      val gradleRunner = gradleRunnerProvider.getGradleRunner(
        testProjectRunner.projectDir,
        testProjectRunner.testVersions.supportedGradleVersion
      )
      File(projectDir, "app/src/main/kotlin/com/android/test").deleteRecursively()
      module("app") {
        kotlinSourceDir("com.android.test") {
          // New file with new method. So we always remove an old file and add a new class file to test the incremental change.
          val newClassName = "TestDialog${Random.nextInt().absoluteValue}"
          file("$newClassName.kt") {
            """
              |package com.android.test
              |import androidx.appcompat.app.AppCompatDialog
              |@com.github.jackchen.android.sample.api.Register(title="The test $newClassName")
              |class $newClassName{
              |   fun testMethod(){
              |     println("testMethod")
              |   }
              |}
            """.trimMargin()
          }
        }
      }
      buildTask(gradleRunner, ":app:transformDebugClassesWithAsm") {
        Assertions.assertEquals(
          TaskOutcome.SUCCESS,
          task(":app:transformDebugClassesWithAsm")?.outcome
        )
      }
    }
  }
}
