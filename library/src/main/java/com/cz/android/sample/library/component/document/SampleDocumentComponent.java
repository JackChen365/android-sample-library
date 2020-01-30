package com.cz.android.sample.library.component.document;

import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import com.cz.android.sample.component.ComponentContainer;
import com.cz.android.sample.library.R;
import com.cz.android.sample.library.appcompat.SampleWrapperViewFragment;
import com.cz.android.sample.library.adapter.SimpleFragmentPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Created by cz
 * @date 2020-01-28 18:04
 * @email bingo110@126.com
 */
public class SampleDocumentComponent implements ComponentContainer {

    @Override
    public boolean isComponentAvailable(Object object) {
        SampleDocument sampleDocument = object.getClass().getAnnotation(SampleDocument.class);
        return null!=sampleDocument&&null!=sampleDocument.value();
    }

    @Override
    public View getComponentView(FragmentActivity context,Object object,ViewGroup container, View parent) {
        //remove if parent view has toolbar
        checkAndRemoveToolBar(null,parent);

        SampleDocument sampleDocument = object.getClass().getAnnotation(SampleDocument.class);
        FrameLayout contentLayout=new FrameLayout(context);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View documentLayout = layoutInflater.inflate(R.layout.sample_fragment_document_tab, contentLayout, true);
        String url = sampleDocument.value();

        TabLayout sampleTabLayout=documentLayout.findViewById(R.id.sampleTabLayout);
        ViewPager sampleViewPager=documentLayout.findViewById(R.id.sampleViewPager);

        Resources resources = context.getResources();
        CharSequence[] sampleTitles = resources.getStringArray(R.array.sample_title);
        List<Fragment> fragmentList=new ArrayList<>();
        fragmentList.add(SampleWrapperViewFragment.newFragment(parent));
        fragmentList.add(DocumentFragment.newInstance(url));
        sampleViewPager.setAdapter(SimpleFragmentPagerAdapter.create(context.getSupportFragmentManager(), fragmentList, Arrays.asList(sampleTitles)));
        sampleTabLayout.setupWithViewPager(sampleViewPager);
        return contentLayout;
    }

    @Override
    public void onCreatedView(FragmentActivity context,Object object, View view) {
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
}
