package com.cz.android.sample.processor.clazz;

import android.app.Dialog;
import android.content.Context;

import androidx.fragment.app.FragmentActivity;

import com.cz.android.sample.api.item.RegisterItem;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Created by cz
 * @date 2020-01-27 15:13
 * @email bingo110@126.com
 */
public class DialogClassActionProcessor extends ClassActionProcessor {
    @Override
    public boolean isInstance(Class item) {
        return super.isInstance(item)&& Dialog.class.isAssignableFrom(item);
    }

    @Override
    public void run(FragmentActivity context, RegisterItem registerItem, Class clazz) throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {
        Constructor<Dialog> constructor = (Constructor<Dialog>) clazz.getConstructor(Context.class);
        Dialog dialog = constructor.newInstance(context);
        dialog.show();
    }
}
