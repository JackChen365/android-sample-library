package com.github.jackchen.plugin.sample.visitor.annotaiton;

class ExtensionAnnotationHandler : AnnotationHandler {
    companion object {
        private val SAMPLE_ANNOTATION_DESC =
            "L" + com.github.jackchen.android.sample.api.Extension::class.java.name.replace('.', '/') + ";"
    }
    private var isSampleExtension: Boolean = false

    override fun accept(desc: String, visible: Boolean): Boolean {
        if (SAMPLE_ANNOTATION_DESC == desc) {
            isSampleExtension = true
            return true
        }
        return false
    }

    override fun visit(name: String, value: Any?) {
    }

    override fun visitEnd() {
        isSampleExtension = false
    }

    fun isSampleExtension() = isSampleExtension
}