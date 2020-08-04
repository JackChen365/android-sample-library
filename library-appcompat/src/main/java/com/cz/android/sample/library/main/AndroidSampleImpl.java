package com.cz.android.sample.library.main;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.cz.android.sample.api.AndroidSampleConstant;
import com.cz.android.sample.api.item.CategoryItem;
import com.cz.android.sample.api.item.Demonstrable;
import com.cz.android.sample.api.item.RegisterItem;
import com.cz.android.sample.component.ComponentContainer;
import com.cz.android.sample.component.ComponentManager;
import com.cz.android.sample.function.FunctionManager;
import com.cz.android.sample.function.SampleFunction;
import com.cz.android.sample.library.component.code.SampleSourceCodeComponent;
import com.cz.android.sample.library.component.document.SampleDocumentComponent;
import com.cz.android.sample.library.component.memory.SampleMemoryComponent;
import com.cz.android.sample.library.component.message.SampleMessageComponent;
import com.cz.android.sample.library.generate.DefaultSampleItemGenerator;
import com.cz.android.sample.library.generate.PackageCategoryGenerator;
import com.cz.android.sample.library.generate.SampleCategoryGenerator;
import com.cz.android.sample.library.generate.SampleItemGenerator;
import com.cz.android.sample.library.main.component.DefaultMainSampleFragment;
import com.cz.android.sample.library.permission.SamplePermissionFunction;
import com.cz.android.sample.library.processor.DialogFragmentClassActionProcessor;
import com.cz.android.sample.library.processor.FragmentClassActionProcessor;
import com.cz.android.sample.main.MainSampleComponentFactory;
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
class AndroidSampleImpl implements AndroidSampleSupport, AppCompatSampleConfiguration {
    private static final String TAG="SampleConfiguration";
    private final static AndroidSampleImpl androidSampleImpl =new AndroidSampleImpl();

    private final List<Demonstrable> demonstrableList = new ArrayList<>();
    private final List<RegisterItem> registerTestCaseList=new ArrayList<>();
    private final ActionProcessManager actionProcessManager=new ActionProcessManager();
    private final ComponentManager componentManager=ComponentManager.getInstance();
    private final FunctionManager functionManager=new FunctionManager();
    private MainSampleComponentFactory mainComponentContainer=new DefaultMainSampleFragment();
    /**
     * The default category generator was {@link PackageCategoryGenerator}
     * It generate the category by the class package path.
     */
    private SampleCategoryGenerator sampleCategoryGenerator=new PackageCategoryGenerator();

    /**
     * The default samples item generator. {@link SampleItemGenerator}
     * Generate all the sample by its class name. Also noticed it just generate the sample use the sub-class {@link android.support.v7.app.AppCompatActivity}
     * and {@link android.support.v4.app.Fragment} without use annotation to indicate that is a sample.
     *
     */
    private SampleItemGenerator sampleItemGenerator=new DefaultSampleItemGenerator();

    public AndroidSampleImpl() {
    }

    /**
     * initialize all the template data
     * see :process#AndroidSampleProcessor this will generate all the sample template by annotation processor
     * @param context
     */
    private void initAndroidSampleTemplate(Context context){
        Object object=null;
        try {
            Class clazz = Class.forName(AndroidSampleConstant.ANDROID_SIMPLE_CLASS);
            object = clazz.newInstance();
        } catch (Exception e){
            Log.w(TAG,"Couldn't load class:"+AndroidSampleConstant.ANDROID_SIMPLE_CLASS_NAME+"!");
        }
        if(null!=object){
            List<CategoryItem> categoryList=getObjectValue(object,AndroidSampleConstant.CATEGORY_FIELD_NAME);
            List<RegisterItem> registerList=getObjectValue(object,AndroidSampleConstant.REGISTER_FIELD_NAME);
            List<String> sampleClassList=getObjectValue(object,"sampleClassList");
            List<String> functionList=getObjectValue(object,AndroidSampleConstant.FUNCTION_FIELD_NAME);
            List<String> componentList=getObjectValue(object,AndroidSampleConstant.COMPONENT_FIELD_NAME);
            List<String> actionProcessorList=getObjectValue(object,AndroidSampleConstant.PROCESSOR_FIELD_NAME);
            List<String> testCaseList=getObjectValue(object,AndroidSampleConstant.TEST_FIELD_NAME);
            String categoryGeneratorClass=getObjectValue(object,"categoryGenerator");
            String mainComponentClass=getObjectValue(object,AndroidSampleConstant.MAIN_COMPONENT_FIELD_NAME);
            //process register and category translate string resources to string
            try {
                SampleCategoryGenerator categoryGenerator = processCategoryGenerator(categoryGeneratorClass);
                if(null!=categoryGenerator){
                    sampleCategoryGenerator=categoryGenerator;
                }
            } catch (Exception e) {
                Log.w(TAG,e.getMessage());
            }
            try {
                processDemonstrateList(context,categoryList,registerList);
                processSampleGenerator(context, categoryList, registerList, sampleClassList);
                registerActionProcessor(actionProcessorList);
                registerComponentList(componentList);
                registerFunctionList(functionList);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                addDemonstrateList(categoryList,registerList);
            }
            //process main component
            try {
                processMainComponentClass(mainComponentClass);
                processTestCases(testCaseList);
            } catch (Exception e) {
                Log.w(TAG,e.getMessage());
            }
        }
    }

    private void processSampleGenerator(Context context, List<CategoryItem> categoryList, List<RegisterItem> registerList, List<String> sampleClassList) {
        //Filter the launch activity.
        if(null!=sampleClassList){
            String launchActivityName = getLaunchActivityName(context);
            sampleClassList.remove(launchActivityName);
        }
        List<RegisterItem> newRegisterList = sampleItemGenerator.generate(context,sampleClassList);
        if(null!=newRegisterList){
            registerList.addAll(newRegisterList);
        }
        List<CategoryItem> newCategoryList = sampleCategoryGenerator.generate(context,registerList);
        if(null!=newCategoryList){
            //Filter the exist category.
            for(CategoryItem newCategoryItem:newCategoryList){
                boolean categoryExist=false;
                for(CategoryItem categoryItem:categoryList){
                    if(newCategoryItem.title.equals(categoryItem.title)){
                        categoryExist=true;
                        break;
                    }
                }
                if(!categoryExist){
                    categoryList.add(newCategoryItem);
                }
            }
        }
    }

    /**
     * Get android.intent.action.MAIN activity class name
     * @param context
     * @return
     */
    private String getLaunchActivityName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(context.getPackageName());
        return intent.getComponent().getClassName();
    }

    /**
     * Get object field value by reflect
     * @param object
     * @param fieldName
     * @param <T>
     * @return
     */
    private<T> T getObjectValue(Object object,String fieldName){
        try {
            Field registerItemField = object.getClass().getDeclaredField(fieldName);
            registerItemField.setAccessible(true);
            Object fieldValue = registerItemField.get(object);
            if (null != fieldValue) {
                return (T) fieldValue;
            }
        } catch (Exception e) {
            Log.w(TAG,"Object:"+object.getClass().getName()+"can't get field:"+fieldName+".");
        }
        return null;
    }

    private SampleCategoryGenerator processCategoryGenerator(String categoryGeneratorClass) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        SampleCategoryGenerator categoryGenerator=null;
        if (null != categoryGeneratorClass) {
            Class<?> clazz = Class.forName(categoryGeneratorClass);
            Object object = clazz.newInstance();
            if (null == object || !(object instanceof SampleCategoryGenerator)) {
                throw new IllegalArgumentException("Class:" + categoryGeneratorClass + " should implement from SampleCategoryGenerator!");
            }
            categoryGenerator=(SampleCategoryGenerator) object;
        }
        return categoryGenerator;
    }

    private void processDemonstrateList(Context context, List<CategoryItem> categoryItemList, List<RegisterItem> registerItems) {
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
        if (null != mainComponentClassName) {
            Class<?> clazz = Class.forName(mainComponentClassName);
            Object object = clazz.newInstance();
            if (null == object || !(object instanceof MainSampleComponentFactory)) {
                throw new IllegalArgumentException("Class:" + mainComponentClassName + " should implement from MainComponentFactory!");
            } else {
                this.mainComponentContainer = (MainSampleComponentFactory) object;
            }
        }
    }

    private void processTestCases(List<String> testCaseList)throws Exception{
        //here if we have a test case,Check this class from register sample list
        registerTestCaseList.clear();
        if(!testCaseList.isEmpty()){
            for(String className:testCaseList){
                Class<?> clazz = Class.forName(className);
                for(Demonstrable demonstrable:demonstrableList){
                    if(demonstrable instanceof RegisterItem){
                        RegisterItem registerItem = (RegisterItem) demonstrable;
                        if(clazz==registerItem.clazz){
                            //Collect all the register item
                            registerTestCaseList.add(registerItem);
                            break;
                        }
                    }
                }
            }
        }
    }

    private void addDemonstrateList(List<CategoryItem> categoryList, List<RegisterItem> registerList) {
        if(null!=categoryList){
            demonstrableList.addAll(categoryList);
        }
        if(null!=registerList){
            demonstrableList.addAll(registerList);
        }
    }

    /**
     * Get the singleton of android sample androidSampleImpl
     * @return
     */
    public static AndroidSampleImpl getInstance(){
        return androidSampleImpl;
    }
    @Override
    public void onCreate(Context context) {
        Application applicationContext = (Application)context.getApplicationContext();
        //initialize all the template data
        initAndroidSampleTemplate(context);
        //register fragment class processor
        actionProcessManager.register(new FragmentClassActionProcessor());
        actionProcessManager.register(new DialogFragmentClassActionProcessor());
        //register component
        componentManager.addComponentContainer(new SampleDocumentComponent());
        componentManager.addComponentContainer(new SampleMessageComponent());
        componentManager.addComponentContainer(new SampleMemoryComponent());
        componentManager.addComponentContainer(new SampleSourceCodeComponent());
        componentManager.relateCompanionComponent();

        //register function
        functionManager.register(new SamplePermissionFunction());

        //register activity lifecycle
        applicationContext.registerActivityLifecycleCallbacks(new SampleActivityLifeCycleCallback());
    }

    /**
     * run this sample
     * @param context
     * @param demonstrable
     */
    @Override
    public void start(FragmentActivity context, RegisterItem demonstrable){
        try {
            actionProcessManager.process(functionManager,context,demonstrable);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Register an exceptionHandler for action com.cz.android.sample.library.processor
     * @param exceptionHandler
     */
    @Override
    public void registerExceptionHandler(ActionExceptionHandler exceptionHandler){
        this.actionProcessManager.registerExceptionHandler(exceptionHandler);
    }

    /**
     * Return all the demonstrable if is belong to this category
     */
    @Override
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

    @Override
    public List<RegisterItem> getTestCases() {
        return registerTestCaseList;
    }

    public MainSampleComponentFactory getMainComponentContainer() {
        return this.mainComponentContainer;
    }

    @Override
    public FunctionManager getFunctionManager() {
        return functionManager;
    }
}
