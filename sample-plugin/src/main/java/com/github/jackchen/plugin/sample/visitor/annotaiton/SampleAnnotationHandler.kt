package com.github.jackchen.plugin.sample.visitor.annotaiton;

import com.github.jackchen.android.sample.api.Register
import com.github.jackchen.android.sample.api.TestCase

class SampleAnnotationHandler : AnnotationHandler {
    companion object {
        private val SAMPLE_ANNOTATION_DESC =
            "L" + Register::class.java.name.replace('.', '/') + ";"
        private val SAMPLE_TEST_CASE_DESC =
            "L" + TestCase::class.java.name.replace('.', '/') + ";"
    }

    private var sampleItem: com.github.jackchen.android.sample.api.SampleItem? = null

    override fun accept(desc: String, visible: Boolean): Boolean {
        if (SAMPLE_ANNOTATION_DESC == desc) {
            sampleItem = com.github.jackchen.android.sample.api.SampleItem()
            return true
        }
        return false
    }

    override fun visit(name: String, value: Any?) {
        when (name) {
            "title" -> sampleItem?.title = value.toString()
            "desc" -> sampleItem?.desc = value.toString()
            "path" -> sampleItem?.path = value.toString()
        }
    }

    override fun visitEnd() {
        sampleItem = null
    }

    fun getSampleItem(): com.github.jackchen.android.sample.api.SampleItem? {
        return sampleItem
    }
}