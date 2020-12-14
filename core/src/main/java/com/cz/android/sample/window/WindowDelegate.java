package com.cz.android.sample.window;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author Created by cz
 * @date 2020-01-28 21:26
 * @email bingo110@126.com
 */
public interface WindowDelegate<C extends Activity> {

    /**
     * when activity/fragment want to have view
     * @param context
     * @param view
     */
    View onCreateView(C context, Object object, ViewGroup container, View view, Bundle saveInstance);
}
