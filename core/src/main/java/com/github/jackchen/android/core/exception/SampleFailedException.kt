package com.github.jackchen.android.core.exception

import com.github.jackchen.android.sample.api.SampleItem

/**
 * @author airsaid
 */
class SampleFailedException(
  val sampleItem: SampleItem, cause: Throwable? = null
) : RuntimeException(sampleItem.toString(), cause)