package com.cz.android.sample.library.visitor.annotaiton;

import com.cz.android.sample.api.Extension

/**
 * Created on 2022/3/5.
 *
 * @author Jack Chen
 * @email zhenchen@tubi.tv
 */
class ExtensionAnnotationHandler : AnnotationHandler {
    companion object {
        private val SAMPLE_ANNOTATION_DESC =
            "L" + Extension::class.java.name.replace('.', '/') + ";"
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