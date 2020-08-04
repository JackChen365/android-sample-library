package com.cz.android.sample.library.appcompat.permission;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.cz.android.sample.api.Function;
import com.cz.android.sample.api.item.RegisterItem;
import com.cz.android.sample.function.SampleFunction;
import com.cz.android.sample.library.main.AndroidSampleSupport;
import com.cz.android.sample.library.main.SampleActivityLifeCycleCallbackAdapter;

/**
 * @author Created by cz
 * @date 2020-01-30 12:20
 * @email bingo110@126.com
 *
 * @see PermissionObserver if a sample want to request permission and want to know the result then implement PermissionObserver
 */
@Function
public class SamplePermissionFunction implements SampleFunction {
    /**
     * If your function wants to do some initial work. Here we inject the fragment.
     * But if we don't we this function, call SamplePermissionsFragment.injectIfNeededIn(context);
     * and then try to get fragment from the FragmentManager It just didn't exist
     * @param context
     */
    @Override
    public void init(FragmentActivity context) {
        //inject permission fragment
        SamplePermissionsFragment.injectIfNeededIn(context);
    }

    /**
     * Check this class and determined this class needs to run this function
     * @param clazz
     * @return
     */
    @Override
    public boolean isAvailable(Class<?> clazz) {
        SamplePermission samplePermission = clazz.getAnnotation(SamplePermission.class);
        return (null!=samplePermission&&
                null!=samplePermission.value()&&
                0 < samplePermission.value().length);
    }

    @Override
    public void run(FragmentActivity context, final Object object, final RegisterItem item) {
        final Application application = context.getApplication();
        application.registerActivityLifecycleCallbacks(new SampleActivityLifeCycleCallbackAdapter(){
            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
                super.onActivityCreated(activity, savedInstanceState);
                application.unregisterActivityLifecycleCallbacks(this);
                if(!(activity instanceof FragmentActivity)){
                    Log.w(AndroidSampleSupport.TAG,"The sample activity is not an FragmentActivity. I just do not know what kind of things do you want to do. " +
                            "And for this reason. The permission function will not work properly.");
                } else {
                    FragmentActivity fragmentActivity=(FragmentActivity)activity;
                    requestSamplePermission(fragmentActivity,item);
                }
            }
        });
    }

    private void requestSamplePermission(@NonNull final FragmentActivity activity, final RegisterItem item) {
        SamplePermission samplePermission = item.clazz.getAnnotation(SamplePermission.class);
        final String[] permissions = samplePermission.value();
        //Here add permission observer
        final SamplePermissionsFragment permissionsFragment=SamplePermissionsFragment.get(activity);
        PermissionObserver permissionObserverWrapper = new PermissionObserver() {
            @Override
            public void onAccepted(PermissionResult permission) {
                PermissionViewModel viewModel = PermissionViewModelProviders.getViewModel(activity);
                PermissionObserver observer = viewModel.getObserver();
                if(null!=observer){
                    observer.onAccepted(permission);
                }
            }
        };
        //add permission observer
        permissionsFragment.addPermissionObserver(permissions,permissionObserverWrapper);
        //Request permission
        permissionsFragment.requestPermissions(permissions);
    }
}
