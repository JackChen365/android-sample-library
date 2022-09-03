package com.github.jackchen.plugin.sample

import com.android.build.api.instrumentation.FramesComputationMode
import com.android.build.api.instrumentation.InstrumentationScope
import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.gradle.AppExtension
import com.android.build.gradle.tasks.TransformClassesWithAsmTask
import com.github.jackchen.plugin.sample.extension.SampleExtension
import com.github.jackchen.plugin.sample.instrumentation.SampleAsmClassVisitorFactory
import com.github.jackchen.plugin.sample.task.MergeSourceFileAndDocTask
import com.github.jackchen.plugin.sample.task.PostTransformClassesWithAsmTask
import com.github.jackchen.plugin.sample.task.PreTransformClassesWithAsmTask
import com.github.jackchen.plugin.sample.util.isAndroidProject
import org.gradle.api.*
import org.gradle.configurationcache.extensions.capitalized
import java.io.File

/**
 * @author JackChen
 */
class SamplePlugin : Plugin<Project> {

  companion object {
    private const val EXTENSION_NAME = "sample"
  }

  override fun apply(project: Project) {
    if (!project.isAndroidProject()) {
      throw GradleException("${project.name} is not an Android project.")
    }
    val sampleExtension = project.extensions.create(EXTENSION_NAME, SampleExtension::class.java)
    configureTransformClass(project)
    createSampleConfigurationClassAfterTransform(project, sampleExtension)
    configureCollectSourceFileAndDocTask(project)
  }

  private fun configureTransformClass(project: Project) {
    val androidComponentsExtension =
      project.extensions.getByType(AndroidComponentsExtension::class.java)
    androidComponentsExtension.onVariants { variant ->
      variant.transformClassesWith(
        SampleAsmClassVisitorFactory::class.java,
        InstrumentationScope.PROJECT
      ) {}
      variant.setAsmFramesComputationMode(
        FramesComputationMode.COMPUTE_FRAMES_FOR_INSTRUMENTED_METHODS
      )
    }
  }

  private fun configureCollectSourceFileAndDocTask(project: Project) {
    project.afterEvaluate {
      val appExtension = project.extensions.getByType(AppExtension::class.java)
      appExtension.applicationVariants.forEach { applicationVariant ->
        val task = project.tasks.create(
          MergeSourceFileAndDocTask.TASK_NAME + "With" +
            applicationVariant.flavorName.capitalized() +
            applicationVariant.buildType.name.capitalized(),
          MergeSourceFileAndDocTask::class.java
        )
        if (applicationVariant.mergeAssetsProvider.isPresent) {
          val mergeAssetsTask = applicationVariant.mergeAssetsProvider.get()
          mergeAssetsTask.dependsOn(task)
        }
      }
    }
  }

  private fun createSampleConfigurationClassAfterTransform(
    project: Project,
    sampleExtension: SampleExtension
  ) {
    val configurationOutputFile = File(
      project.buildDir,
      "intermediates/${PostTransformClassesWithAsmTask.TASK_NAME}/output"
    )
    val preTransformClassesWithAsmTask = project.tasks.create(
      PreTransformClassesWithAsmTask.TASK_NAME,
      PreTransformClassesWithAsmTask::class.java
    ) { task ->
      task.configurationOutputFileProvider.set(configurationOutputFile)
      task.outputs.upToDateWhen { false }
    }
    val postTransformClassesWithAsmTask = project.tasks.create(
      PostTransformClassesWithAsmTask.TASK_NAME,
      PostTransformClassesWithAsmTask::class.java
    ) { task ->
      task.outputDebugLogProvider.set(sampleExtension.enableDebug)
      task.configurationOutputFileProvider.set(configurationOutputFile)
      task.outputs.upToDateWhen { false }
    }
    project.tasks.withType(TransformClassesWithAsmTask::class.java) { transformTask ->
      transformTask.doLast(object : Action<Task> {
        override fun execute(t: Task) {
          val outputFiles = transformTask.outputs.files.files
          postTransformClassesWithAsmTask.transformOutputsProvider.set(outputFiles)
        }
      })
      transformTask.dependsOn(preTransformClassesWithAsmTask)
      transformTask.finalizedBy(postTransformClassesWithAsmTask)
    }
  }
}
