package com.cz.android.sample.library.component.message;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentActivity;

import com.cz.android.sample.component.ComponentContainer;
import com.cz.android.sample.library.R;
import com.cz.android.sample.library.thread.WorkThread;

import java.util.Observable;
import java.util.Observer;

/**
 * @author Created by cz
 * @date 2020-01-29 16:28
 * @email bingo110@126.com
 */
public class SampleMessageComponent implements ComponentContainer <FragmentActivity>{
    private final WorkThread<String> workThread=new WorkThread("sample_work_thread");
    private final SampleSystemConsole sampleSystemConsole=new SampleSystemConsole();

    public SampleMessageComponent() {
        workThread.startService();
        sampleSystemConsole.setup(workThread);
    }

    @Override
    public boolean isComponentAvailable(@NonNull Object object) {
        SampleMessage sampleMessage = object.getClass().getAnnotation(SampleMessage.class);
        return null!=sampleMessage&&sampleMessage.value();
    }

    @Override
    public View getComponentView(FragmentActivity context, Object object, ViewGroup parentView,View view, Bundle saveInstance) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        final View contentLayout = layoutInflater.inflate(R.layout.sample_message_layout, parentView, false);
        FrameLayout sampleMessageContentLayout=contentLayout.findViewById(R.id.sampleMessageContentLayout);
        sampleMessageContentLayout.addView(view);
        return contentLayout;
    }

    @Override
    public void onCreatedView(FragmentActivity context, Object object, View view) {
        //If activity/fragment want to output message
        final NestedScrollView scrollView=view.findViewById(R.id.sampleScrollView);
        final TextView messageView = view.findViewById(R.id.sampleMessageText);
        final Observer observer=new Observer() {
            @Override
            public void update(Observable observable,final Object o) {
                messageView.post(new Runnable() {
                    @Override
                    public void run() {
                        messageView.append(o.toString());
                    }
                });
            }
        };
        final View scrollDownButton=view.findViewById(R.id.scrollDownButton);
        scrollDownButton.setSelected(true);
        final TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(scrollDownButton.isSelected()){
                    scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                }
            }
        };
        messageView.addTextChangedListener(textWatcher);
        scrollDownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollDownButton.setSelected(!scrollDownButton.isSelected());
                if(scrollDownButton.isSelected()){
                    messageView.addTextChangedListener(textWatcher);
                } else {
                    messageView.removeTextChangedListener(textWatcher);
                }
            }
        });
        View clearMessageButton=view.findViewById(R.id.clearMessageButton);
        clearMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                messageView.setText(null);
            }
        });

        workThread.addObserver(observer);
        //here we try to bind SampleMessageBindFragment
        SampleMessageBindFragment.injectIfNeededIn(context,workThread);
    }

    @Override
    public int getComponentPriority() {
        return 1;
    }
}
