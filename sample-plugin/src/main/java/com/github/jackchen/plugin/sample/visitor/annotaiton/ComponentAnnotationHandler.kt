package com.github.jackchen.plugin.sample.visitor.annotaiton

import com.github.jackchen.android.sample.api.Register
import com.github.jackchen.android.sample.api.SampleItem
import com.github.jackchen.plugin.sample.visitor.SampleClassVisitor

class ComponentAnnotationHandler : AnnotationHandler() {
  companion object {
    private val SAMPLE_ANNOTATION_DESC =
      "L" + Register::class.java.name.replace('.', '/') + ";"
  }
  var sampleItem: SampleItem? = null

  override fun accept(classVisitor: SampleClassVisitor, desc: String, visible: Boolean): Boolean {
    if (SAMPLE_ANNOTATION_DESC == desc) {
      sampleItem = SampleItem()
      whenFoundClass?.invoke(classVisitor)
      return true
    }
    return false
  }

  override fun visit(name: String, value: Any?) {
    when (name) {
      "title" -> sampleItem?.title = value.toString()
      "desc" -> sampleItem?.desc = value.toString()
      "path" -> sampleItem?.path = value.toString()
      "pathTitle" -> sampleItem?.pathTitle = value.toString()
      "pathDesc" -> sampleItem?.pathDesc = value.toString()
    }
  }
}
