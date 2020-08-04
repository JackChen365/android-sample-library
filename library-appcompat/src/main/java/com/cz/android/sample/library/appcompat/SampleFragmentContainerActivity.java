package com.cz.android.sample.library.appcompat;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;


import com.cz.android.sample.api.item.RegisterItem;
import com.cz.android.sample.library.R;
import com.cz.android.sample.window.impl.ComponentWindowDelegate;

/**
 * @author Created by cz
 * @date 2020-01-28 18:00
 * @email bingo110@126.com
 */
public class SampleFragmentContainerActivity extends AppCompatActivity {
    private static final String SAMPLE_FRAGMENT_TAG = "sample_fragment_tag";

    private ComponentWindowDelegate windowDelegate=new ComponentWindowDelegate();

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
    }

    @Override
    public void setContentView(int layoutRes) {
        Fragment fragment = getSampleFragment();
        LinearLayout contentView = new LinearLayout(this);
        contentView.setOrientation(LinearLayout.VERTICAL);
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(layoutRes, contentView, false);
        setContentViewInternal(fragment,contentView,view);
    }

    @Override
    public void setContentView(View view) {
        Fragment fragment = getSampleFragment();
        LinearLayout contentView = new LinearLayout(this);
        contentView.setOrientation(LinearLayout.VERTICAL);
        setContentViewInternal(fragment,contentView,view);
    }

    private void setContentViewInternal(final Fragment fragment, ViewGroup contentView, View view){
        View createView = windowDelegate.onCreateView(this,fragment, contentView, view);
        if(!hasToolBar(createView)){
            Toolbar toolBar = new Toolbar(new ContextThemeWrapper(this, R.style.AppTheme_AppBarOverlay));
            //set toolbar background color.
            TypedArray a = obtainStyledAttributes(new int[]{R.attr.colorPrimary});
            int colorPrimary = a.getColor(0, Color.GRAY);
            toolBar.setBackgroundColor(colorPrimary);
            a.recycle();

            //set toolbar elevation
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Resources resources = getResources();
                toolBar.setElevation(resources.getDimension(R.dimen.sample_toolbar_elevation));
            }
            //This Activity already has an action bar supplied by the window decor. Do not request Window.FEATURE_SUPPORT_ACTION_BAR and set windowActionBar to false in your theme to use a Toolbar instead.
            AppCompatDelegate delegate = getDelegate();
            if(!delegate.hasWindowFeature(Window.FEATURE_NO_TITLE)){
                delegate.requestWindowFeature(Window.FEATURE_NO_TITLE);
            }
            //initialize all the information
            setSupportActionBar(toolBar);
            ActionBar supportActionBar = getSupportActionBar();
            Intent intent = getIntent();
            supportActionBar.setTitle(intent.getStringExtra("title"));
            supportActionBar.setSubtitle(intent.getStringExtra("desc"));
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            toolBar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
            contentView.addView(toolBar, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        //add children view to content view
        contentView.addView(createView,ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        super.setContentView(contentView);

        //then add our fragment;
        //Now the view is on fragment. actually fragment manager can't find this view by id. So we postpone this process.
        view.post(new Runnable() {
            @Override
            public void run() {
                FragmentManager supportFragmentManager = getSupportFragmentManager();
                supportFragmentManager.beginTransaction().add(R.id.sampleActivityFragmentContainer,fragment,SAMPLE_FRAGMENT_TAG).commit();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * Return fragment from class string
     * @return
     */
    @NonNull
    private Fragment getSampleFragment() {
        Fragment fragment=null;
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
                fragment = (Fragment) instance;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
        return fragment;
    }


    /**
     * If user want to have his own toolbar. we won't add the standard toolbar for sample
     * @param view
     * @return
     */
    private Boolean hasToolBar(View view){
        if(Toolbar.class==view.getClass()){
            return true;
        } else if(view instanceof ViewGroup){
            ViewGroup viewGroup = (ViewGroup) view;
            for(int i=0;i<viewGroup.getChildCount();i++){
                View childView = viewGroup.getChildAt(i);
                return hasToolBar(childView);
            }
        }
        return false;
    }
}
