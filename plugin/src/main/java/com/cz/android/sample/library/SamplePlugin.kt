package com.cz.android.sample.library

import com.android.build.api.dsl.AndroidSourceDirectorySet
import com.android.build.gradle.AppExtension
import com.android.build.gradle.api.AndroidSourceSet
import com.android.build.gradle.api.ApplicationVariant
import com.cz.android.sample.library.transform.SampleTransform
import org.apache.commons.io.FileUtils
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File
import java.io.IOException
import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes
import java.util.ArrayList
import java.util.function.Consumer

/**
 * @author Created by cz
 * @date 2020-05-17 16:28
 * @email bingo110@126.com
 */
class SamplePlugin : Plugin<Project> {
    companion object {
        fun isAndroidProject(project: Project): Boolean {
            val plugins = project.plugins
            return null != plugins.findPlugin("com.android.application")
        }
    }

    override fun apply(project: Project) {
        if (!isAndroidProject(project)) return
        //Merge all the documents inside the project.
        val appExtension = project.extensions.getByType(AppExtension::class.java)
        val sampleBuildFile = File(project.buildDir, "sample")
        sampleBuildFile.mkdirs()
        project.afterEvaluate {
            for (variant in appExtension.applicationVariants) {
                if (variant.assembleProvider.isPresent) {
                    val assemble = variant.assembleProvider.get()
                    assemble.inputs.file(sampleBuildFile.absolutePath)
                }
            }
        }
        val sampleConfigurationFile = File(sampleBuildFile, "sample_configuration.json")
        val documentBuildFile = File(sampleBuildFile, "document")
        mergeDocument(project, documentBuildFile)

        //We copy all the source file into the assets folder.
        appExtension.sourceSets.forEach { sourceSet: AndroidSourceSet ->
            if (sourceSet.name == "main") {
                sourceSet.assets.srcDirs(sourceSet.java.srcDirs)
            }
        }
        //Register the gradle transform.
        val sampleTransform = SampleTransform(sampleConfigurationFile)
        appExtension.registerTransform(sampleTransform)
    }

    /**
     * Collect all the documents inside this project.
     *
     * @param project
     * @param outputFolder
     */
    private fun mergeDocument(project: Project, outputFolder: File) {
        //Collect all the documents inside the project.
        val rootProject = project.rootProject
        val projectDir = rootProject.projectDir
        val projectAbsolutePath = projectDir.absolutePath
        val documentList = collectDocuments(project)
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

    private fun collectDocuments(project: Project): List<File> {
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
                        val name = f.name
                        if (name.endsWith(".md") || name.endsWith(".MD")) {
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