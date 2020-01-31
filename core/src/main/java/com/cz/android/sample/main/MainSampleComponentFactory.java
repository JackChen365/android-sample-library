package com.cz.android.sample.main;

import androidx.fragment.app.Fragment;

/**
 * @author Created by cz
 * @date 2020-01-29 14:19
 * @email bingo110@126.com
 */
public interface MainSampleComponentFactory {
    /**
     * return a new fragment that home pager need
     * @return
     */
    Fragment getFragmentComponent();
}
