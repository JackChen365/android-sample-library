package com.cz.android.sample.library.appcompat;

import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.Toolbar;

import com.cz.android.sample.component.SampleComponentContainer;
import com.cz.android.sample.library.R;
import com.cz.android.sample.window.impl.ComponentWindowDelegate;

import java.util.Observable;
import java.util.Observer;

/**
 * @author Created by cz
 * @date 2020-01-28 14:05
 * @email bingo110@126.com
 */
public class SampleAppCompatActivity extends AppCompatActivity implements SampleComponentContainer {
    private ComponentWindowDelegate windowDelegate=new ComponentWindowDelegate();
    @Override
    public void setContentView(int layoutResID) {
//        super.setContentView(layoutResID);
        LinearLayout contentView = new LinearLayout(this);
        contentView.setOrientation(LinearLayout.VERTICAL);
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(layoutResID, contentView, false);
        setContentViewInternal(contentView,view);
    }

    @Override
    public void setContentView(View view) {
        LinearLayout contentView = new LinearLayout(this);
        contentView.setOrientation(LinearLayout.VERTICAL);
        setContentViewInternal(contentView,view);
    }

    private void setContentViewInternal(ViewGroup contentView,View view){
        if(hasToolBar(view)){
            View createView = windowDelegate.onCreateView(this,this,contentView, view);
            super.setContentView(createView);
        } else {
            Toolbar toolBar = new Toolbar(new ContextThemeWrapper(this, R.style.AppTheme_AppBarOverlay));
            //set toolbar background color.
            TypedArray a = obtainStyledAttributes(new int[]{R.attr.colorPrimary});
            int colorPrimary = a.getColor(0, Color.GRAY);
            toolBar.setBackgroundColor(colorPrimary);
            a.recycle();

            //set toolbar elevation
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Resources resources = getResources();
                toolBar.setElevation(resources.getDimension(R.dimen.sampleToolBarElevation));
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
            View createView = windowDelegate.onCreateView(this,this,contentView, view);

            //add child view to content view
            contentView.addView(toolBar, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            contentView.addView(createView,ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);

            super.setContentView(contentView);
        }
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
