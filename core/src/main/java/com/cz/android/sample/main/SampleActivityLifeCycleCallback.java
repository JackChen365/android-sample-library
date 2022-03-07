package com.cz.android.sample.main;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.cz.android.sample.AndroidSample;
import com.cz.android.sample.function.FunctionManager;
import com.cz.android.sample.function.SampleFunction;
import java.util.List;

/**
 * @author Created by cz
 * @date 2020-01-27 19:47
 * @email bingo110@126.com
 */
public class SampleActivityLifeCycleCallback implements Application.ActivityLifecycleCallbacks {
    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {
        //initialize all functions
        AndroidSample androidSample = AndroidSample.getInstance();
        FunctionManager functionManager = androidSample.getFunctionManager();
        List<SampleFunction> functionList = functionManager.getFunctionList();
        for(SampleFunction function:functionList){
            if(activity instanceof AppCompatActivity){
                AppCompatActivity appCompatActivity=(AppCompatActivity)activity;
                function.onInitialize(appCompatActivity);
            }
        }
    }


    @Override public void onActivityStarted(@NonNull final Activity activity) {
    }

    @Override public void onActivityResumed(@NonNull final Activity activity) {
    }

    @Override public void onActivityPaused(@NonNull final Activity activity) {
    }

    @Override public void onActivityStopped(@NonNull final Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull final Activity activity, @NonNull final Bundle outState) {
    }

    @Override public void onActivityDestroyed(@NonNull final Activity activity) {
    }

}
