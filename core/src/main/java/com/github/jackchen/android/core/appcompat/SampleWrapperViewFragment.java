package com.github.jackchen.android.core.appcompat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * @author Created by cz
 * @date 2020-01-29 12:53
 * @email bingo110@126.com
 */
public class SampleWrapperViewFragment extends Fragment {

    public static Fragment newFragment(View view) {
        return new SampleWrapperViewFragment(view);
    }

    /**
     * view that need display
     */
    public View view;

    public SampleWrapperViewFragment() {
    }

    public SampleWrapperViewFragment(View view) {
        this.view = view;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return view;
    }
}
