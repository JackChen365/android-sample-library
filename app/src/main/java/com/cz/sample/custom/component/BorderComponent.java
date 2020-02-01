package com.cz.sample.custom.component;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.cz.android.sample.api.Component;
import com.cz.android.sample.component.ComponentContainer;
import com.cz.android.sample.window.impl.ComponentWindowDelegate;

/**
 * @author Created by cz
 * @date 2020-01-30 13:23
 * @email bingo110@126.com
 */
@Component
public class BorderComponent implements ComponentContainer {

    /**
     * We check if this object has Annotation:SampleBorder.
     * If this sample object doesn't have this annotation. It won't call the other functions
     * @param object
     * @return
     */
    @Override
    public boolean isComponentAvailable(@NonNull Object object) {
        SampleBorder sampleBorder = object.getClass().getAnnotation(SampleBorder.class);
        return null!=sampleBorder&&sampleBorder.value();
    }

    /**
     * This function is an critical function. It's move like a chain. Each component will call this function
     * And return a new view for the next.
     *
     * Tips:
     * 1. If the sample is not a activity or fragment. Take a look on {@link ComponentWindowDelegate}
     *
     * @param context activity context
     * @param object the instance of the sample. It depends on which one that you registered
     * @param parentView The parent view of your original view.
     * @param view your fragment/activity content view
     * @return
     */
    @Override
    public View getComponentView(@NonNull FragmentActivity context, @NonNull Object object, @NonNull ViewGroup parentView, @NonNull View view) {
        BorderLayout borderLayout=new BorderLayout(context);
        borderLayout.addView(view);
        return borderLayout;
    }

    /**
     * After this component created a new view. This function will call automatically.
     * The view is the one you created. You only have this chance to initialize your code here or it will be changed by the other component.
     * @param context
     * @param object
     * @param view
     */
    @Override
    public void onCreatedView(@NonNull FragmentActivity context, @NonNull Object object, @NonNull View view) {
    }

    /**
     * The priority in the component queue. If you want your component run before others
     * @return
     */
    @Override
    public int getComponentPriority() {
        return 0;
    }
}
