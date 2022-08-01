package com.github.jackchen.android.core.processor

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.github.jackchen.android.core.exception.SampleFailedException
import com.github.jackchen.android.sample.api.SampleItem

class DialogFragmentClassActionProcessor : ActionProcessor {

  override fun isAvailable(clazz: Class<*>): Boolean {
    return DialogFragment::class.java.isInstance(clazz)
  }

  @Throws(SampleFailedException::class)
  override fun execute(context: AppCompatActivity, sampleItem: SampleItem) {
    val dialog = sampleItem.clazz().getConstructor().newInstance() as DialogFragment
    dialog.show(context.supportFragmentManager, null)
  }
}
