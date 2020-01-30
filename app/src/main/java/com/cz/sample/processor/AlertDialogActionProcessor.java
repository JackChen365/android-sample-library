package com.cz.sample.processor;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;

import com.cz.android.sample.api.ActionProcessor;
import com.cz.android.sample.api.item.RegisterItem;
import com.cz.android.sample.library.sample.SampleObject;
import com.cz.android.sample.processor.AbsActionProcessor;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Created by cz
 * @date 2020-01-29 22:02
 * @email bingo110@126.com
 */
@ActionProcessor
public class AlertDialogActionProcessor extends AbsActionProcessor<AlertDialog> {
    @Override
    public boolean isInstance(Class clazz) {
        return SampleObject.class.isAssignableFrom(clazz);
    }

    @Override
    public AlertDialog getInstance(FragmentActivity context, RegisterItem item, Class clazz) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        SampleObject object;
        try{
            object= (SampleObject) clazz.newInstance();
        } catch (InstantiationException e){
            Class outerClass = clazz.getEnclosingClass();
            Constructor<SampleObject> constructor = clazz.getDeclaredConstructor(outerClass);
            Object outer = outerClass.newInstance();
            object= constructor.newInstance(outer);
        }
        return (AlertDialog) object.getObject(context);
    }

    @Override
    public void run(FragmentActivity context, RegisterItem registerItem, AlertDialog item) {
        item.show();
    }
}
