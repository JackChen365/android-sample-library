package com.cz.android.sample.library.project;

import android.content.Context;
import android.util.Log;

import com.cz.android.sample.api.AndroidSampleConstant;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Created by cz
 * @date 2020-01-30 22:05
 * @email chenzhen@okay.cn
 */
public class SampleProjectFileSystem {
    private static final String TAG="SampleProjectFileSystem";
    private Map<String,List<String>> fileSystemMap=new HashMap<>();
    /**
     * repository url
     */
    private String repositoryUrl;

    /**
     * initialize all the template data
     * see :process#AndroidSampleProcessor this will generate all the sample template by annotation processor
     * @param context
     */
    public void initAndroidSampleProjectFile(Context context){
        if(null!=repositoryUrl){
            Object object=null;
            try {
                Class clazz = Class.forName(AndroidSampleConstant.PROJECT_FILE_CLASS);
                object = clazz.newInstance();
            } catch (Exception e){
                Log.w(TAG,"Couldn't load class:"+AndroidSampleConstant.PROJECT_FILE_CLASS+"!");
            }
            if(null!=object){
                List<File> projectFileList=getObjectValue(object,AndroidSampleConstant.PROJECT_FILE_LIST_FIELD_NAME);
                //Process all the project files
                try {
                    processProjectFileList(repositoryUrl,projectFileList);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println();
            }
        }
    }

    /**
     *
     * @param repositoryUrl
     * @param fileList
     */
    private void processProjectFileList(String repositoryUrl, List<File> fileList) {
        for(File file:fileList){
            String packageName=file.getParent().replace(File.separator,".");
            List<String> files = fileSystemMap.get(packageName);
            if(null==files){
                files=new ArrayList<>();
                fileSystemMap.put(packageName,files);
            }
            String filePath = file.getPath();
            if(!repositoryUrl.endsWith("/")){
                files.add(repositoryUrl+"/"+filePath);
            } else {
                files.add(repositoryUrl+filePath);
            }
        }
    }

    /**
     * Get object field value by reflect
     * @param object
     * @param fieldName
     * @param <T>
     * @return
     */
    private<T> T getObjectValue(Object object,String fieldName){
        try {
            Field registerItemField = object.getClass().getDeclaredField(fieldName);
            registerItemField.setAccessible(true);
            Object fieldValue = registerItemField.get(object);
            if (null != fieldValue) {
                return (T) fieldValue;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * set up repository url in order to use relative path
     * this url link to src/main/java
     * e.g. https://github.com/momodae/SuperTextView/tree/master/app/src/main/java
     * @param repositoryUrl
     */
    public void setRepositoryUrl(String repositoryUrl) {
        this.repositoryUrl = repositoryUrl;
    }

    public String getRepositoryUrl() {
        return repositoryUrl;
    }
}
