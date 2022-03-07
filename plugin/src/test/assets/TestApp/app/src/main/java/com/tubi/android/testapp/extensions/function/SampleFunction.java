package com.tubi.android.testapp.extensions.function;

import androidx.appcompat.app.AppCompatActivity;
import com.cz.android.sample.api.SampleItem;

public interface SampleFunction {
    void onInitialize(AppCompatActivity context);
    boolean isAvailable(Class<?> clazz);
    void execute(AppCompatActivity context, SampleItem item);
}
