package com.cz.android.sample.library.function.permission;

/**
 * @author Created by cz
 * @date 2020-01-30 12:41
 * @email bingo110@126.com
 */
public interface PermissionObserver {
    /**
     * when user granted this permission
     * @param permission
     */
    void onGranted(Permission permission);

    /**
     * When user denied you have this permission
     * @param permission
     */
    void onDenied(Permission permission);
}
