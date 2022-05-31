package com.github.jackchen.android.core.processor;

import androidx.appcompat.app.AppCompatActivity;
import com.github.jackchen.android.core.exception.SampleFailedException;
import com.github.jackchen.android.sample.api.SampleItem;

/**
 * @author Created by cz
 * @date 2020-01-27 15:02
 * @email bingo110@126.com
 * An action processor, When you execute an action. For example try to start an activity. How it work.
 */
public abstract class ActionProcessor {
    /**
     * consider if you want to process this item. usually is class object. HowEver It will be an custom object.
     */
    public abstract boolean isAvailable(Class clazz);

    /**
     * process the register configuration
     *
     * @param context    Android context object. Here you is an FragmentActivity.
     * @param sampleItem the one that you register which is all the information you could use
     * @throws Exception when you execute an action failed throw an exception
     */
    public abstract void execute(AppCompatActivity context, SampleItem sampleItem) throws SampleFailedException;
}
