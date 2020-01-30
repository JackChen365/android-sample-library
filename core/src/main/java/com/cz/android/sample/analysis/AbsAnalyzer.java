package com.cz.android.sample.analysis;

import android.content.Context;

/**
 * @author Created by cz
 * @date 2020-01-27 14:27
 * @email bingo110@126.com
 * An abstract class that you can extend from this to analyzer any data. It is usually work with {@link Source}
 */
public abstract class AbsAnalyzer<P,R1,R2> {
    /**
     * the data source object
     */
    private Source<P,R1> source;

    /**
     * set data source to this analyzer
     * @param source
     */
    public void setDataSource(Source<P,R1> source){
        this.source=source;
    }

    /**
     * from data source get data then analysis the data get result
     */
    public R2 analysis(Context context,P params){
        R2 result=null;
        if(null!=source){
            R1 e=source.getSource(context,params);
            //分析数据源
            result=analysisSource(e);
        }
        return result;
    }

    /**
     * 分析数据源
     */
    protected abstract R2 analysisSource(R1 params);
}
