package com.github.jackchen.plugin.sample.visitor.annotaiton

interface AnnotationHandler {
    fun accept(desc: String, visible: Boolean): Boolean
    fun visit(name: String, value: Any?)
    fun visitEnd()
}