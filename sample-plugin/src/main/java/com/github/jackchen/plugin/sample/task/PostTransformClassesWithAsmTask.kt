package com.github.jackchen.plugin.sample.task

import com.github.jackchen.android.sample.api.ExtensionItem
import com.github.jackchen.android.sample.api.SampleItem
import com.github.jackchen.plugin.sample.PathNodePrinter
import com.github.jackchen.plugin.sample.instrumentation.AndroidSampleTemplateCreator
import com.github.jackchen.plugin.sample.instrumentation.SampleClassHandler
import com.google.gson.Gson
import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.io.File

abstract class PostTransformClassesWithAsmTask : DefaultTask() {
  companion object {
    const val TASK_NAME = "postTransformClassesWithAsm"
  }

  @get:Input
  abstract val outputDebugLogProvider: Property<Boolean>

  @get:Input
  abstract val transformOutputsProvider: ListProperty<File>

  @get:OutputFile
  abstract val configurationOutputFileProvider: RegularFileProperty

  init {
    outputDebugLogProvider.convention(false)
    configurationOutputFileProvider.set(
      File(
        project.buildDir,
        "intermediates/$TASK_NAME/output"
      )
    )
  }

  @TaskAction
  fun postTransformClassesWithAsm() {
    if (!transformOutputsProvider.isPresent) return
    val transformOutputDirs = transformOutputsProvider.get()
    val classFileList =
      transformOutputDirs.flatMap { it.walk().filter { classFile -> classFile.name.endsWith(".class") } }
    val sampleList = SampleClassHandler.getSampleList().toMutableList()
    sampleList.removeIf { item ->
      val classPath = item.className.replace('.', '/')
      classFileList.none { it.absolutePath.endsWith("$classPath.class") }
    }
    val extensionList = SampleClassHandler.getExtensionList().toMutableList()
    extensionList.removeIf { item ->
      val classPath = item.className.replace('.', '/')
      classFileList.none { it.absolutePath.endsWith("$classPath.class") }
    }
    if (transformOutputDirs.isNotEmpty()) {
      val destFolder = transformOutputDirs.first()
      val newClassConfigurations = ClassConfigurations(sampleList, extensionList)
      createSampleConfigurationClass(destFolder, newClassConfigurations)
    }
    if (outputDebugLogProvider.get()) {
      PathNodePrinter.printPathTree(sampleList)
    }
  }

  private fun createSampleConfigurationClass(
    destFolder: File,
    newClassConfigurations: ClassConfigurations
  ) {
    val configurationJsonText = Gson().toJson(newClassConfigurations)
    val regularFile = configurationOutputFileProvider.get()
    regularFile.asFile.writeText(configurationJsonText)
    AndroidSampleTemplateCreator.create(destFolder, configurationJsonText)
  }
}
