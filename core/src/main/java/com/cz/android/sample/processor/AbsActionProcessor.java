package com.cz.android.sample.processor;

import androidx.fragment.app.FragmentActivity;
import com.cz.android.sample.api.item.RegisterItem;

/**
 * @author Created by cz
 * @date 2020-01-27 15:02
 * @email bingo110@126.com
 *
 * An action com.cz.android.sample.library.processor, When you execute an action. For example try to start an activity. How it work.
 */
public abstract class AbsActionProcessor<T> {
    /**
     * consider if you want to process this item. usually is class object. HowEver It will be an custom object.
     */
    public abstract boolean isInstance(Class clazz);

    /**
     * Get object instance from class object
     * @param context
     * @param clazz
     * @return
     */
    public abstract T getInstance(FragmentActivity context,RegisterItem item,Class clazz) throws Exception;

    /**
     * process the register configuration
     * @param context Android context object. Here you is an FragmentActivity.
     * @param registerItem the one that you register which is all the information you could use
     * @throws Exception when you execute an action failed throw an exception
     */
    public abstract void run(FragmentActivity context, RegisterItem registerItem,T item) throws Exception;
}
