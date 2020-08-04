package com.cz.android.sample.library.appcompat.permission;

import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import java.util.HashMap;
import java.util.Map;

public class SamplePermissionsFragment extends Fragment {

    private static final int PERMISSIONS_REQUEST_CODE = 42;

    private static final String BIND_SAMPLE_PERMISSION_FRAGMENT_TAG = "cz.sample.permission.bind_fragment_tag";

    private Runnable pendingRequestRunnable=null;

    public static void injectIfNeededIn(FragmentActivity activity) {
        FragmentManager supportFragmentManager = activity.getSupportFragmentManager();
        if (supportFragmentManager.findFragmentByTag(BIND_SAMPLE_PERMISSION_FRAGMENT_TAG) == null) {
            supportFragmentManager.beginTransaction().add(new SamplePermissionsFragment(), BIND_SAMPLE_PERMISSION_FRAGMENT_TAG).commitNow();
        }
    }

    public static SamplePermissionsFragment get(FragmentActivity activity){
        FragmentManager supportFragmentManager = activity.getSupportFragmentManager();
        Fragment fragment = supportFragmentManager.findFragmentByTag(BIND_SAMPLE_PERMISSION_FRAGMENT_TAG);
        if(null==fragment){
            return null;
        } else {
            return (SamplePermissionsFragment)fragment;
        }
    }

    // Contains all the current permission requests.
    // Once granted or denied, they are removed from it.
    private Map<String, PermissionObserver> permissionObservers = new HashMap<>();

    public SamplePermissionsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(null!=pendingRequestRunnable){
            pendingRequestRunnable.run();
        }
    }

    void requestPermissions(@NonNull final String[] permissions) {
        if(isAdded()){
            requestPermissions(permissions, PERMISSIONS_REQUEST_CODE);
        } else {
            pendingRequestRunnable=new Runnable() {
                @Override
                public void run() {
                    requestPermissions(permissions, PERMISSIONS_REQUEST_CODE);
                }
            };
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != PERMISSIONS_REQUEST_CODE) return;
        boolean[] shouldShowRequestPermissionRationale = new boolean[permissions.length];
        for (int i = 0; i < permissions.length; i++) {
            shouldShowRequestPermissionRationale[i] = shouldShowRequestPermissionRationale(permissions[i]);
        }
        onRequestPermissionsResult(permissions, grantResults, shouldShowRequestPermissionRationale);
    }

    void onRequestPermissionsResult(String permissions[], int[] grantResults, boolean[] shouldShowRequestPermissionRationale) {
        for (int i = 0, size = permissions.length; i < size; i++) {
            // Find the corresponding subject
            PermissionObserver permissionObserver = this.permissionObservers.remove(permissions[i]);
            if (permissionObserver == null) {
                // No subject found
                return;
            }
            boolean isGranted = grantResults[i] == PackageManager.PERMISSION_GRANTED;
            PermissionResult permission = new PermissionResult(permissions[i], isGranted, shouldShowRequestPermissionRationale[i]);
            if(isGranted){
                permissionObserver.onAccepted(permission);
            } else {
                permissionObserver.onAccepted(permission);
            }
        }
    }

    public void addPermissionObserver(String[] permissions, PermissionObserver permissionObserver) {
        for(String permission:permissions){
            if(!permissionObservers.containsKey(permissions)){
                permissionObservers.put(permission,permissionObserver);
            }
        }
    }

}
