package com.cz.android.sample.library.main;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.cz.android.sample.function.FunctionManager;
import com.cz.android.sample.function.SampleFunction;
import com.cz.android.sample.library.R;
import com.cz.android.sample.main.MainSampleComponentFactory;

import java.util.List;

/**
 * @author Created by cz
 * @date 2020-01-27 19:47
 * @email bingo110@126.com
 */
public class SampleActivityLifeCycleCallback implements Application.ActivityLifecycleCallbacks {
    private static final String BIND_MAIN_SAMPLE_FRAGMENT_TAG="android_sample_main_fragment";
    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {
        //initialize all functions
        initializeFunction(activity);
        //check and inject main component
        injectMainComponent(activity);
    }

    /**
     * Check this activity if is main activity we will inject our fragment
     * @param activity
     */
    private void injectMainComponent(@NonNull Activity activity) {
        String launchActivityName = getLaunchActivityName(activity);
        if(launchActivityName.equals(activity.getClass().getName())){
            //Here we are the main activity
            if(!(activity instanceof AppCompatActivity)){
                throw new IllegalArgumentException("The main activity should extend from AppCompatActivity! We can't support the Activity!");
            } else {
                AppCompatActivity appCompatActivity = (AppCompatActivity) activity;
                // Hide ActionBar
//                Resources.Theme theme = appCompatActivity.getTheme();
//                theme.applyStyle(R.style.SampleAppCompat,true);
                //Add the main fragment if needed;
                FragmentManager supportFragmentManager = appCompatActivity.getSupportFragmentManager();
                if (supportFragmentManager.findFragmentByTag(BIND_MAIN_SAMPLE_FRAGMENT_TAG) == null) {
                    AndroidSampleImpl androidSample = AndroidSampleImpl.getInstance();
                    MainSampleComponentFactory componentContainer = androidSample.getMainComponentContainer();
                    Fragment fragment = componentContainer.getFragmentComponent();
                    supportFragmentManager.beginTransaction().add(android.R.id.content,fragment, BIND_MAIN_SAMPLE_FRAGMENT_TAG).commit();
                }
            }
        }
    }

    /**
     * Get android.intent.action.MAIN activity class name
     * @param context
     * @return
     */
    private String getLaunchActivityName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(context.getPackageName());
        return intent.getComponent().getClassName();
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        checkMainComponent(activity);
    }

    private void initializeFunction(@NonNull Activity activity) {
        AndroidSampleImpl androidSample = AndroidSampleImpl.getInstance();
        FunctionManager functionManager = androidSample.getFunctionManager();
        List<SampleFunction> functionList = functionManager.getFunctionList();
        for(SampleFunction function:functionList){
            if(activity instanceof FragmentActivity){
                FragmentActivity fragmentActivity=(FragmentActivity)activity;
                function.init(fragmentActivity);
            }
        }
    }

    /**
     * check main activity if user has its own layout we should remove it
     * @param activity
     */
    private void checkMainComponent(@NonNull Activity activity) {
        String launchActivityName = getLaunchActivityName(activity);
        if(launchActivityName.equals(activity.getClass().getName())) {
            //Here we are the main activity
            if (!(activity instanceof AppCompatActivity)) {
                throw new IllegalArgumentException("The main activity should extend from AppCompatActivity! We can't support the Activity!");
            } else {
                ViewGroup contentLayout=activity.findViewById(android.R.id.content);
                ViewGroup sampleFragmentContainer=contentLayout.findViewById(R.id.sampleFragmentContainer);
                if(null==sampleFragmentContainer){
                    contentLayout.removeAllViews();
                } else {
                    //remove all the children from content view except my boy
                    int keepSize = 0;
                    while(keepSize<contentLayout.getChildCount()){
                        View childView = contentLayout.getChildAt(keepSize);
                        if(childView==sampleFragmentContainer){
                            keepSize++;
                        } else {
                            contentLayout.removeView(childView);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {
    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
    }
}
