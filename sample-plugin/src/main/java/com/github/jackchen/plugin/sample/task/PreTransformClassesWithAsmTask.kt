package com.github.jackchen.plugin.sample.task

import com.github.jackchen.plugin.sample.instrumentation.SampleClassHandler
import com.google.gson.Gson
import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.io.File

abstract class PreTransformClassesWithAsmTask : DefaultTask() {
  companion object {
    const val TASK_NAME = "preTransformClassesWithAsm"
  }

  @get:OutputFile
  abstract val configurationOutputFileProvider: RegularFileProperty

  init {
    configurationOutputFileProvider.set(
      File(
        project.buildDir,
        "intermediates/$TASK_NAME/output"
      )
    )
  }

  @TaskAction
  fun postTransformClassesWithAsm() {
    val regularFile = configurationOutputFileProvider.get()
    val configurationOutputFile = regularFile.asFile
    if (configurationOutputFile.exists()) {
      val configurationOutputCaching = configurationOutputFile.readText()
      val classConfigurations = Gson().fromJson(configurationOutputCaching, ClassConfigurations::class.java)
      SampleClassHandler.loadDataFromCache(classConfigurations)
    }
  }
}
