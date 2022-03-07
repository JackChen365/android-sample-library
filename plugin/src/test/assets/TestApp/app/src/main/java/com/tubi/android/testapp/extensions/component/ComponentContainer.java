package com.tubi.android.testapp.extensions.component;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @author Created by cz
 * @date 2020-01-27 17:36
 * @email bingo110@126.com
 */
public interface ComponentContainer {
    boolean isComponentAvailable(Object object);
    View getComponentView(AppCompatActivity context, Object object, ViewGroup parentView, View view,
            Bundle saveInstance);
    void onCreatedView(AppCompatActivity context, Object object, View view);
    int getComponentPriority();
}
