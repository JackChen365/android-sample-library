package com.cz.android.sample.library.appcompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.cz.android.sample.library.R;

/**
 * @author Created by cz
 * @date 2020-01-28 20:37
 * @email bingo110@126.com
 * @hide
 */
@Keep
public class SampleToolbarWrapperFragment extends Fragment {

    @Nullable
    @Override
    public final View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sample_toolbar_fragment_container,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View view = getView();
        Toolbar toolBar = view.findViewById(R.id.sampleToolBar);
        //initialize all the information
        final AppCompatActivity activity = (AppCompatActivity)getActivity();
        activity.setSupportActionBar(toolBar);
        ActionBar supportActionBar = activity.getSupportActionBar();
        Intent intent = activity.getIntent();
        supportActionBar.setTitle(intent.getStringExtra("title"));
        supportActionBar.setSubtitle(intent.getStringExtra("desc"));
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.finish();
            }
        });
    }

}
