package com.cz.android.sample.library;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;

import com.cz.android.sample.api.AndroidSampleConstant;
import com.cz.android.sample.api.item.CategoryItem;
import com.cz.android.sample.api.item.Demonstrable;
import com.cz.android.sample.api.item.RegisterItem;
import com.cz.android.sample.component.ComponentContainer;
import com.cz.android.sample.component.ComponentManager;
import com.cz.android.sample.function.FunctionManager;
import com.cz.android.sample.function.SampleFunction;
import com.cz.android.sample.library.lifecycle.SampleActivityLifeCycleCallback;
import com.cz.android.sample.library.component.document.SampleDocumentComponent;
import com.cz.android.sample.library.component.memory.SampleMemoryComponent;
import com.cz.android.sample.library.component.message.SampleMessageComponent;
import com.cz.android.sample.library.function.permission.SamplePermissionFunction;
import com.cz.android.sample.library.main.DefaultMainSampleFragment;
import com.cz.android.sample.library.processor.FragmentClassActionProcessor;
import com.cz.android.sample.main.MainComponentFactory;
import com.cz.android.sample.processor.AbsActionProcessor;
import com.cz.android.sample.processor.ActionProcessManager;
import com.cz.android.sample.processor.exception.ActionExceptionHandler;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;


/**
 * @author Created by cz
 * @date 2020-01-27 20:31
 * @email bingo110@126.com
 */
public class AndroidSample {
    private static final String TAG="SampleConfiguration";
    private final static AndroidSample configuration=new AndroidSample();

    private final List<Demonstrable> demonstrableList = new ArrayList<>();
    private final ActionProcessManager actionProcessManager=new ActionProcessManager();
    private final ComponentManager componentManager=ComponentManager.getInstance();
    private final FunctionManager functionManager=new FunctionManager();
    private MainComponentFactory mainComponentContainer=new DefaultMainSampleFragment();

    /**
     * repository url
     */
    private String repositoryUrl;

    /**
     * initialize all the template data
     * see :process#AndroidSampleProcessor this will generate all the sample template by annotation processor
     * @param context
     */
    private void initAndroidSampleTemplate(Context context){
        Class clazz=null;
        try {
            clazz = Class.forName(AndroidSampleConstant.ANDROID_SIMPLE_CLASS);
        } catch (ClassNotFoundException e){
            Log.w(TAG,"Couldn't load class:"+AndroidSampleConstant.ANDROID_SIMPLE_CLASS_NAME+"!");
        }
        if(null!=clazz){
            List<CategoryItem> categoryList=getObjectValue(clazz,AndroidSampleConstant.CATEGORY_FIELD_NAME);
            List<RegisterItem> registerList=getObjectValue(clazz,AndroidSampleConstant.REGISTER_FIELD_NAME);
            List<String> functionList=getObjectValue(clazz,AndroidSampleConstant.FUNCTION_FIELD_NAME);
            List<String> componentList=getObjectValue(clazz,AndroidSampleConstant.COMPONENT_FIELD_NAME);
            List<String> actionProcessorList=getObjectValue(clazz,AndroidSampleConstant.PROCESSOR_FIELD_NAME);
            String mainComponentClass=getObjectValue(clazz,AndroidSampleConstant.MAIN_COMPONENT_FIELD_NAME);

            //process register and category translate string resources to string
            try {
                processCategoryList(context,categoryList,registerList);
                registerActionProcessor(actionProcessorList);
                registerComponentList(componentList);
                registerFunctionList(functionList);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //process main component
            try {
                processMainComponentClass(mainComponentClass);
            } catch (Exception e) {
                Log.w(TAG,e.getMessage());
            }
        }
    }

    /**
     * Get object field value by reflect
     * @param clazz
     * @param fieldName
     * @param <T>
     * @return
     */
    private<T> T getObjectValue(Class clazz,String fieldName){
        try {
            if(null!=clazz) {
                Object item = clazz.newInstance();
                Field registerItemField = clazz.getDeclaredField(fieldName);
                registerItemField.setAccessible(true);
                Object fieldValue = registerItemField.get(item);
                if (null != fieldValue) {
                    return (T) fieldValue;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void processCategoryList(Context context,List<CategoryItem> categoryItemList,List<RegisterItem> registerItems) {
        for(CategoryItem categoryItem:categoryItemList){
            if(AndroidSampleConstant.REF_TYPE==categoryItem.type){
                categoryItem.title=context.getString(categoryItem.titleRes);
                categoryItem.desc=context.getString(categoryItem.descRes);
                if(0==categoryItem.categoryRes){
                    categoryItem.category=AndroidSampleConstant.CATEGORY_ROOT;
                } else {
                    categoryItem.category=context.getString(categoryItem.categoryRes);
                }
            }
            demonstrableList.add(categoryItem);
        }
        for(RegisterItem registerItem:registerItems){
            if(AndroidSampleConstant.REF_TYPE==registerItem.type){
                registerItem.title=context.getString(registerItem.titleRes);
                registerItem.desc=context.getString(registerItem.descRes);
                if(0==registerItem.categoryRes){
                    registerItem.category=AndroidSampleConstant.CATEGORY_ROOT;
                } else {
                    registerItem.category=context.getString(registerItem.categoryRes);
                }
            }
            demonstrableList.add(registerItem);
        }
    }

    private void registerFunctionList(List<String> functionList) throws Exception {
        for(String functionClassName:functionList){
            Class<?> clazz = Class.forName(functionClassName);
            Object object = clazz.newInstance();
            functionManager.register((SampleFunction)object);
        }
    }

    private void registerComponentList(List<String> componentList) throws Exception {
        for(String className:componentList){
            Class<?> clazz = Class.forName(className);
            Object object = clazz.newInstance();
            componentManager.addComponentContainer((ComponentContainer) object);
        }
    }

    private void registerActionProcessor(List<String> actionProcessorList) throws Exception {
        for(String className:actionProcessorList){
            Class<?> clazz = Class.forName(className);
            Object object = clazz.newInstance();
            actionProcessManager.register((AbsActionProcessor) object);
        }
    }

    private void processMainComponentClass(String mainComponentClassName) throws Exception {
        if(null!=mainComponentClassName) {
            Class<?> clazz = Class.forName(mainComponentClassName);
            Object object = clazz.newInstance();
            if(null==object||!(object instanceof MainComponentFactory)){
                throw new IllegalArgumentException("Class:"+mainComponentClassName+" should implement from MainComponentFactory!");
            } else {
               this.mainComponentContainer = (MainComponentFactory) object;
            }
        }
    }

    /**
     * Get the singleton of android sample configuration
     * @return
     */
    public static AndroidSample getInstance(){
        return configuration;
    }

    /**
     * initialize all the setting
     */
    public void init(Context context){
        Application applicationContext = (Application)context.getApplicationContext();
        //initialize all the template data
        initAndroidSampleTemplate(context);
        //register fragment class processor
        actionProcessManager.register(new FragmentClassActionProcessor());

        //register component
        componentManager.addComponentContainer(new SampleDocumentComponent());
        componentManager.addComponentContainer(new SampleMessageComponent());
        componentManager.addComponentContainer(new SampleMemoryComponent());

        //register function
        functionManager.register(new SamplePermissionFunction());

        //register activity lifecycle
        applicationContext.registerActivityLifecycleCallbacks(new SampleActivityLifeCycleCallback());
    }

    /**
     * set up repository url in order to use relative path
     * this url link to src/main/java
     * e.g. https://github.com/momodae/SuperTextView/tree/master/app/src/main/java
     * @param url
     */
    public void setRepositoryUrl(String url){
        this.repositoryUrl=url;
    }

    /**
     * Register an exceptionHandler for action com.cz.android.sample.library.processor
     * @param exceptionHandler
     */
    public void registerExceptionHandler(ActionExceptionHandler exceptionHandler){
        this.actionProcessManager.registerExceptionHandler(exceptionHandler);
    }

    /**
     * run this sample
     * @param context
     * @param demonstrable
     */
    public void run(FragmentActivity context, RegisterItem demonstrable){
        actionProcessManager.process(functionManager,context,demonstrable);
    }

    /**
     * Return all the demonstrable if is belong to this category
     */
    public List<Demonstrable> getDemonstrableList(String category){
        Set<Demonstrable> filterDemonstrableSet=null;
        for(Demonstrable demonstrable: demonstrableList){
            if(demonstrable instanceof RegisterItem){
                RegisterItem registerItem = (RegisterItem) demonstrable;
                if(registerItem.category.equals(category)){
                    if(null==filterDemonstrableSet){
                        filterDemonstrableSet= new TreeSet<>(new Comparator<Demonstrable>() {
                            @Override
                            public int compare(Demonstrable t0, Demonstrable t1) {
                                int priority = t0.getPriority() - t1.getPriority();
                                if(0==priority){
                                    //sort by name
                                    priority=t0.getTitle().compareTo(t1.getTitle());
                                }
                                return priority;
                            }
                        });
                    }
                    filterDemonstrableSet.add(demonstrable);
                }
            } else if(demonstrable instanceof CategoryItem){
                CategoryItem categoryItem = (CategoryItem) demonstrable;
                if(categoryItem.category.equals(category)){
                    if(null==filterDemonstrableSet){
                        filterDemonstrableSet= new TreeSet<>(new Comparator<Demonstrable>() {
                            @Override
                            public int compare(Demonstrable t0, Demonstrable t1) {
                                //sort by priority
                                int priority = t0.getPriority() - t1.getPriority();
                                if(0==priority){
                                    //sort by name
                                    priority=t0.getTitle().compareTo(t1.getTitle());
                                }
                                return priority;
                            }
                        });
                    }
                    filterDemonstrableSet.add(demonstrable);
                }
            }
        }
        if(null==filterDemonstrableSet){
            return null;
        } else {
            List<Demonstrable> filterDemonstrableList=new ArrayList<>(filterDemonstrableSet.size());
            filterDemonstrableList.addAll(filterDemonstrableSet);
            return filterDemonstrableList;
        }
    }

    public MainComponentFactory getMainComponentContainer() {
        return this.mainComponentContainer;
    }

    public FunctionManager getFunctionManager() {
        return functionManager;
    }

    public String getRepositoryUrl() {
        return repositoryUrl;
    }
}
