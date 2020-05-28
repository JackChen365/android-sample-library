package com.cz.android.sample.api;

/**
 * Created by cz on 2018/8/9.
 */
public @interface RefRegister {
    /**
     * title for this sample
     */
    int title();
    /**
     * description for this sample
     */
    int desc() default 0;
    /**
     * sorting priority
     */
    int priority() default 0;
    /**
     * determine this sample belong to which category
     */
    int category() default 0;
    /**
     * the version of this sample
     * @return
     */
    String version() default "1.0.0";
}
