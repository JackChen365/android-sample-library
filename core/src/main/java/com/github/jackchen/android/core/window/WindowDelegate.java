package com.github.jackchen.android.core.window;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @author Created by cz
 * @date 2020-01-28 21:26
 * @email bingo110@126.com
 */
public interface WindowDelegate {
    /**
     * when activity/fragment want to have view
     *
     * @param context
     * @param view
     */
    View onCreateView(AppCompatActivity context, Object object, ViewGroup container, View view, Bundle saveInstance);
}
