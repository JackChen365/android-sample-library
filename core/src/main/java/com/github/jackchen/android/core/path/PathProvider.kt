package com.github.jackchen.android.core.path

import com.github.jackchen.android.sample.api.SampleItem

/**
 * @author airsaid
 */
object PathProvider {

  private val mPathMap = hashMapOf<String, PathInfo>()

  fun init(samples: List<SampleItem>) {
    samples.forEach { item ->
      val oldPath = mPathMap[item.path]
      val newPath = PathInfo(item.path, item.pathTitle, item.pathDesc)
      if (oldPath != newPath) {
        mPathMap[item.path] = newPath
      }
    }
  }

  fun getPathMap(): HashMap<String, PathInfo> = mPathMap

}
