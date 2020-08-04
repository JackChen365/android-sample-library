package com.cz.android.sample.library.provider.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author :Created by cz
 * @date 2019-06-21 16:22
 * @email bingo110@126.com
 * Table annotation
 * 1. you could use this annotation change the table name
 * 2. setup primary key
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {
    String value() default "";
    String primaryKey() default "";
    boolean autoIncrement() default false;
    int version() default 0;
}
