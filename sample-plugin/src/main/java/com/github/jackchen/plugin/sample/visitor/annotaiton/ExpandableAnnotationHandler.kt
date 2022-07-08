package com.github.jackchen.plugin.sample.visitor.annotaiton;

import com.github.jackchen.plugin.sample.visitor.SampleClassVisitor

class ExpandableAnnotationHandler(private val classList: List<String>) : AnnotationHandler() {
    override fun accept(classVisitor: SampleClassVisitor, desc: String, visible: Boolean): Boolean {
        if(classList.any { it == desc }){
            whenFoundClass?.invoke(classVisitor)
            return true
        }
        return false
    }

    override fun visit(name: String, value: Any?) =Unit
}