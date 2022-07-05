package com.github.jackchen.plugin.sample.visitor.annotaiton

import com.github.jackchen.plugin.sample.visitor.SampleClassVisitor

abstract class AnnotationHandler {
    var whenFoundClass: ((SampleClassVisitor)->Unit)?=null

    abstract fun accept(classVisitor: SampleClassVisitor,desc: String, visible: Boolean): Boolean
    abstract fun visit(name: String, value: Any?)

    fun whenFoundClass(closure:(SampleClassVisitor)->Unit){
        whenFoundClass = closure
    }
}