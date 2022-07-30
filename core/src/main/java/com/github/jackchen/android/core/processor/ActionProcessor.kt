package com.github.jackchen.android.core.processor

import androidx.appcompat.app.AppCompatActivity
import com.github.jackchen.android.core.exception.SampleFailedException
import com.github.jackchen.android.sample.api.SampleItem

/**
 * An action processor, When you execute an action.
 * For example try to start an activity. How it work.
 *
 * @author JackChen
 */
interface ActionProcessor {

  /**
   * Consider if you want to process this item.
   * Usually is class object. However It will be an custom object.
   *
   * @param clazz The class object.
   * @return True if you want to process this item.
   */
  fun isAvailable(clazz: Class<*>): Boolean

  /**
   * Process the register configuration.
   *
   * @param context Android context object. Here you is an FragmentActivity.
   * @param sampleItem the one that you register which is all the information you could use.
   * @throws Exception when you execute an action failed throw an exception.
   */
  @Throws(SampleFailedException::class)
  fun execute(context: AppCompatActivity, sampleItem: SampleItem)
}
