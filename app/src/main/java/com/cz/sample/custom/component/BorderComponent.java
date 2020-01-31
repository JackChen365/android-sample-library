package com.cz.sample.custom.component;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.cz.android.sample.api.Component;
import com.cz.android.sample.component.ComponentContainer;

/**
 * @author Created by cz
 * @date 2020-01-30 13:23
 * @email bingo110@126.com
 */
@Component
public class BorderComponent implements ComponentContainer {
    @Override
    public boolean isComponentAvailable(@NonNull Object object) {
        SampleBorder sampleBorder = object.getClass().getAnnotation(SampleBorder.class);
        return null!=sampleBorder&&sampleBorder.value();
    }

    @Override
    public View getComponentView(@NonNull FragmentActivity context, @NonNull Object object, @NonNull ViewGroup parentView, @NonNull View view) {
        BorderLayout borderLayout=new BorderLayout(context);
        borderLayout.addView(view);
        return borderLayout;
    }

    @Override
    public void onCreatedView(@NonNull FragmentActivity context, @NonNull Object object, @NonNull View view) {
    }

    @Override
    public int getComponentPriority() {
        return 0;
    }
}
