package com.github.jackchen.plugin.sample.transform

import com.android.build.api.transform.*
import com.android.build.api.transform.QualifiedContent.DefaultContentType
import com.android.build.gradle.AppExtension
import com.android.build.gradle.internal.tasks.CompressAssetsTask
import com.github.jackchen.android.sample.api.ExtensionItem
import com.github.jackchen.android.sample.api.SampleItem
import com.github.jackchen.plugin.sample.PathNodePrinter
import com.github.jackchen.plugin.sample.SamplePlugin
import com.github.jackchen.plugin.sample.visitor.SampleClassVisitor
import com.github.jackchen.plugin.sample.visitor.annotaiton.ExtensionAnnotationHandler
import com.github.jackchen.plugin.sample.visitor.annotaiton.SampleAnnotationHandler
import com.github.jackchen.plugin.sample.visitor.annotaiton.SampleTestCaseHandler
import com.google.gson.Gson
import org.apache.commons.io.FileUtils
import org.gradle.api.Project
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.UncheckedIOException
import java.nio.file.Files
import java.nio.file.Path
import java.util.function.Consumer
import java.util.jar.JarEntry
import java.util.jar.JarOutputStream
import kotlin.reflect.jvm.internal.impl.builtins.StandardNames.FqNames.target


class SampleTransform(private val project: Project, private val configurationFile: File) : Transform() {
    private val sampleAnnotationHandler = SampleAnnotationHandler()
    private val extensionAnnotationHandler = ExtensionAnnotationHandler()
    private val sampleTestCaseHandler = SampleTestCaseHandler()

    override fun getName(): String {
        return "Sample"
    }

    override fun getInputTypes(): Set<QualifiedContent.ContentType> {
        return setOf<QualifiedContent.ContentType>(DefaultContentType.CLASSES)
    }

    override fun getScopes(): MutableSet<in QualifiedContent.Scope>? {
        return mutableSetOf(QualifiedContent.Scope.PROJECT)
    }

    override fun getReferencedScopes(): MutableSet<in QualifiedContent.Scope>? {
        return mutableSetOf(QualifiedContent.Scope.PROJECT)
    }

    override fun isIncremental(): Boolean {
        return false
    }

    @Throws(IOException::class)
    override fun transform(transformInvocation: TransformInvocation) {
        if (transformInvocation.isIncremental) {
            throw UnsupportedOperationException("Unsupported incremental build!")
        }
        val outputProvider = transformInvocation.outputProvider
        outputProvider.deleteAll()
        //Copy all the jar and classes to the where they need to...
        for (input in transformInvocation.inputs) {
            input.jarInputs.parallelStream().forEach { jarInput: JarInput ->
                val dest = outputProvider
                    .getContentLocation(
                        jarInput.name, jarInput.contentTypes, jarInput.scopes,
                        Format.JAR
                    )
                if (dest.exists()) {
                    throw RuntimeException(
                        "Jar file " + jarInput.name + " already exists!" +
                                " src: " + jarInput.file.path + ", dest: " + dest.path
                    )
                }
                try {
                    FileUtils.copyFile(jarInput.file, dest)
                } catch (e: IOException) {
                    throw UncheckedIOException(e)
                }
            }
        }
        val sampleItemList: MutableList<SampleItem> = mutableListOf()
        val extensionList: MutableList<ExtensionItem> = mutableListOf()
        for (input in transformInvocation.inputs) {
            input.directoryInputs?.forEach(Consumer { dir: DirectoryInput ->
                try {
                    if (dir.file.isDirectory) {
                        Files.walk(dir.file.toPath()).filter { path: Path ->
                            val name = path.toFile().name
                            name.endsWith(".class") && !name.startsWith("R$") &&
                                    "R.class" != name && "BuildConfig.class" != name
                        }.forEach { path: Path ->
                            val classFile = path.toFile()
                            try {
                                processJavaClassFile(
                                    classFile,
                                    sampleItemList,
                                    extensionList
                                )
                            } catch (e: IOException) {
                                e.printStackTrace()
                            }
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                try {
                    val destFolder = outputProvider.getContentLocation(
                        dir.name, dir.contentTypes, dir.scopes,
                        Format.DIRECTORY
                    )
                    FileUtils.copyDirectory(dir.file, destFolder)
                } catch (e: IOException) {
                    throw UncheckedIOException(e)
                }
            })
        }
        //Output the debug log.
        PathNodePrinter.printPathTree(sampleItemList)
        //Write the sample configuration to file
        createSampleConfigurationClass(transformInvocation, sampleItemList, extensionList)
    }

    private fun createSampleConfigurationClass(
        transformInvocation: TransformInvocation,
        sampleItemList: MutableList<SampleItem>,
        extensionList: MutableList<ExtensionItem>
    ) {
        val configurationJsonText = Gson().toJson(mapOf("samples" to sampleItemList, "extensions" to extensionList))
        val transformInput = transformInvocation.inputs.first { it.directoryInputs.isNotEmpty() }
        val directoryInput = transformInput.directoryInputs.first()
        if (null != directoryInput) {
            val destFolder = transformInvocation.outputProvider.getContentLocation(
                directoryInput.name, directoryInput.contentTypes, directoryInput.scopes,
                Format.DIRECTORY
            )
            AndroidSampleTemplateCreator.create(destFolder, configurationJsonText)
        }
    }

    @Throws(IOException::class)
    private fun processJavaClassFile(
        file: File,
        sampleItemList: MutableList<SampleItem>,
        extensionList: MutableList<ExtensionItem>
    ) {
        //The first step: We process context classes include the Application class file.
        val bytes = Files.readAllBytes(file.toPath())
        try {
            //Change the super activity class of this sample.
            processSampleClass(file, bytes, sampleItemList, extensionList)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @Throws(IOException::class)
    private fun processSampleClass(
        file: File, bytes: ByteArray,
        sampleItemList: MutableList<SampleItem>,
        extensionList: MutableList<ExtensionItem>
    ) {
        val classReader = ClassReader(bytes)
        val classWriter = ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
        val sampleClassVisitor = SampleClassVisitor(classWriter)
        sampleClassVisitor.addAnnotationHandler(sampleAnnotationHandler)
        sampleClassVisitor.addAnnotationHandler(extensionAnnotationHandler)
        sampleClassVisitor.addAnnotationHandler(sampleTestCaseHandler)
        classReader.accept(sampleClassVisitor, ClassReader.EXPAND_FRAMES)
        val sampleItem = sampleAnnotationHandler.getSampleItem()
        if (null != sampleItem) {
            sampleItem.className = sampleClassVisitor.getClassName()
            sampleItemList.add(sampleItem)
        }
        if (sampleTestCaseHandler.isTestCase) {
            sampleItem?.isTestCase = true
        }
        if (extensionAnnotationHandler.isSampleExtension()) {
            val extensionItem = ExtensionItem()
            extensionItem.className = sampleClassVisitor.getClassName()
            extensionItem.superClass = sampleClassVisitor.getSuperClass()
            extensionItem.interfaces = sampleClassVisitor.getInterfaces()
            extensionList.add(extensionItem)
        }
        sampleAnnotationHandler.visitEnd()
        extensionAnnotationHandler.visitEnd()
        sampleTestCaseHandler.visitEnd()
        val code = classWriter.toByteArray()
        val fos = FileOutputStream(
            file.parentFile.absoluteFile.toString() + File.separator + file.name
        )
        fos.write(code)
        fos.close()
    }
}