package com.cz.android.sample.analysis;

import android.content.Context;

/**
 * @author Created by cz
 * @date 2020-01-27 14:27
 * @email bingo110@126.com
 * It's an abstract class you could implement from this to get data
 */
public abstract class Source<P,R> {

    /**
     * get source data
     * @param params whatever the parameters
     */
    public abstract R getSource(Context context, P params);
}
