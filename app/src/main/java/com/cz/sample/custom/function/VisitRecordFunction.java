package com.cz.sample.custom.function;

import android.util.Log;

import androidx.annotation.Keep;
import androidx.fragment.app.FragmentActivity;

import com.cz.android.sample.api.Function;
import com.cz.android.sample.api.item.RegisterItem;
import com.cz.android.sample.function.SampleFunction;

/**
 * @author Created by cz
 * @date 2020-01-30 13:14
 * @email bingo110@126.com
 */
@Keep
@Function
public class VisitRecordFunction implements SampleFunction {
    private static final String TAG="VisitRecordFunction";

    @Override
    public void init(FragmentActivity context) {
    }

    @Override
    public boolean isAvailable(Class<?> clazz) {
        return true;
    }

    @Override
    public void run(FragmentActivity context, Object object, RegisterItem item) {
        Log.e(TAG,"Title:"+item.getTitle()+" class:"+object.toString());
    }
}
