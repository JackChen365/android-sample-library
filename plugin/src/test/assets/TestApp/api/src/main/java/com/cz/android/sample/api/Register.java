package com.cz.android.sample.api;

/**
 * Created by cz on 2018/8/9.
 */
public @interface Register {
    /**
     * title for this sample
     */
    String title() default "";
    /**
     * description for this sample
     */
    String desc() default "";

    String path() default "";
}
