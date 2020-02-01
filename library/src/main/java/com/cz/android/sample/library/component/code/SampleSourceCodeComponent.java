package com.cz.android.sample.library.component.code;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.cz.android.sample.component.CompanionComponentContainer;
import com.cz.android.sample.library.R;
import com.cz.android.sample.library.adapter.SimpleFragmentPagerAdapter;
import com.cz.android.sample.library.appcompat.SampleWrapperViewFragment;
import com.cz.android.sample.library.component.document.SampleDocumentComponent;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Created by cz
 * @date 2020-01-31 11:38
 * @email bingo110@126.com
 */
public class SampleSourceCodeComponent extends CompanionComponentContainer {

    @Override
    public boolean isComponentAvailable(@NonNull Object object) {
        SampleSourceCode sampleSourceCode = object.getClass().getAnnotation(SampleSourceCode.class);
        return null!=sampleSourceCode;
    }

    /**
     * If user want to have his own toolbar. we won't add the standard toolbar for sample
     * @param view
     * @return
     */
    private void checkAndRemoveToolBar(ViewGroup parent,View view){
        if(androidx.appcompat.widget.Toolbar.class==view.getClass()){
            parent.removeView(view);
        } else if(view instanceof ViewGroup){
            ViewGroup viewGroup = (ViewGroup) view;
            for(int i=0;i<viewGroup.getChildCount();i++){
                View childView = viewGroup.getChildAt(i);
                checkAndRemoveToolBar(viewGroup,childView);
            }
        }
    }

    @Override
    public View onCreateCompanionComponent(@NonNull FragmentActivity context, @NonNull Object object, @NonNull ViewGroup parentView, @NonNull View view) {
        //remove if parent view has toolbar
        checkAndRemoveToolBar(null,view);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View contentLayout = layoutInflater.inflate(R.layout.sample_fragment_tab, parentView, false);
        ViewPager sampleViewPager=contentLayout.findViewById(R.id.sampleViewPager);
        List<CharSequence> titleList=new ArrayList<>();
        titleList.add(context.getString(R.string.sample));
        List<Fragment> fragmentList=new ArrayList<>();
        fragmentList.add(SampleWrapperViewFragment.newFragment(view));

        sampleViewPager.setAdapter(SimpleFragmentPagerAdapter.create(context.getSupportFragmentManager(), fragmentList,titleList));
        return contentLayout;
    }

    @Override
    public Class<CompanionComponentContainer>[] getCompanionComponent() {
        return new Class[]{SampleDocumentComponent.class};
    }

    @Override
    public View getComponentView(@NonNull FragmentActivity context, @NonNull Object object, @NonNull ViewGroup parentView, @NonNull View view) {
        ViewPager sampleViewPager=view.findViewById(R.id.sampleViewPager);
        TabLayout sampleTabLayout=view.findViewById(R.id.sampleTabLayout);
        PagerAdapter adapter = sampleViewPager.getAdapter();
        List<Fragment> fragmentList=new ArrayList<>();
        List<CharSequence> titleList=new ArrayList<>();
        if(adapter instanceof FragmentPagerAdapter){
            FragmentPagerAdapter fragmentPagerAdapter = (FragmentPagerAdapter) adapter;
            for(int i=0;i<fragmentPagerAdapter.getCount();i++){
                Fragment fragment = fragmentPagerAdapter.getItem(i);
                fragmentList.add(fragment);
                CharSequence title = fragmentPagerAdapter.getPageTitle(i);
                titleList.add(title);
            }
        }
        //Plus our component
        titleList.add(context.getString(R.string.sample_source_code));
        Class<?> clazz = object.getClass();
        String packageName = clazz.getPackage().getName();
        fragmentList.add(SampleSourceFileFragmentListFragment.newInstance(packageName));
        FragmentPagerAdapter fragmentPagerAdapter = SimpleFragmentPagerAdapter.create(context.getSupportFragmentManager(), fragmentList, titleList);
        sampleViewPager.setAdapter(fragmentPagerAdapter);
        sampleTabLayout.setupWithViewPager(sampleViewPager);
        return view;
    }

    @Override
    public void onCreatedView(@NonNull FragmentActivity context, @NonNull Object object, @NonNull View view) {
        Toolbar sampleToolbar=view.findViewById(R.id.sampleToolBar);
        final AppCompatActivity activity=(AppCompatActivity)context;
        activity.setSupportActionBar(sampleToolbar);
        final ActionBar supportActionBar = activity.getSupportActionBar();
        Intent intent = activity.getIntent();
        supportActionBar.setTitle(intent.getStringExtra("title"));
        supportActionBar.setSubtitle(intent.getStringExtra("desc"));
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        sampleToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.finish();
            }
        });
    }

    @Override
    public int getComponentPriority() {
        return 0;
    }

}
