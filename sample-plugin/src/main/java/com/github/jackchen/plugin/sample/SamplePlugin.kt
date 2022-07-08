package com.github.jackchen.plugin.sample

import com.android.build.api.instrumentation.FramesComputationMode
import com.android.build.api.instrumentation.InstrumentationScope
import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.gradle.AppExtension
import com.android.build.gradle.tasks.TransformClassesWithAsmTask
import com.github.jackchen.android.sample.api.ExtensionItem
import com.github.jackchen.android.sample.api.SampleItem
import com.github.jackchen.plugin.sample.instrumentation.AndroidSampleTemplateCreator
import com.github.jackchen.plugin.sample.instrumentation.SampleAsmClassVisitorFactory
import com.github.jackchen.plugin.sample.instrumentation.SampleClassHandler
import com.github.jackchen.plugin.sample.task.MergeSourceFileAndDocTask
import com.google.gson.Gson
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.configurationcache.extensions.capitalized
import java.io.File

class SamplePlugin : Plugin<Project> {
    private fun isAndroidProject(project: Project): Boolean {
        return null != project.plugins.findPlugin("com.android.application")
    }

    override fun apply(project: Project) {
        if (!isAndroidProject(project)) return
        configureTransformClass(project)
        configureCollectSourceFileAndDocTask(project)
    }

    private fun configureCollectSourceFileAndDocTask(project: Project) {
        project.afterEvaluate {
            val appExtension = project.extensions.getByType(AppExtension::class.java)
            appExtension.applicationVariants.forEach { applicationVariant ->
                val task = project.tasks.create(
                    MergeSourceFileAndDocTask.TASK_NAME + "With" +
                            applicationVariant.flavorName.capitalized() +
                            applicationVariant.buildType.name,
                    MergeSourceFileAndDocTask::class.java
                )
                if (applicationVariant.mergeAssetsProvider.isPresent) {
                    val mergeAssetsTask = applicationVariant.mergeAssetsProvider.get()
                    mergeAssetsTask.dependsOn(task)
                }
            }
        }
    }

    private fun configureTransformClass(project: Project) {
        val androidComponentsExtension = project.extensions.getByType(AndroidComponentsExtension::class.java)
        androidComponentsExtension.onVariants { variant ->
            // Clear the data every time when we register the transform.
            SampleClassHandler.clearData()
            variant.transformClassesWith(
                SampleAsmClassVisitorFactory::class.java,
                InstrumentationScope.PROJECT
            ) { }
            variant.setAsmFramesComputationMode(
                FramesComputationMode.COMPUTE_FRAMES_FOR_INSTRUMENTED_METHODS
            )
        }
        createSampleConfigurationClassAfterTransform(project)
    }

    private fun createSampleConfigurationClassAfterTransform(project: Project) {
        project.tasks.withType(TransformClassesWithAsmTask::class.java) { transformClassesTask ->
            transformClassesTask.doLast {
                val files = transformClassesTask.outputs.files.files
                val sampleList = SampleClassHandler.getSampleList()
                val extensionList = SampleClassHandler.getExtensionList()
                if (files.isNotEmpty()) {
                    val destFolder = files.first()
                    createSampleConfigurationClass(destFolder, sampleList, extensionList)
                }
                PathNodePrinter.printPathTree(sampleList)
            }
        }
    }

    private fun createSampleConfigurationClass(
        destFolder: File,
        sampleItemList: MutableList<SampleItem>,
        extensionList: MutableList<ExtensionItem>
    ) {
        val configurationJsonText = Gson().toJson(mapOf("samples" to sampleItemList, "extensions" to extensionList))
        AndroidSampleTemplateCreator.create(destFolder, configurationJsonText)
    }
}