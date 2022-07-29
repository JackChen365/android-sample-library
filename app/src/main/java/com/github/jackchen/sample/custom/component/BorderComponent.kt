package com.github.jackchen.sample.custom.component

import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.github.jackchen.android.core.component.ComponentContainer
import com.github.jackchen.android.sample.api.Extension

/**
 * @author Created by cz
 * @date 2020-01-30 13:23
 * @email bingo110@126.com
 */
@Extension
@Suppress("unused")
class BorderComponent : ComponentContainer {
  /**
   * We check if this object has Annotation:SampleBorder.
   * If this sample object doesn't have this annotation. It won't call the other functions
   * @param component
   * @return
   */
  override fun isComponentAvailable(component: Any): Boolean {
    val sampleBorder = component.javaClass.getAnnotation(SampleBorder::class.java)
    return null != sampleBorder && sampleBorder.value
  }

  /**
   * This function is an critical function. It's move like a chain. Each component will call this function
   * And return a new view for the next.
   * Tips:
   * 1. If the sample is not a activity or fragment. Take a look on [com.github.jackchen.android.core.window.AppcompatWindowDelegate]
   *
   * @param context    activity context
   * @param component     the instance of the sample. It depends on which one that you registered
   * @param parentView The parent view of your original view.
   * @param view       your fragment/activity content view
   * @return
   */
  override fun getComponentView(
    context: AppCompatActivity,
    component: Any,
    parentView: ViewGroup,
    view: View
  ): View {
    val borderLayout = BorderLayout(context)
    borderLayout.addView(view)
    return borderLayout
  }

  /**
   * After this component created a new view. This function will call automatically.
   * The view is the one you created. You only have this chance to initialize your code here or it will be changed by the other component.
   *
   * @param context
   * @param object
   * @param view
   */
  override fun onCreatedView(context: AppCompatActivity, `object`: Any, view: View) {}
}
