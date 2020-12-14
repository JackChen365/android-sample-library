package com.cz.android.sample.library.main;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.cz.android.sample.AndroidSample;
import com.cz.android.sample.function.FunctionManager;
import com.cz.android.sample.main.MainSampleComponentFactory;

/**
 * @author Created by cz
 * @date 2020/7/28 3:46 PM
 * @email bingo110@126.com
 */
public interface AndroidSampleSupport extends AndroidSample<FragmentActivity> {
    String TAG="AndroidSampleSupport";

    FunctionManager getFunctionManager();

    /**
     * Return the component factory.
     * @see MainSampleComponentFactory
     * @return
     */
    MainSampleComponentFactory<Fragment> getComponentContainerFactory();
}
