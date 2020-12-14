package com.cz.android.sample.library.main;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.cz.android.sample.function.FunctionManager;
import com.cz.android.sample.function.SampleFunction;

import java.util.List;

/**
 * @author Created by cz
 * @date 2020-01-27 19:47
 * @email bingo110@126.com
 */
public class SampleActivityLifeCycleCallback extends SampleActivityLifeCycleCallbackAdapter {
    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {
        //initialize all functions
        initializeFunction(activity);
    }

    private void initializeFunction(@NonNull Activity activity) {
        AndroidSampleImpl androidSample = AndroidSampleImpl.getInstance();
        FunctionManager functionManager = androidSample.getFunctionManager();
        List<SampleFunction> functionList = functionManager.getFunctionList();
        for(SampleFunction function:functionList){
            if(activity instanceof FragmentActivity){
                FragmentActivity fragmentActivity=(FragmentActivity)activity;
                function.initialize(fragmentActivity);
            }
        }
    }

}
