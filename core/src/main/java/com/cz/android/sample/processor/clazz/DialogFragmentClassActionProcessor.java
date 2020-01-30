package com.cz.android.sample.processor.clazz;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import com.cz.android.sample.api.item.RegisterItem;

/**
 * @author Created by cz
 * @date 2020-01-27 15:41
 * @email bingo110@126.com
 */
public class DialogFragmentClassActionProcessor extends ClassActionProcessor{
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
