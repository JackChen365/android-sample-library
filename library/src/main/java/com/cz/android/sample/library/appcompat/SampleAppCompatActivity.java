package com.cz.android.sample.library.appcompat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.Toolbar;

import com.cz.android.sample.component.SampleComponentContainer;
import com.cz.android.sample.library.R;
import com.cz.android.sample.window.impl.ComponentWindowDelegate;

/**
 * @author Created by cz
 * @date 2020-01-28 14:05
 * @email bingo110@126.com
 */
public class SampleAppCompatActivity extends AppCompatActivity implements SampleComponentContainer {
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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //Because if we restart this activity. It will re-create the fragment by FragmentManagerState
        if(null!=savedInstanceState){
            savedInstanceState.remove(ANDROID_SUPPORT_FRAGMENTS);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layout) {
        String launchActivityName = getLaunchActivityName(this);
        if(launchActivityName.equals(getClass().getName())){
            super.setContentView(layout);
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
           super.setContentView(view);
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
            View createView = windowDelegate.onCreateView(this,this,contentView, view);
            super.setContentView(createView);
        } else {
            View createView = windowDelegate.onCreateView(this,this, contentView, view);
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
