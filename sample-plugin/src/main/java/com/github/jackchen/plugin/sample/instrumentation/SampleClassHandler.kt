package com.github.jackchen.plugin.sample.instrumentation

import com.github.jackchen.android.sample.api.ExtensionItem
import com.github.jackchen.android.sample.api.SampleItem
import com.github.jackchen.plugin.sample.visitor.SampleClassVisitor
import com.github.jackchen.plugin.sample.visitor.annotaiton.AnnotationHandler
import com.github.jackchen.plugin.sample.visitor.annotaiton.ComponentAnnotationHandler
import com.github.jackchen.plugin.sample.visitor.annotaiton.ExpandableAnnotationHandler

object SampleClassHandler {
  private val SAMPLE_TEST_CASE_DESC =
    "L" + com.github.jackchen.android.sample.api.TestCase::class.java.name.replace('.', '/') + ";"
  private val SAMPLE_EXTENSION_DESC =
    "L" + com.github.jackchen.android.sample.api.Extension::class.java.name.replace('.', '/') + ";"

  private val sampleAnnotationHandlerList: MutableList<AnnotationHandler> = ArrayList()

  private val sampleMap = hashMapOf<String, SampleItem>()
  private val extensionMap = hashMapOf<String, ExtensionItem>()

  init {
    val componentAnnotationHandler = ComponentAnnotationHandler()
    componentAnnotationHandler.whenFoundClass { classVisitor ->
      val sampleItem = componentAnnotationHandler.sampleItem
      if (null != sampleItem) {
        val className = classVisitor.getClassName()
        sampleItem.className = className
        sampleMap[className] = sampleItem
      }
    }
    sampleAnnotationHandlerList.add(componentAnnotationHandler)

    val testcaseAnnotationHandler = ExpandableAnnotationHandler(listOf(SAMPLE_TEST_CASE_DESC))
    testcaseAnnotationHandler.whenFoundClass {
      val sampleItem = componentAnnotationHandler.sampleItem
      if (null != sampleItem) {
        sampleItem.isTestCase = true
      }
    }
    sampleAnnotationHandlerList.add(testcaseAnnotationHandler)

    val sampleExtensionAnnotationHandler = ExpandableAnnotationHandler(listOf(SAMPLE_EXTENSION_DESC))
    sampleExtensionAnnotationHandler.whenFoundClass { classVisitor ->
      val extensionItem = ExtensionItem()
      val className = classVisitor.getClassName()
      extensionItem.className = className
      extensionItem.superClass = classVisitor.getSuperClassName()
      extensionItem.interfaces = classVisitor.getInterfaces()
      extensionMap[className] = extensionItem
    }
    sampleAnnotationHandlerList.add(sampleExtensionAnnotationHandler)
  }

  fun accept(classVisitor: SampleClassVisitor, desc: String, visible: Boolean): AnnotationHandler? {
    return sampleAnnotationHandlerList.find { it.accept(classVisitor, desc, visible) }
  }

  fun getSampleList(): List<SampleItem> = sampleMap.values.toList()

  fun getExtensionList(): List<ExtensionItem> = extensionMap.values.toList()

  /**
   * For incremental scenarios, if the new class no longer matches,
   * it needs to be removed from the list to avoid generating dirty data.
   *
   * @param notMatchClassName Class names that do not conform to the processing rules.
   */
  fun cleanDirtyData(notMatchClassName: String) {
    if (sampleMap.containsKey(notMatchClassName)) {
      sampleMap.remove(notMatchClassName)
    }
    if (extensionMap.containsKey(notMatchClassName)) {
      extensionMap.remove(notMatchClassName)
    }
  }
}
