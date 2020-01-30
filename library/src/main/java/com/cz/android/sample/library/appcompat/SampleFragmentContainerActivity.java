package com.cz.android.sample.library.appcompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.cz.android.sample.api.item.RegisterItem;
import com.cz.android.sample.component.SampleComponentContainer;
import com.cz.android.sample.library.R;
import com.cz.android.sample.window.impl.ComponentWindowDelegate;

/**
 * @author Created by cz
 * @date 2020-01-28 18:00
 * @email bingo110@126.com
 */
public class SampleFragmentContainerActivity extends AppCompatActivity{

    /**
     * start activity for a fragment sample
     */
    public static void startActivity(Context context, RegisterItem registerItem) {
        if (!Fragment.class.isAssignableFrom(registerItem.clazz)) {
            throw new IllegalStateException("SampleActivity only support case that implemented by Fragment!");
        } else {
            Intent intent = new Intent(context, SampleFragmentContainerActivity.class);
            intent.putExtra("title", registerItem.title);
            intent.putExtra("desc", registerItem.desc);
            intent.putExtra("fragment", registerItem.clazz.getName());
            context.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_activity_fragment_container);
        Intent intent = getIntent();
        String className = intent.getStringExtra("fragment");
        Class clazz = null;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
        }
        if(null==clazz){
            throw new IllegalArgumentException("We can't find class:"+className);
        } else {
            try {
                Object instance = clazz.newInstance();
                if(instance instanceof Fragment){
                    Fragment fragment = (Fragment) instance;
                    if(fragment instanceof SampleComponentContainer){
                        //already initialized component
                        FragmentManager supportFragmentManager = getSupportFragmentManager();
                        supportFragmentManager.beginTransaction().add(R.id.sampleActivityFragmentContainer,fragment).commit();
                    } else {
                        //We need to wrap this fragment once
                        FragmentManager supportFragmentManager = getSupportFragmentManager();
                        SampleToolbarWrapperFragment toolbarWrapperFragment = new SampleToolbarWrapperFragment();
                        supportFragmentManager.beginTransaction().add(R.id.sampleActivityFragmentContainer,toolbarWrapperFragment).commit();
                        //then add our fragment;
                        supportFragmentManager.beginTransaction().add(R.id.sampleFragmentContainer,fragment).commit();

                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
    }
}
