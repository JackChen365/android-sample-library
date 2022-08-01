package com.github.jackchen.android.core.main

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.jackchen.android.core.AndroidSample

/**
 * @author Created by cz
 * @date 2020-01-27 19:47
 * @email bingo110@126.com
 */
class SampleActivityLifeCycleCallback : ActivityLifecycleCallbacks {
  override fun onActivityCreated(activity: Activity, bundle: Bundle?) {
    // initialize all functions
    val functionManager = AndroidSample.instance.getFunctionManager()
    val functionList = functionManager.getFunctionList()
    for (function in functionList) {
      if (activity is AppCompatActivity) {
        function.onInitialize(activity)
      }
    }
  }

  override fun onActivityStarted(activity: Activity) {}
  override fun onActivityResumed(activity: Activity) {}
  override fun onActivityPaused(activity: Activity) {}
  override fun onActivityStopped(activity: Activity) {}
  override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
  override fun onActivityDestroyed(activity: Activity) {}
}
