package com.cz.android.sample.library.file;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.cz.android.sample.api.AndroidSampleConstant;
import com.cz.android.sample.library.main.SampleConfiguration;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * @author Created by cz
 * @date 2020-01-30 22:05
 * @email bingo110@126.com
 */
public class SampleProjectFileSystemManager implements SampleConfiguration {
    private static final String TAG="SampleProjectFileSystem";
    private static final SampleProjectFileSystemManager fileSystemManager=new SampleProjectFileSystemManager();
    private Map<String,List<String>> fileSystemMap=new HashMap<>();

    public static SampleProjectFileSystemManager getInstance(){
        return fileSystemManager;
    }

    private SampleProjectFileSystemManager() {
    }

    /**
     * initialize all the template data
     * see :process#AndroidSampleProcessor this will generate all the sample template by annotation processor
     * @param context
     */
    @Override
    public void onCreate(Context context) {
        Object object=null;
        try {
            Class clazz = Class.forName(AndroidSampleConstant.PROJECT_FILE_CLASS);
            object = clazz.newInstance();
        } catch (Exception e){
            Log.w(TAG,"Couldn't load class:"+AndroidSampleConstant.PROJECT_FILE_CLASS+"!");
        }
        if(null!=object){
            List<String> projectFileList=getObjectValue(object,AndroidSampleConstant.PROJECT_FILE_LIST_FIELD_NAME);
            //Process all the project files
            try {
                processProjectFileList(projectFileList);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println();
        }
    }

    /**
     *
     * @param fileList
     */
    private void processProjectFileList(List<String> fileList) {
        for(String filePath:fileList){
            File file=new File(filePath);
            String parent = file.getParent();
            String packageName;
            if(null==parent){
                packageName="";
                //For example:there is a file without package.
            } else {
                packageName=parent.replace(File.separator,".");
            }
            List<String> files = fileSystemMap.get(packageName);
            if(null==files){
                files=new ArrayList<>();
                fileSystemMap.put(packageName,files);
            }
            files.add(filePath);
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
     * Return all repository file that in the same package name. Including all the sub directory
     * @param packageName
     * @return
     */
    public List<String> getProjectFileList(String packageName,String fileFilter){
        List<String> fileList=null;
        Pattern pattern=null;
        if(!TextUtils.isEmpty(fileFilter)){
            try {
                pattern = Pattern.compile(fileFilter);
            } catch (PatternSyntaxException e){
                Log.w(TAG,e.getMessage());
            }
        }
        for(Map.Entry<String,List<String>> entry:fileSystemMap.entrySet()){
            String filePackageName = entry.getKey();
            if(filePackageName.startsWith(packageName)){
                if(null==fileList){
                    fileList=new ArrayList<>();
                }
                List<String> value = entry.getValue();
                if(null!=value){
                    for(String fileName:value){
                        if(!fileName.startsWith(".")&&
                                (null==pattern||pattern.matcher(fileName).matches())){
                            fileList.add(fileName);
                        }
                    }
                }
            }
        }
        return fileList;
    }
}
