package com.cz.android.sample.window;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

/**
 * @author Created by cz
 * @date 2020-01-28 21:26
 * @email bingo110@126.com
 */
public interface WindowDelegate {

    /**
     * when activity/fragment want to have view
     * @param context
     * @param view
     */
    @NonNull View onCreateView(@NonNull FragmentActivity context,@NonNull Object object,@NonNull ViewGroup container, @NonNull View view);
}
