package com.cz.android.sample.processor.exception;

import android.app.Activity;

import com.cz.android.sample.api.item.RegisterItem;
import com.cz.android.sample.processor.AbsActionProcessor;

/**
 * @author Created by cz
 * @date 2020-01-27 15:16
 * @email bingo110@126.com
 *
 * When an register action open failed. It will throw an exception.
 * You could implement this exceptionHandler to do something.
 *
 * Here this process will throw Exception.
 * @see AbsActionProcessor#run(Activity, RegisterItem, Object)
 */
public interface ActionExceptionHandler {
    /**
     * handle exception
     */
    void handleException(Activity context,Exception e, RegisterItem registerItem, Object item);
}
