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
    private val sampleList = mutableListOf<SampleItem>()
    private val extensionList = mutableListOf<ExtensionItem>()

    init {
        val componentAnnotationHandler = ComponentAnnotationHandler()
        componentAnnotationHandler.whenFoundClass { classVisitor->
            val sampleItem = componentAnnotationHandler.sampleItem
            if(null != sampleItem){
                sampleItem.className = classVisitor.getClassName()
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
            extensionItem.className = classVisitor.getClassName()
            extensionItem.superClass = classVisitor.getSuperClassName()
            extensionItem.interfaces = classVisitor.getInterfaces()
            extensionList.add(extensionItem)
        }
        sampleAnnotationHandlerList.add(sampleExtensionAnnotationHandler)
    }

    fun accept(classVisitor: SampleClassVisitor, desc: String, visible: Boolean): AnnotationHandler?{
        return sampleAnnotationHandlerList.find { it.accept(classVisitor, desc, visible) }
    }

    fun getSampleList() = sampleList

    fun getExtensionList() = extensionList

    fun clearData(){
        sampleList.clear()
        extensionList.clear()
    }
}