package com.cz.android.sample.api;

/**
 * Created by cz on 2018/8/9.
 */
public @interface Category {
    /**
     * title of this category
     */
    String title();
    /**
     * sorting priority for this category
     */
    int priority() default 0;
    /**
     * description for this category
     */
    String desc();
    /**
     * parent category's title
     */
    String category() default AndroidSampleConstant.CATEGORY_ROOT;
}
