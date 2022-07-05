package com.github.jackchen.plugin.sample

import com.android.build.api.dsl.AndroidSourceSet
import com.android.build.api.instrumentation.FramesComputationMode
import com.android.build.api.instrumentation.InstrumentationScope
import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.gradle.AppExtension
import com.android.build.gradle.internal.api.DefaultAndroidSourceDirectorySet
import com.android.build.gradle.internal.api.DefaultAndroidSourceSet
import com.github.jackchen.plugin.sample.instrumentation.SampleAsmClassVisitorFactory
import org.apache.commons.io.FileUtils
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile
import java.io.File
import java.io.IOException
import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes
import java.util.ArrayList

class SamplePlugin : Plugin<Project> {
    companion object {
        private const val SAMPLE_DIR_NAME = "sample"
        private const val SAMPLE_DOCUMENT_FOLDER = "document"
        private const val SAMPLE_CONFIGURATION_FILE_NAME = "sample_configuration.json"
        fun isAndroidProject(project: Project): Boolean {
            return null != project.plugins.findPlugin("com.android.application")
        }
    }

    override fun apply(project: Project) {
        if (!isAndroidProject(project)) return
        val sampleBuildDir = File(project.buildDir, SAMPLE_DIR_NAME)
//        project.tasks.withType(JavaCompile::class.java){ compileTask->
//            val output = compileTask.outputs
//            val input = compileTask.inputs
//            println()
//        }
        val androidComponentsExtension = project.extensions.getByType(AndroidComponentsExtension::class.java)
        androidComponentsExtension.onVariants { variant ->
            variant.transformClassesWith(
                SampleAsmClassVisitorFactory::class.java,
                InstrumentationScope.ALL
            ) { params ->
                params.configurationFile.set(File(sampleBuildDir, SAMPLE_CONFIGURATION_FILE_NAME))
            }
            variant.setAsmFramesComputationMode(
                FramesComputationMode.COMPUTE_FRAMES_FOR_INSTRUMENTED_METHODS
            )
        }
        mergeExtraResources(project, sampleBuildDir)
    }

    private fun mergeExtraResources(project: Project, sampleBuildDir: File) {
        val appExtension = project.extensions.getByType(AppExtension::class.java)
        appExtension.applicationVariants.forEach { applicationVariant ->
            if (applicationVariant.mergeAssetsProvider.isPresent) {
                val mergeAsserts = applicationVariant.mergeAssetsProvider.get()
                mergeAsserts.outputDir.files().files
                mergeAsserts.doFirst {
                    //Merge all the documents inside the project.
                    if (!sampleBuildDir.exists()) {
                        sampleBuildDir.mkdirs()
                    }
                    val documentBuildFile = File(sampleBuildDir, SAMPLE_DOCUMENT_FOLDER)
                    mergeDocumentFiles(project, documentBuildFile)
                    //We copy all the source file into the assets folder.
                    appExtension.sourceSets.forEach { sourceSet: AndroidSourceSet ->
                        if (sourceSet.name == "main") {
                            if (sourceSet is DefaultAndroidSourceSet) {
                                val javaSourceSet = sourceSet.java
                                if (javaSourceSet is DefaultAndroidSourceDirectorySet) {
                                    sourceSet.assets.srcDirs(javaSourceSet.srcDirs)
                                }
                                val kotlinSourceSet = sourceSet.kotlin
                                if (kotlinSourceSet is DefaultAndroidSourceDirectorySet) {
                                    sourceSet.assets.srcDirs(kotlinSourceSet.srcDirs)
                                }
                                sourceSet.assets.srcDirs(documentBuildFile.absolutePath)
                                sourceSet.assets.srcDirs(sampleBuildDir.absolutePath)
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Collect all the documents inside this project.
     *
     * @param project
     * @param outputFolder
     */
    private fun mergeDocumentFiles(project: Project, outputFolder: File) {
        //Collect all the documents inside the project.
        val rootProject = project.rootProject
        val projectDir = rootProject.projectDir
        val projectAbsolutePath = projectDir.absolutePath
        val documentList = collectDocumentFiles(project)
        for (file in documentList) {
            try {
                val absolutePath = file.absolutePath
                val fileRelativePath = absolutePath.substring(projectAbsolutePath.length + 1)
                val destFile = File(outputFolder, fileRelativePath)
                val destFolder = destFile.parentFile
                if (!destFolder.exists()) {
                    destFolder.mkdir()
                }
                FileUtils.copyFile(file, destFile)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun collectDocumentFiles(project: Project): List<File> {
        val projectDir = project.rootProject.projectDir
        val ignoreFileList: MutableList<String> = ArrayList()
        val documentList: MutableList<File> = ArrayList()
        ignoreFileList.add(".idea")
        ignoreFileList.add(".gradle")
        ignoreFileList.add("build")
        try {
            Files.walkFileTree(projectDir.toPath(), object : SimpleFileVisitor<Path>() {
                @Throws(IOException::class)
                override fun preVisitDirectory(path: Path, attributes: BasicFileAttributes): FileVisitResult {
                    val file = path.toFile()
                    val name = file.name
                    return if (ignoreFileList.contains(name)) {
                        FileVisitResult.SKIP_SUBTREE
                    } else {
                        FileVisitResult.CONTINUE
                    }
                }

                override fun visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult {
                    if (!attrs.isDirectory) {
                        val f = file.toFile()
                        if (f.name.endsWith(".md") || f.name.endsWith(".MD")) {
                            documentList.add(f)
                        }
                    }
                    return FileVisitResult.CONTINUE
                }
            })
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return documentList
    }
}