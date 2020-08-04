package com.cz.android.sample.library.appcompat.permission;

import androidx.annotation.NonNull;

/**
 * @author Created by cz
 * @date 2020-01-30 12:41
 * @email bingo110@126.com
 */
public interface PermissionObserver {
    /**
     * When user denied you have this permission
     * @param permission
     */
    void onAccepted(@NonNull PermissionResult permission);
}
