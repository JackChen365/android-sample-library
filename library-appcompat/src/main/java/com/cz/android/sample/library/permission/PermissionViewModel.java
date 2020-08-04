package com.cz.android.sample.library.permission;


import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.MainThread;

/**
 * @author Created by cz
 * @date 2020/7/28 5:02 PM
 * @email bingo110@126.com
 */
public class PermissionViewModel extends ViewModel {

    private final MutableLiveData<PermissionObserver> observerMap=new MutableLiveData<>();

    @MainThread
    PermissionObserver getObserver(){
        return observerMap.getValue();
    }

    @MainThread
    public void addObserver(PermissionObserver observer){
        observerMap.setValue(observer);
    }
}
