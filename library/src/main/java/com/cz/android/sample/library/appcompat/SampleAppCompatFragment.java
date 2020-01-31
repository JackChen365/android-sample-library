package com.cz.android.sample.library.appcompat;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.cz.android.sample.component.SampleComponentContainer;
import com.cz.android.sample.library.R;
import com.cz.android.sample.window.impl.ComponentWindowDelegate;


/**
 * @author Created by cz
 * @date 2020-01-28 20:37
 * @email bingo110@126.com
 */
@Keep
public abstract class SampleAppCompatFragment extends Fragment implements SampleComponentContainer {
    private ComponentWindowDelegate windowDelegate=new ComponentWindowDelegate();
    @Nullable
    public abstract View onCreateView(@Nullable ViewGroup container,@NonNull LayoutInflater inflater, @Nullable Bundle savedInstanceState);

    @Nullable
    @Override
    public final View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = onCreateView(container, inflater, savedInstanceState);
        FragmentActivity activity = getActivity();
        if(hasToolBar(view)){
            View createView = windowDelegate.onCreateView(activity, this,container,view);
            return createView;
        } else {
            View contentView = getContentViewInternal(view.getContext(), view);
            View createView = windowDelegate.onCreateView(activity,this,container, contentView);
            return createView;
        }
    }

    private View getContentViewInternal(Context context, View view){
        LinearLayout contentView = new LinearLayout(context);
        contentView.setOrientation(LinearLayout.VERTICAL);

        Toolbar toolBar = new Toolbar(new ContextThemeWrapper(context, R.style.AppTheme_AppBarOverlay));
        //set toolbar background color.
        TypedArray a = context.obtainStyledAttributes(new int[]{R.attr.colorPrimary});
        int colorPrimary = a.getColor(0, Color.GRAY);
        toolBar.setBackgroundColor(colorPrimary);
        a.recycle();

        //set toolbar elevation
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Resources resources = getResources();
            toolBar.setElevation(resources.getDimension(R.dimen.sampleToolBarElevation));
        }

        //add child view to content view
        contentView.addView(toolBar, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        contentView.addView(view,ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
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
        return contentView;
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
