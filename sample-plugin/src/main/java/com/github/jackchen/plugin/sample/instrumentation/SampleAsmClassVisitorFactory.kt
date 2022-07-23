package com.github.jackchen.plugin.sample.instrumentation

import com.android.build.api.instrumentation.AsmClassVisitorFactory
import com.android.build.api.instrumentation.ClassContext
import com.android.build.api.instrumentation.ClassData
import com.android.build.api.instrumentation.InstrumentationParameters
import com.github.jackchen.android.sample.api.Extension
import com.github.jackchen.android.sample.api.Register
import com.github.jackchen.android.sample.api.TestCase
import com.github.jackchen.plugin.sample.visitor.SampleClassVisitor
import org.objectweb.asm.ClassVisitor

/**
 * @author JackChen
 */
abstract class SampleAsmClassVisitorFactory :
  AsmClassVisitorFactory<InstrumentationParameters.None> {

  companion object {
    private val ANNOTATION_CLASS_NAMES = hashSetOf<String>(
      Register::class.java.name,
      TestCase::class.java.name,
      Extension::class.java.name,
    )
  }

  override fun createClassVisitor(
    classContext: ClassContext,
    nextClassVisitor: ClassVisitor
  ): ClassVisitor {
    return SampleClassVisitor(nextClassVisitor)
  }

  override fun isInstrumentable(classData: ClassData): Boolean {
    val superClassName = classData.superClasses[0]
    if (superClassName.isCompatActivityClassName() || superClassName.isFragmentActivityClassName()) {
      return true
    }
    classData.classAnnotations.forEach {
      if (ANNOTATION_CLASS_NAMES.contains(it)) {
        return true
      }
    }
    return false
  }

  private fun String.isCompatActivityClassName(): Boolean {
    return this == SampleClassVisitor.ANDROIDX_COMPAT_ACTIVITY_CLASS_NAME.replace("/", ".")
  }

  private fun String.isFragmentActivityClassName(): Boolean {
    return this == SampleClassVisitor.ANDROIDX_FRAGMENT_ACTIVITY_CLASS_NAME.replace("/", ".")
  }
}