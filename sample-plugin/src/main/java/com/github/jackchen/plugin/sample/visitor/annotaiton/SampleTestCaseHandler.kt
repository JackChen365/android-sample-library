package com.github.jackchen.plugin.sample.visitor.annotaiton;

class SampleTestCaseHandler : AnnotationHandler {
    companion object {
        private val SAMPLE_TEST_CASE_DESC =
            "L" + com.github.jackchen.android.sample.api.TestCase::class.java.name.replace('.', '/') + ";"
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