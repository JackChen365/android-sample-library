package com.cz.android.sample.processor.exception;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

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
 * @see AbsActionProcessor#run(androidx.fragment.app.FragmentActivity, com.cz.android.sample.api.item.RegisterItem, java.lang.Object)
 */
public interface ActionExceptionHandler {

    /**
     * handle exception
     */
    void handleException(@NonNull FragmentActivity context,@NonNull Exception e,@NonNull RegisterItem registerItem,@NonNull Object item);
}
