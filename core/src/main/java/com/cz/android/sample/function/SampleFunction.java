package com.cz.android.sample.function;

import android.app.Activity;

import com.cz.android.sample.api.item.RegisterItem;

/**
 * @author Created by cz
 * @date 2020-01-27 16:39
 * @email bingo110@126.com
 */
public interface SampleFunction<C extends Activity> {
    /**
     * When open a new activity. This is the change to setup somethings
     * @param context
     */
    void initialize(C context);
    /**
     * Check if this function is still available. Return true function will run or this function will passed
     * @return
     */
    boolean isAvailable(Class<?> clazz);
    /**
     * run the function
     */
    void run(C context, Object object, RegisterItem item);
}
