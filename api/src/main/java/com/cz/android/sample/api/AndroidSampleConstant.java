package com.cz.android.sample.api;

/**
 * @author Created by cz
 * @date 2020-01-27 13:39
 * @email bingo110@126.com
 */
public class AndroidSampleConstant {
    /**
     * configuration class package name
     */
    public static final String ITEM_CLASS_NAME ="com.cz.android.sample.api.item";
    /**
     * default generate package name
     */
    public static final String CLASS_PACKAGE ="com.cz.android.sample";
    /**
     * default generate class name
     */
    public static final String ANDROID_SIMPLE_CLASS_NAME="AndroidSampleTemplate";
    /**
     * default generated class path
     */
    public static final String ANDROID_SIMPLE_CLASS=CLASS_PACKAGE+"."+ANDROID_SIMPLE_CLASS_NAME;



    public static final String PROJECT_FILE_CLASS_NAME="AndroidProjectFile";
    /**
     * default generated class path
     */
    public static final String PROJECT_FILE_CLASS=CLASS_PACKAGE+"."+PROJECT_FILE_CLASS_NAME;


    public static final String CATEGORY_ROOT="<#>root<#>";

    public static final int NORMAL_TYPE=0;
    public static final int REF_TYPE=1;

    /**
     * All the field that template class needed
     */
    public static final String REGISTER_FIELD_NAME="registerList";
    public static final String CATEGORY_FIELD_NAME="categoryList";
    public static final String MAIN_COMPONENT_FIELD_NAME="mainComponent";
    public static final String REPOSITORY_URL_FIELD_NAME="repositoryUrl";
    public static final String FUNCTION_FIELD_NAME="functionList";
    public static final String COMPONENT_FIELD_NAME="componentList";
    public static final String PROCESSOR_FIELD_NAME="processorList";
    public static final String TEST_FIELD_NAME="testCaseList";

    /**
     * All the field that class:AndroidProjectFile has
     */
    public static final String PROJECT_FILE_LIST_FIELD_NAME="projectFileList";
}
