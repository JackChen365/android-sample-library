package com.github.jackchen.android.core.util

import com.github.jackchen.android.core.AndroidSample
import com.github.jackchen.android.core.path.PathProvider
import com.github.jackchen.android.sample.api.SampleItem

fun AndroidSample.PathNode.displayTitle(): String {
  val nodeItem = item
  if (nodeItem is SampleItem) {
    return nodeItem.title.replaceFirstChar { it.uppercaseChar() }
  }
  val itemStr = nodeItem.toString()
  val pathMap = PathProvider.getPathMap()
  val pathInfo = pathMap[fullPath]
  return if (pathInfo != null && pathInfo.pathTitle.isNotEmpty()) {
    pathInfo.pathTitle
  } else {
    itemStr.replaceFirstChar { it.uppercaseChar() }
  }
}

fun AndroidSample.PathNode.displayDesc(): String {
  val nodeItem = item
  if (nodeItem is SampleItem) {
    return nodeItem.desc
  }
  val pathMap = PathProvider.getPathMap()
  val pathInfo = pathMap[fullPath]
  return pathInfo?.pathDesc ?: ""
}