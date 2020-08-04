package com.cz.android.sample.library.generate;

import android.content.Context;
import android.support.annotation.NonNull;


import com.cz.android.sample.api.AndroidSampleConstant;
import com.cz.android.sample.api.item.RegisterItem;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Created by cz
 * @date 2020/7/29 10:41 AM
 * @email bingo110@126.com
 */
public class DefaultSampleItemGenerator implements SampleItemGenerator {
    @Override
    public List<RegisterItem> generate(@NonNull Context context, @NonNull List<String> sampleClassList) {
        List<RegisterItem> registerItemList=new ArrayList<>();
        if(null!=sampleClassList){
            for(String className:sampleClassList){
                String classSimpleName;
                int i = className.lastIndexOf(".");
                if(0 > i){
                    //With out package.
                    classSimpleName=className;
                } else {
                    classSimpleName=className.substring(i+1);
                }
                Class<?> clazz=null;
                try {
                    clazz = Class.forName(className);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                if(null!=clazz){
                    registerItemList.add(new RegisterItem(classSimpleName, "", clazz, AndroidSampleConstant.CATEGORY_ROOT, 0));
                }
            }
        }
        return registerItemList;
    }
}
