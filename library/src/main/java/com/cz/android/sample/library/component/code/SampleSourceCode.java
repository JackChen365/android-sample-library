package com.cz.android.sample.library.component.code;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author :Created by cz
 * @date 2019/4/18 上午9:53
 * @email bingo110@126.com
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SampleSourceCode {
    /**
     * If you want to filter the source files by regex.
     * @return
     */
    String value() default "";
}
