package com.cz.android.sample.library.permission;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

/**
 * @author Created by cz
 * @date 2020/7/28 5:02 PM
 * @email bingo110@126.com
 */
public class PermissionViewModelProviders{

    public static PermissionViewModel getViewModel(FragmentActivity activity){
        return ViewModelProviders.of(activity).get(PermissionViewModel.class);
    }

    /**
     * We will only use the activity's viewModelStore to store the ViewModel.
     * If we use the different lifecycleOwner with different ViewModelStore.
     * The fragment will never receive the event.
     * @param fragment
     * @return
     */
    public static PermissionViewModel getViewModel(Fragment fragment){
        FragmentActivity activity = fragment.getActivity();
        return ViewModelProviders.of(activity).get(PermissionViewModel.class);
    }

}
