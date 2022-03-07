package com.tubi.android.testapp.extensions.component;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.cz.android.sample.api.Extension;

@Extension
public class BorderComponent implements ComponentContainer {
    @Override
    public boolean isComponentAvailable(@NonNull Object object) {
        SampleBorder sampleBorder = object.getClass().getAnnotation(SampleBorder.class);
        return null!=sampleBorder&&sampleBorder.value();
    }

    @Override
    public View getComponentView(final AppCompatActivity context, final Object object, final ViewGroup parentView,
            final View view,
            final Bundle saveInstance) {
        BorderLayout borderLayout=new BorderLayout(context);
        borderLayout.addView(view);
        return borderLayout;
    }

    @Override public void onCreatedView(final AppCompatActivity context, final Object object, final View view) {
    }
    @Override
    public int getComponentPriority() {
        return 0;
    }
}
