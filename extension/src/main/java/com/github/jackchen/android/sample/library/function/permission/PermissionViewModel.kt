package com.github.jackchen.android.sample.library.function.permission

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

/**
 * @author Created by cz
 * @date 2020/7/28 5:02 PM
 * @email bingo110@126.com
 */
class PermissionViewModel : ViewModel() {
    private val permissionLiveData = MutableLiveData<PermissionResult>()

    fun setPermissionResult(permissionResult: PermissionResult) {
        permissionLiveData.value = permissionResult
    }

    @MainThread
    fun addObserver(lifecycleOwner: LifecycleOwner, observer: Observer<PermissionResult>) {
        permissionLiveData.observe(lifecycleOwner, observer)
    }
}