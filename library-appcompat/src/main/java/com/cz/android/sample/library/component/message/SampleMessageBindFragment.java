package com.cz.android.sample.library.component.message;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.cz.android.sample.library.thread.WorkThread;

/**
 * @author Created by cz
 * @date 2020-01-29 16:22
 * @email bingo110@126.com
 */
public class SampleMessageBindFragment extends Fragment {

    private static final String BIND_SAMPLE_MESSAGE_FRAGMENT_TAG = "cz.sample.bind_fragment_tag";

    public static void injectIfNeededIn(FragmentActivity activity, WorkThread<String> workThread) {
        FragmentManager supportFragmentManager = activity.getSupportFragmentManager();
        if (supportFragmentManager.findFragmentByTag(BIND_SAMPLE_MESSAGE_FRAGMENT_TAG) == null) {
            SampleMessageBindFragment fragment = new SampleMessageBindFragment();
            fragment.setWorkThread(workThread);
            supportFragmentManager.beginTransaction().add(fragment, BIND_SAMPLE_MESSAGE_FRAGMENT_TAG).commit();
        }
    }
    private WorkThread<String> workThread;

    public SampleMessageBindFragment() {
    }

    public void setWorkThread(WorkThread<String> workThread){
        this.workThread = workThread;
    }

    @Override
    public void onDestroy() {
        workThread.clearObserver();
        super.onDestroy();
    }
}
