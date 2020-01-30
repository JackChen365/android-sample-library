package com.cz.android.sample.api;

/**
 * Created by cz on 2018/8/9.
 */
public @interface Register {
    /**
     * title for this sample
     */
    String title();
    /**
     * description for this sample
     */
    String desc();
    /**
     * sorting priority
     */
    int priority() default 0;
    /**
     * determine this sample belong to which category
     */
    String category() default AndroidSampleConstant.CATEGORY_ROOT;
    /**
     * the version of this sample
     * @return
     */
    String version() default "1.0.0";
}
