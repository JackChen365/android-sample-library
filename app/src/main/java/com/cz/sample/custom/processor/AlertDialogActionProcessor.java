package com.cz.sample.custom.processor;

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
public class AlertDialogActionProcessor extends AbsActionProcessor<AlertDialog,FragmentActivity> {
    @Override
    public boolean isInstance(Class clazz) {
        return SampleObject.class.isAssignableFrom(clazz);
    }

    @Override
    public AlertDialog getInstance(FragmentActivity context, RegisterItem item, Class clazz) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        SampleObject sampleObject;
        try{
            sampleObject= (SampleObject) clazz.newInstance();
        } catch (InstantiationException e){
            Class outerClass = clazz.getEnclosingClass();
            Constructor<SampleObject> constructor = clazz.getDeclaredConstructor(outerClass);
            Object outer = outerClass.newInstance();
            sampleObject= constructor.newInstance(outer);
        }
        Object object = sampleObject.getObject(context);
        if(null!=object&&object instanceof AlertDialog){
            return (AlertDialog)object;
        }
        return null;
    }

    @Override
    public void run(FragmentActivity context, RegisterItem registerItem, AlertDialog item) {
        item.show();
    }
}
