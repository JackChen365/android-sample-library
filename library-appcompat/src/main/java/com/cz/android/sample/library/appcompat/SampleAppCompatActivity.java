package com.cz.android.sample.library.appcompat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
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

import com.cz.android.sample.AndroidSample;
import com.cz.android.sample.component.SampleComponentContainer;
import com.cz.android.sample.library.R;
import com.cz.android.sample.library.main.AndroidSampleSupport;
import com.cz.android.sample.library.main.SampleApplication;
import com.cz.android.sample.main.MainSampleComponentFactory;
import com.cz.android.sample.window.impl.ComponentWindowDelegate;

/**
 * @author Created by cz
 * @date 2020-01-28 14:05
 * @email bingo110@126.com
 */
public class SampleAppCompatActivity extends AppCompatActivity implements SampleComponentContainer {
    private static final String BIND_MAIN_SAMPLE_FRAGMENT_TAG="android_sample_main_fragment";
    private static final String ANDROID_SUPPORT_FRAGMENTS="android:support:fragments";
    private ComponentWindowDelegate windowDelegate;
    /**
     * This is user's original view. However We may change it. or put this view input a fragment
     * It will causes some problems:
     * <pre>
     * override fun onCreate(savedInstanceState: Bundle?) {
     *     super.onCreate(savedInstanceState)
     *     setContentView(R.layout.activity_component_document_sample)
     *     // Here we may change this content view. and put content view into fragment.
     *     // Then If you try to findViewById It doesn't existed
     *     testButton.setOnClickListener {
     *     }
     * }
     * </pre>
     * So we keep this view. If findViewById can't find the view. we try to find view from it
     */
    private View contentView=null;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Because if we restart this activity. It will re-create the fragment by FragmentManagerState
        outState.remove(ANDROID_SUPPORT_FRAGMENTS);
    }

    @Override
    public void setContentView(int layout) {
        String launchActivityName = getLaunchActivityName(this);
        if(launchActivityName.equals(getClass().getName())){
//            super.setContentView(layout);
            injectLaunchComponent(this);
        } else {
            LinearLayout contentView = new LinearLayout(this);
            contentView.setOrientation(LinearLayout.VERTICAL);
            LayoutInflater layoutInflater = getLayoutInflater();
            View view = layoutInflater.inflate(layout, contentView, false);
            setContentViewInternal(contentView,view);
        }
    }

    @Override
    public void setContentView(View view) {
        String launchActivityName = getLaunchActivityName(this);
        if(launchActivityName.equals(getClass().getName())){
//           super.setContentView(view);
            injectLaunchComponent(this);
        } else {
            LinearLayout contentView = new LinearLayout(this);
            contentView.setOrientation(LinearLayout.VERTICAL);
            setContentViewInternal(contentView,view);
        }
    }



    private void setContentViewInternal(ViewGroup contentView,View view){
        this.contentView=view;
        if(null==windowDelegate){
            windowDelegate=new ComponentWindowDelegate();
        }
        if(hasToolBar(view)){
            View createView = windowDelegate.onCreateView(this,this,contentView, view,null);
            super.setContentView(createView);
        } else {
            View createView = windowDelegate.onCreateView(this,this, contentView, view,null);
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

//                Caused by: java.lang.IllegalStateException: This Activity already has an action bar supplied by the window decor. Do not request Window.FEATURE_SUPPORT_ACTION_BAR and set windowActionBar to false in your theme to use a Toolbar instead.
//                at androidx.appcompat.app.AppCompatDelegateImpl.setSupportActionBar(AppCompatDelegateImpl.java:421)
                //For this problem. I use this solution.

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
        }
    }

    @Override
    public <T extends View> T findViewById(int id) {
        T v=super.findViewById(id);
        if(null==v){
            v=contentView.findViewById(id);
        }
        return v;
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

    /**
     * Check this activity if is main activity we will inject our fragment
     * @param activity
     */
    private void injectLaunchComponent(@NonNull Activity activity) {
        //Here we are the main activity
        if(!(activity instanceof FragmentActivity)){
            throw new IllegalArgumentException("The main activity should extend from FragmentActivity! We can't support the Activity!");
        } else {
            FragmentActivity fragmentActivity = (FragmentActivity) activity;
            // Hide ActionBar
//                Resources.Theme theme = appCompatActivity.getTheme();
//                theme.applyStyle(R.style.SampleAppCompat,true);
            //Add the main fragment if needed;
            FragmentManager supportFragmentManager = fragmentActivity.getSupportFragmentManager();
            if (supportFragmentManager.findFragmentByTag(BIND_MAIN_SAMPLE_FRAGMENT_TAG) == null) {
                SampleApplication projectApplication = SampleApplication.getProjectApplication();
                AndroidSampleSupport androidSample = projectApplication.getAndroidSample();
                MainSampleComponentFactory<Fragment> componentContainerFactory = androidSample.getComponentContainerFactory();
                Fragment fragment = componentContainerFactory.getFragmentComponent();
                supportFragmentManager.beginTransaction().add(android.R.id.content,fragment, BIND_MAIN_SAMPLE_FRAGMENT_TAG).commit();
            }
        }
    }

    /**
     * Get android.intent.action.MAIN activity class name
     * @param context
     * @return
     */
    private String getLaunchActivityName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(context.getPackageName());
        return intent.getComponent().getClassName();
    }
}
