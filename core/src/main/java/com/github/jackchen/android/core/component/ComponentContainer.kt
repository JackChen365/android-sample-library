package com.github.jackchen.android.core.component

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import android.view.ViewGroup

/**
 * @author Created by cz
 * @date 2020-01-27 17:36
 * @email bingo110@126.com
 */
interface ComponentContainer {
    /**
     * We check if this object has Annotation:SampleBorder.
     * If this sample object doesn't have this annotation. It won't call the other functions
     *
     * @return
     */
    fun isComponentAvailable(component: Any): Boolean

    /**
     * This function is an critical function. It's move like a chain. Each component will call this function
     * And return a new view for the next.
     * Tips:
     * 1. If the sample is not a activity or fragment. Take a look on [AppcompatWindowDelegate]
     *
     * @param context    activity context
     * @param component     the instance of the sample. It depends on which one that you registered
     * @param parentView The parent view of your original view.
     * @param view       your fragment/activity content view
     * @return
     */
    fun getComponentView(context: AppCompatActivity, component: Any, parentView: ViewGroup, view: View): View

    /**
     * After this component created a new view. This function will call automatically.
     * The view is the one you created. You only have this chance to initialize your code here or it will be changed by the other component.
     *
     * @param context
     * @param object
     * @param view
     */
    fun onCreatedView(context: AppCompatActivity, `object`: Any, view: View)

    /**
     * The priority in the component queue. If you want your component run before others
     *
     * @return
     */
    fun getComponentPriority(): Int = 0
}