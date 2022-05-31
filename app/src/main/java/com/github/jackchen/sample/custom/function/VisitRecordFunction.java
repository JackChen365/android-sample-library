package com.github.jackchen.sample.custom.function;

import android.util.Log;
import androidx.annotation.Keep;
import androidx.appcompat.app.AppCompatActivity;
import com.github.jackchen.android.sample.api.Extension;
import com.github.jackchen.android.sample.api.SampleItem;
import com.github.jackchen.android.core.function.SampleFunction;

/**
 * @author Created by cz
 * @date 2020-01-30 13:14
 * @email bingo110@126.com
 */
@Keep
@Extension
public class VisitRecordFunction implements SampleFunction {
    private static final String TAG = "VisitRecordFunction";

    @Override public void onInitialize(final AppCompatActivity context) {
    }

    @Override
    public boolean isAvailable(Class<?> clazz) {
        return true;
    }

    @Override
    public void execute(AppCompatActivity context, SampleItem item) {
        Log.e(TAG, "Title:" + item.title + " class:" + item.className);
    }
}
