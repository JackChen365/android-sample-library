package com.github.jackchen.plugin.sample.instrumentation

import com.android.build.api.instrumentation.AsmClassVisitorFactory
import com.android.build.api.instrumentation.ClassContext
import com.android.build.api.instrumentation.ClassData
import com.android.build.api.instrumentation.InstrumentationContext
import com.android.build.api.instrumentation.InstrumentationParameters
import com.github.jackchen.plugin.sample.visitor.SampleClassVisitor
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.Optional
import org.objectweb.asm.ClassVisitor
import java.io.File


class SampleAsmClassVisitorFactory(
    override val instrumentationContext: InstrumentationContext,
    override val parameters: Property<SpanAddingParameters>
) : AsmClassVisitorFactory<SampleAsmClassVisitorFactory.SpanAddingParameters> {

    interface SpanAddingParameters : InstrumentationParameters {
        @get:Input
        @get:Optional
        val configurationFile: Property<File>
    }

//    private fun createSampleConfigurationClass(
//        transformInvocation: TransformInvocation,
//        sampleItemList: MutableList<SampleItem>,
//        extensionList: MutableList<ExtensionItem>
//    ) {
//        val configurationJsonText = Gson().toJson(mapOf("samples" to sampleItemList, "extensions" to extensionList))
//        val transformInput = transformInvocation.inputs.first { it.directoryInputs.isNotEmpty() }
//        val directoryInput = transformInput.directoryInputs.first()
//        if (null != directoryInput) {
//            val destFolder = transformInvocation.outputProvider.getContentLocation(
//                directoryInput.name, directoryInput.contentTypes, directoryInput.scopes,
//                Format.DIRECTORY
//            )
//            AndroidSampleTemplateCreator.create(destFolder, configurationJsonText)
//        }
//    }
    override fun createClassVisitor(classContext: ClassContext, nextClassVisitor: ClassVisitor): ClassVisitor {
        //        //Output the debug log.
        //        PathNodePrinter.printPathTree(sampleItemList)
        //        //Write the sample configuration to file
        //        createSampleConfigurationClass(transformInvocation, sampleItemList, extensionList)
        return SampleClassVisitor(nextClassVisitor)
    }

    override fun isInstrumentable(classData: ClassData): Boolean {
        return true
    }
}