package com.cz.android.sample.library.permission;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

/**
 * @author Created by cz
 * @date 2020/7/28 5:02 PM
 * @email bingo110@126.com
 */
public class PermissionViewModelProviders{

    public static PermissionViewModel getViewModel(FragmentActivity activity){
        ViewModelProvider.Factory newInstanceFactory = new ViewModelProvider.NewInstanceFactory();
        ViewModelProvider viewModelProvider = new ViewModelProvider(activity.getViewModelStore(), newInstanceFactory);
        return viewModelProvider.get(PermissionViewModel.class);
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
        ViewModelProvider.Factory newInstanceFactory = new ViewModelProvider.NewInstanceFactory();
        ViewModelProvider viewModelProvider = new ViewModelProvider(activity.getViewModelStore(), newInstanceFactory);
        return viewModelProvider.get(PermissionViewModel.class);
    }

}
