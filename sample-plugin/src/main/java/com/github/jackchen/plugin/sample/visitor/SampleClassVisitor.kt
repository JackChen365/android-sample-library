package com.github.jackchen.plugin.sample.visitor

import com.github.jackchen.android.sample.api.ExtensionItem
import com.github.jackchen.android.sample.api.SampleItem
import com.github.jackchen.plugin.sample.visitor.annotaiton.AnnotationHandler
import com.github.jackchen.plugin.sample.visitor.annotaiton.ExpandableAnnotationHandler
import com.github.jackchen.plugin.sample.visitor.annotaiton.ComponentAnnotationHandler
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes

class SampleClassVisitor(classVisitor: ClassVisitor?) : ClassVisitor(Opcodes.ASM6, classVisitor) {
    companion object {
        private const val SUPER_CLASS_NAME = "com/github/jackchen/android/core/appcompat/SampleAppCompatActivity"
        private const val ANDROIDX_COMPAT_ACTIVITY_CLASS_NAME = "androidx/appcompat/app/AppCompatActivity"
        private const val ANDROIDX_FRAGMENT_ACTIVITY_CLASS_NAME = "androidx/fragment/app/FragmentActivity"
        private const val ANDROIDX_FRAGMENT_CLASS_NAME = "androidx/fragment/app/Fragment"

        private val SAMPLE_TEST_CASE_DESC =
            "L" + com.github.jackchen.android.sample.api.TestCase::class.java.name.replace('.', '/') + ";"
        private val SAMPLE_EXTENSION_DESC =
            "L" + com.github.jackchen.android.sample.api.Extension::class.java.name.replace('.', '/') + ";"

        private val SAMPLE_CLASS_LIST: MutableList<String> = ArrayList()

        init {
            SAMPLE_CLASS_LIST.add(ANDROIDX_COMPAT_ACTIVITY_CLASS_NAME)
            SAMPLE_CLASS_LIST.add(ANDROIDX_FRAGMENT_ACTIVITY_CLASS_NAME)
            SAMPLE_CLASS_LIST.add(ANDROIDX_FRAGMENT_CLASS_NAME)
        }
        private val sampleAnnotationHandlerList: MutableList<AnnotationHandler> = ArrayList()
        private val sampleList = mutableListOf<SampleItem>()
        private val extensionList = mutableListOf<ExtensionItem>()

        init {
            val componentAnnotationHandler = ComponentAnnotationHandler()
            componentAnnotationHandler.whenFoundClass {
                val sampleItem = componentAnnotationHandler.sampleItem
                if(null != sampleItem){
                    sampleList.add(sampleItem)
                }
            }
            sampleAnnotationHandlerList.add(componentAnnotationHandler)
            val testcaseAnnotationHandler = ExpandableAnnotationHandler(listOf(SAMPLE_TEST_CASE_DESC))
            testcaseAnnotationHandler.whenFoundClass {
                val sampleItem = componentAnnotationHandler.sampleItem
                if(null != sampleItem){
                    sampleItem.isTestCase = true
                }
            }
            sampleAnnotationHandlerList.add(testcaseAnnotationHandler)
            val sampleExtensionAnnotationHandler = ExpandableAnnotationHandler(listOf(SAMPLE_EXTENSION_DESC))
            sampleExtensionAnnotationHandler.whenFoundClass { classVisitor->
                val extensionItem = ExtensionItem()
                extensionItem.className = classVisitor.className.replace('/', '.')
                extensionItem.superClass = classVisitor.superClass
                extensionItem.interfaces = classVisitor.interfaceArray
                extensionList.add(extensionItem)
            }
            sampleAnnotationHandlerList.add(sampleExtensionAnnotationHandler)
        }
    }
    private lateinit var superClass: String
    private var interfaceArray = emptyArray<String>()
    private lateinit var className: String


    override fun visit(
        version: Int, access: Int, name: String,
        signature: String?, superName: String, interfaces: Array<String>
    ) {
        className = name
        superClass = superName
        interfaceArray = interfaces
        //Here we change the super class name to our appcompat class.
        if (ANDROIDX_COMPAT_ACTIVITY_CLASS_NAME == superName || ANDROIDX_FRAGMENT_ACTIVITY_CLASS_NAME == superName) {
            super.visit(version, access, name, signature, SUPER_CLASS_NAME, interfaces)
        } else {
            super.visit(version, access, name, signature, superName, interfaces)
        }
    }

    override fun visitAnnotation(desc: String, visible: Boolean): AnnotationVisitor {
        val annotationVisitor =
            sampleAnnotationHandlerList.find { it.accept(this, desc, visible) }
        if (null != annotationVisitor) {
            return object : AnnotationVisitor(Opcodes.ASM6, super.visitAnnotation(desc, visible)) {
                override fun visit(name: String, value: Any?) {
                    super.visit(name, value)
                    annotationVisitor.visit(name, value)
                }
            }
        }
        return super.visitAnnotation(desc, visible)
    }
}