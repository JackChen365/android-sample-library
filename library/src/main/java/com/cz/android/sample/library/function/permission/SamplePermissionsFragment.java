package com.cz.android.sample.library.function.permission;

import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import java.util.HashMap;
import java.util.Map;

public class SamplePermissionsFragment extends Fragment {

    private static final int PERMISSIONS_REQUEST_CODE = 42;

    private static final String BIND_SAMPLE_PERMISSION_FRAGMENT_TAG = "cz.sample.permission.bind_fragment_tag";

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

    @TargetApi(Build.VERSION_CODES.M)
    void requestPermissions(@NonNull String[] permissions) {
        requestPermissions(permissions, PERMISSIONS_REQUEST_CODE);
    }

    @TargetApi(Build.VERSION_CODES.M)
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
            boolean granted = grantResults[i] == PackageManager.PERMISSION_GRANTED;
            Permission permission = new Permission(permissions[i], granted, shouldShowRequestPermissionRationale[i]);
            if(granted){
                permissionObserver.onGranted(permission);
            } else {
                permissionObserver.onDenied(permission);
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
