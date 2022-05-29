package com.github.jackchen.android.core.main.sample;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @author Created by cz
 * @date 2020-01-29 21:11
 * @email bingo110@126.com
 */
public interface SampleInterface<T> {
    /**
     * Return the object when you want to display
     * @return
     */
    T getObject(AppCompatActivity context);
}
