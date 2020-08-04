package com.cz.android.sample.library.appcompat;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author Created by cz
 * @date 2020-01-29 12:53
 * @email bingo110@126.com
 */
public class SampleWrapperViewFragment extends Fragment {

    public static Fragment newFragment(View view){
        SampleWrapperViewFragment sampleWrapperViewFragment = new SampleWrapperViewFragment();
        sampleWrapperViewFragment.setContentView(view);
        return sampleWrapperViewFragment;
    }
    /**
     * view that need display
     */
    public View view;

    public SampleWrapperViewFragment() {
    }

    public void setContentView(View view){
        this.view=view;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return view;
    }
}
