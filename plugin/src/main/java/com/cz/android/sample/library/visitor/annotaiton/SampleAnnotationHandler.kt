package com.cz.android.sample.library.visitor.annotaiton;

import com.cz.android.sample.api.Register
import com.cz.android.sample.api.SampleItem
import com.cz.android.sample.api.TestCase

/**
 * Created on 2022/3/5.
 *
 * @author Jack Chen
 * @email zhenchen@tubi.tv
 */
class SampleAnnotationHandler : AnnotationHandler {
    companion object {
        private val SAMPLE_ANNOTATION_DESC =
            "L" + Register::class.java.name.replace('.', '/') + ";"
        private val SAMPLE_TEST_CASE_DESC =
            "L" + TestCase::class.java.name.replace('.', '/') + ";"
    }

    private var sampleItem: SampleItem? = null

    override fun accept(desc: String, visible: Boolean): Boolean {
        if (SAMPLE_ANNOTATION_DESC == desc) {
            sampleItem = SampleItem()
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

    fun getSampleItem(): SampleItem? {
        return sampleItem
    }
}