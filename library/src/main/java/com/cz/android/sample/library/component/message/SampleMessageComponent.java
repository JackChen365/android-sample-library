package com.cz.android.sample.library.component.message;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
public class SampleMessageComponent implements ComponentContainer {
    private final WorkThread<String> workThread=new WorkThread("sample_work_thread");
    private final SampleSystemConsole sampleSystemConsole=new SampleSystemConsole(workThread);

    public SampleMessageComponent() {
        workThread.startService();
    }

    @Override
    public boolean isComponentAvailable(@NonNull Object object) {
        SampleMessage sampleMessage = object.getClass().getAnnotation(SampleMessage.class);
        return null!=sampleMessage&&sampleMessage.value();
    }

    @Override
    public View getComponentView(FragmentActivity context, Object object, ViewGroup parentView,View view) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View contentLayout = layoutInflater.inflate(R.layout.sample_message_layout, parentView, false);
        FrameLayout sampleMessageContentLayout=contentLayout.findViewById(R.id.sampleMessageContentLayout);
        sampleMessageContentLayout.addView(view);
        return contentLayout;
    }

    @Override
    public void onCreatedView(FragmentActivity context, Object object, View view) {
        //If activity/fragment want to output message
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
        workThread.addObserver(observer);
        //here we try to bind SampleMessageBindFragment
        SampleMessageBindFragment.injectIfNeededIn(context,workThread);
    }

    @Override
    public int getComponentPriority() {
        return 1;
    }
}
