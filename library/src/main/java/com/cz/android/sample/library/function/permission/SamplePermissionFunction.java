package com.cz.android.sample.library.function.permission;

import androidx.fragment.app.FragmentActivity;

import com.cz.android.sample.api.Function;
import com.cz.android.sample.api.item.RegisterItem;
import com.cz.android.sample.function.SampleFunction;

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
    public void run(FragmentActivity context, final Object object, RegisterItem item) {
        SamplePermission samplePermission = item.clazz.getAnnotation(SamplePermission.class);
        String[] permissions = samplePermission.value();
        //Here add permission observer
        SamplePermissionsFragment permissionsFragment=SamplePermissionsFragment.get(context);
        PermissionObserver permissionObserver=null;
        if(object instanceof PermissionObserver){
            permissionObserver = (PermissionObserver) object;
        }
        final PermissionObserver observer=permissionObserver;
        PermissionObserver permissionObserverWrapper = new PermissionObserver() {
            @Override
            public void onGranted(Permission permission) {
                if(null!=observer){
                    observer.onGranted(permission);
                }
            }

            @Override
            public void onDenied(Permission permission) {
                if(null!=observer){
                    observer.onDenied(permission);
                }
            }
        };
        //add permission observer
        permissionsFragment.addPermissionObserver(permissions,permissionObserverWrapper);
        //Request permission
        permissionsFragment.requestPermissions(permissions);
    }
}
