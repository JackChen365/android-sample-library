package com.cz.android.sample.library.processor;


import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;

import com.cz.android.sample.api.item.RegisterItem;
import com.cz.android.sample.processor.clazz.ClassActionProcessor;

/**
 * @author Created by cz
 * @date 2020-01-27 15:41
 * @email bingo110@126.com
 */
public class DialogFragmentClassActionProcessor extends ClassActionProcessor<FragmentActivity>{
    @Override
    public boolean isInstance(Class item) {
        return super.isInstance(item)&& DialogFragment.class.isInstance(item);
    }

    @Override
    public void run(FragmentActivity context, RegisterItem registerItem, Class clazz) throws InstantiationException, IllegalAccessException {
        DialogFragment dialog = (DialogFragment) clazz.newInstance();
        dialog.show(context.getSupportFragmentManager(),null);
    }
}
