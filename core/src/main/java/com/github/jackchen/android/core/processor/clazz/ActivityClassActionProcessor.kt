package com.github.jackchen.android.core.processor.clazz

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.github.jackchen.android.core.SampleConstants
import com.github.jackchen.android.core.processor.ActionProcessor
import com.github.jackchen.android.sample.api.SampleItem

/**
 * @author JackChen
 */
class ActivityClassActionProcessor : ActionProcessor {

  override fun isAvailable(clazz: Class<*>): Boolean {
    return Activity::class.java.isAssignableFrom(clazz)
  }

  override fun execute(context: AppCompatActivity, sampleItem: SampleItem) {
    Intent(context, sampleItem.clazz()).apply {
      putExtra(SampleConstants.PARAMETER_TITLE, sampleItem.title)
      putExtra(SampleConstants.PARAMETER_DESC, sampleItem.desc)
      context.startActivity(this)
    }
  }
}
