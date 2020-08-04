package com.cz.android.sample.library.processor;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.cz.android.sample.api.item.RegisterItem;
import com.cz.android.sample.library.appcompat.SampleFragmentContainerActivity;
import com.cz.android.sample.processor.clazz.ClassActionProcessor;

/**
 * @author Created by cz
 * @date 2020-01-28 20:48
 * @email bingo110@126.com
 */
public class FragmentClassActionProcessor extends ClassActionProcessor<FragmentActivity> {

    @Override
    public boolean isInstance(Class item) {
        return super.isInstance(item)&& Fragment.class.isAssignableFrom(item);
    }

    @Override
    public void run(FragmentActivity context, RegisterItem registerItem, Class item) throws Exception {
        SampleFragmentContainerActivity.startActivity(context,registerItem);
    }
}
