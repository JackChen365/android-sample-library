package com.github.jackchen.android.sample.library.component.message;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Created by cz
 * @date 2019-12-11 17:14
 * @email bingo110@126.com
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SampleMessage {
    /**
     * show message
     */
    boolean value() default true;
}
