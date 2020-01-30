package com.cz.android.sample.function;

import androidx.fragment.app.FragmentActivity;

import com.cz.android.sample.api.item.RegisterItem;

/**
 * @author Created by cz
 * @date 2020-01-27 16:39
 * @email bingo110@126.com
 */
public interface SampleFunction {
    /**
     * When open a new activity. This is the change to setup somethings
     * @param context
     */
    void init(FragmentActivity context);
    /**
     * Check if this function is still available. Return true function will run or this function will passed
     * @return
     */
    boolean isAvailable(Class<?> clazz);
    /**
     * run the function
     */
    void run(FragmentActivity context, Object object, RegisterItem item);
}
