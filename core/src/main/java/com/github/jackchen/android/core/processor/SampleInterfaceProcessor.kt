package com.github.jackchen.android.core.processor

import androidx.appcompat.app.AppCompatActivity
import com.github.jackchen.android.core.exception.SampleFailedException
import com.github.jackchen.android.core.main.sample.SampleInterface
import com.github.jackchen.android.sample.api.SampleItem

/**
 * @author JackChen
 */
abstract class SampleInterfaceProcessor<T> : ActionProcessor {

  override fun isAvailable(clazz: Class<*>): Boolean {
    return SampleInterface::class.java.isAssignableFrom(clazz)
  }

  @Suppress("UNCHECKED_CAST")
  override fun execute(context: AppCompatActivity, sampleItem: SampleItem) {
    try {
      val clazz = sampleItem.clazz() as Class<SampleInterface<T>>
      val sampleInterface: SampleInterface<T> = clazz.newInstance()
      val obj: T = sampleInterface.getObject(context)
      execute(context, obj)
    } catch (e: Exception) {
      e.printStackTrace()
      throw SampleFailedException(sampleItem, e)
    }
  }

  @Throws(SampleFailedException::class)
  abstract fun execute(context: AppCompatActivity, obj: T)
}
