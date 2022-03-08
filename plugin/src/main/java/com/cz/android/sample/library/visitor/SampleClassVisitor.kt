package com.cz.android.sample.library.visitor

import com.cz.android.sample.library.visitor.annotaiton.AnnotationHandler
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes

/**
 * @author Created by cz
 * @date 2020-05-17 15:12
 * @email bingo110@126.com
 */
class SampleClassVisitor(classVisitor: ClassVisitor?) : ClassVisitor(Opcodes.ASM6, classVisitor) {
    companion object {
        private const val SUPER_CLASS_NAME = "com/cz/android/sample/library/appcompat/SampleAppCompatActivity"
        private const val ANDROIDX_COMPAT_ACTIVITY_CLASS_NAME = "androidx/appcompat/app/AppCompatActivity"
        private const val ANDROIDX_FRAGMENT_ACTIVITY_CLASS_NAME = "androidx/fragment/app/FragmentActivity"
        private const val ANDROIDX_FRAGMENT_CLASS_NAME = "androidx/fragment/app/Fragment"
        private val SAMPLE_CLASS_LIST: MutableList<String> = ArrayList()

        init {
            SAMPLE_CLASS_LIST.add(ANDROIDX_COMPAT_ACTIVITY_CLASS_NAME)
            SAMPLE_CLASS_LIST.add(ANDROIDX_FRAGMENT_ACTIVITY_CLASS_NAME)
            SAMPLE_CLASS_LIST.add(ANDROIDX_FRAGMENT_CLASS_NAME)
        }
    }

    private val sampleAnnotationHandlerList: MutableList<AnnotationHandler> = ArrayList()
    private lateinit var superClass: String
    private var interfaceArray = emptyArray<String>()
    private lateinit var className: String
    var isDemonstrable = false

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
            sampleAnnotationHandlerList.find { it.accept(desc, visible) }
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

    fun getClassName(): String {
        return className.replace('/', '.')
    }

    fun getSuperClass(): String = superClass

    fun getInterfaces() = interfaceArray

    fun addAnnotationHandler(annotationHandler: AnnotationHandler) {
        sampleAnnotationHandlerList.add(annotationHandler)
    }
}