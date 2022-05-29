package com.github.jackchen.android.sample.library.component.message;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

/**
 * @author Created by cz
 * @date 2020-01-29 16:22
 * @email bingo110@126.com
 */
public class SampleMessageBindFragment extends Fragment{

    private static final String BIND_SAMPLE_MESSAGE_FRAGMENT_TAG = "cz.sample.bind_fragment_tag";

    public static void injectIfNeededIn(FragmentActivity activity, WorkThread<String> workThread) {
        FragmentManager supportFragmentManager = activity.getSupportFragmentManager();
        if (supportFragmentManager.findFragmentByTag(BIND_SAMPLE_MESSAGE_FRAGMENT_TAG) == null) {
            SampleMessageBindFragment fragment = new SampleMessageBindFragment(workThread);
            supportFragmentManager.beginTransaction().add(fragment, BIND_SAMPLE_MESSAGE_FRAGMENT_TAG).commit();
        }
    }

    private WorkThread<String> workThread;

    public SampleMessageBindFragment() {
    }

    public SampleMessageBindFragment(WorkThread<String> workThread) {
        this.workThread = workThread;
    }

    @Override
    public void onDestroy() {
        if(null!=workThread){
            workThread.clearObserver();
        }
        super.onDestroy();
    }
}
