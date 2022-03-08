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
class SampleTestCaseHandler : AnnotationHandler {
    companion object {
        private val SAMPLE_TEST_CASE_DESC =
            "L" + TestCase::class.java.name.replace('.', '/') + ";"
    }

    var isTestCase: Boolean = false

    override fun accept(desc: String, visible: Boolean): Boolean {
        if (SAMPLE_TEST_CASE_DESC == desc) {
            isTestCase = true
            return true
        }
        return false
    }

    override fun visit(name: String, value: Any?) {
    }

    override fun visitEnd() {
        isTestCase = false
    }
}