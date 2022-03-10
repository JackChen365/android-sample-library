package com.cz.android.sample.library.function.permission;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import com.cz.android.sample.api.Extension;
import com.cz.android.sample.api.SampleItem;
import com.cz.android.sample.function.SampleFunction;

/**
 * @author Created by cz
 * @date 2020-01-30 12:20
 * @email bingo110@126.com
 *
 * @see PermissionObserver if a sample want to request permission and want to know the result then implement PermissionObserver
 */
@Extension
public class SamplePermissionFunction implements SampleFunction {
    /**
     * If your function wants to do some initial work. Here we inject the fragment.
     * But if we don't we this function, call SamplePermissionsFragment.injectIfNeededIn(context);
     * and then try to get fragment from the FragmentManager It just didn't exist
     * @param context
     */
    @Override public void onInitialize(final AppCompatActivity context) {
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
    public void execute(AppCompatActivity context, final SampleItem item) {
        final Application application = context.getApplication();
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks(){

            @Override
            public void onActivityCreated(@NonNull final Activity activity, @Nullable final Bundle savedInstanceState) {
                application.unregisterActivityLifecycleCallbacks(this);
                FragmentActivity fragmentActivity=(FragmentActivity)activity;
                requestSamplePermission(fragmentActivity,item);
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
        });
    }

    private void requestSamplePermission(@NonNull final FragmentActivity activity, final SampleItem item) {
        final Class<?> clazz = item.clazz();
        SamplePermission samplePermission = clazz.getAnnotation(SamplePermission.class);
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
