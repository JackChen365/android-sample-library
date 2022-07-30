package com.github.jackchen.android.core.initializer

import android.content.Context
import androidx.startup.Initializer
import com.github.jackchen.android.core.AndroidSample
import java.util.*

/**
 * @author JackChen & Airsaid
 */
class AndroidSampleInitializer : Initializer<AndroidSample> {
  override fun create(context: Context): AndroidSample {
    return AndroidSample.instance.apply {
      attachToContext(context)
    }
  }

  override fun dependencies(): MutableList<Class<out Initializer<*>>> {
    return Collections.emptyList()
  }
}
