package com.cz.android.sample.library.checker;

/**
 * @author Created by cz
 * @date 2020-05-17 17:20
 * @email bingo110@126.com
 */
public abstract class AnnotationChecker {
    public abstract String getAnnotation();

    public boolean isAnnotation(String desc){
        String annotation = getAnnotation();
        String classDesc = "L" + annotation.replace(".", "/") + ";";
        return classDesc.equals(desc);
    }
}
