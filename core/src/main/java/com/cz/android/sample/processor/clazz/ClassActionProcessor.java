package com.cz.android.sample.processor.clazz;

import android.app.Activity;

import com.cz.android.sample.api.item.RegisterItem;
import com.cz.android.sample.processor.AbsActionProcessor;

/**
 * @author Created by cz
 * @date 2020-01-27 15:08
 * @email bingo110@126.com
 * this com.cz.android.sample.library.processor only process class object
 */
public abstract class ClassActionProcessor<C extends Activity> extends AbsActionProcessor<Class,C> {

    @Override
    public Class getInstance(C context, RegisterItem item, Class clazz) {
        return clazz;
    }

    @Override
    public boolean isInstance(Class item) {
        return item instanceof Class;
    }
}
