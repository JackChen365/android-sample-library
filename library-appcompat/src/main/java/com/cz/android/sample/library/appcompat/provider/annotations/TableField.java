package com.cz.android.sample.library.appcompat.provider.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author :Created by cz
 * @date 2019-06-21 16:24
 * @email bingo110@126.com
 * Table field annotation.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TableField {
    String value();
    boolean primaryKey() default false;
    boolean autoIncrement() default false;
}
