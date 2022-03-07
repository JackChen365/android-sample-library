package com.cz.android.sample.library.visitor.annotaiton

interface AnnotationHandler {
    fun accept(desc: String, visible: Boolean): Boolean
    fun visit(name: String, value: Any?)
    fun visitEnd()
}