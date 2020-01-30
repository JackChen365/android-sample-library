package com.cz.android.sample.api;

/**
 * Created by cz on 2018/8/9.
 */
public @interface RefCategory {
    /**
     * title of this category
     */
    int title();
    /**
     * sorting priority for this category
     */
    int priority() default 0;
    /**
     * description for this category
     */
    int desc();
    /**
     * parent category's title
     */
    int category() default 0;
}
