package com.cz.android.sample.library.appcompat.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Created by cz
 * @date 2020-01-28 18:15
 * @email bingo110@126.com
 */
public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments=new ArrayList<>();
    private List<CharSequence> titleList;

    public static FragmentPagerAdapter create(@NonNull FragmentManager fm,@NonNull Fragment[] fragmentList){
        return new SimpleFragmentPagerAdapter(fm, Arrays.asList(fragmentList),null);
    }

    public static FragmentPagerAdapter create(@NonNull FragmentManager fm,@NonNull List<Fragment> fragmentList){
        return new SimpleFragmentPagerAdapter(fm,fragmentList,null);
    }

    public static FragmentPagerAdapter create(@NonNull FragmentManager fm,@NonNull List<Fragment> fragmentList,@Nullable List<CharSequence> titleList){
        return new SimpleFragmentPagerAdapter(fm,fragmentList,titleList);
    }

    public SimpleFragmentPagerAdapter(@NonNull FragmentManager fm, List<Fragment> fragments, List<CharSequence> titleList) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.fragments.addAll(fragments);
        if(null!=titleList){
            this.titleList=new ArrayList<>();
            this.titleList.addAll(titleList);
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        CharSequence title=null;
        if(null!= titleList &&position< titleList.size()){
            title = titleList.get(position);
        }
        return title;
    }

    @Override
    public void finishUpdate(@NonNull ViewGroup container) {
        try {
            super.finishUpdate(container);
        } catch (Exception e){
        }
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
