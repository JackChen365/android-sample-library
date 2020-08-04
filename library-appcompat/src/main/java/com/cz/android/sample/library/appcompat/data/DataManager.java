package com.cz.android.sample.library.appcompat.data;

import android.content.Context;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Created by cz
 * @date 2020-01-30 18:03
 * @email bingo110@126.com
 */
public class DataManager {
    /**
     * all registered data service
     */
    private static final Map<Class,Object> dataService=new HashMap<>();

    public static DataProvider getDataProvider(Context context){
        return DataManager.getDataService(context, DataProvider.class);
    }

    /**
     * Return a data service by class
     * @param clazz
     * @param <T>
     * @return
     */
    public synchronized static<T>T getDataService(Context context,Class<T> clazz){
        Object instance = dataService.get(clazz);
        if(null==instance){
            try {
                Constructor<T> constructor = clazz.getConstructor(Context.class);
                instance=constructor.newInstance(context);
                dataService.put(clazz,instance);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return (T)instance;
    }
}
