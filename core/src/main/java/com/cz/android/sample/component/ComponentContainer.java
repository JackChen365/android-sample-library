package com.cz.android.sample.component;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

/**
 * @author Created by cz
 * @date 2020-01-27 17:36
 * @email bingo110@126.com
 */
public interface ComponentContainer {
    /**
     * if there was a condition that make this component available or not
     * @return
     */
    boolean isComponentAvailable(@NonNull Object object);

    /**
     * Get component view by view. If you want to maintain the original view just return it back;
     * @return
     */
    View getComponentView(@NonNull FragmentActivity context,@NonNull Object object,@NonNull ViewGroup parentView,@NonNull View view);

    /**
     * When component is already. This method will call
     * @see ComponentContainer#isComponentAvailable(java.lang.Object)
     * @param context
     * @param view
     */
    void onCreatedView(@NonNull FragmentActivity context,@NonNull Object object,@NonNull View view);

    /**
     * This will put this component to top or bottom in stack
     * @return
     */
    int getComponentPriority();
}
