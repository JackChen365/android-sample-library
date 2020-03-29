package com.cz.android.sample.processor;

import androidx.fragment.app.FragmentActivity;

import com.cz.android.sample.api.item.RegisterItem;
import com.cz.android.sample.function.FunctionManager;
import com.cz.android.sample.processor.clazz.ActivityClassActionProcessor;
import com.cz.android.sample.processor.clazz.DialogClassActionProcessor;
import com.cz.android.sample.processor.clazz.DialogFragmentClassActionProcessor;
import com.cz.android.sample.processor.exception.ActionExceptionHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Created by cz
 * @date 2020-01-27 15:24
 * @email bingo110@126.com
 *
 * This is an action com.cz.android.sample.library.processor manager
 *
 * @see AbsActionProcessor
 */
public class ActionProcessManager {
    /**
     * An action exceptionHandlerList;
     */
    private List<ActionExceptionHandler> exceptionHandlerList =new ArrayList<>();
    /**
     * all registered com.cz.android.sample.library.processor list
     */
    private final List<AbsActionProcessor> processorList=new ArrayList<>();
    {
        //register default action com.cz.android.sample.library.processor
        register(new DialogClassActionProcessor());
        register(new DialogFragmentClassActionProcessor());
        register(new ActivityClassActionProcessor());
    }
    /**
     * Register an new action com.cz.android.sample.library.processor
     * @param processor
     */
    public void register(AbsActionProcessor processor){
        this.processorList.add(processor);
    }

    /**
     * Unregister an action com.cz.android.sample.library.processor from list
     * @param processor
     */
    public void unregister(AbsActionProcessor processor){
        this.processorList.remove(processor);
    }

    /**
     * Register an exceptionHandlerList for action com.cz.android.sample.library.processor
     * @param exceptionHandler
     */
    public void registerExceptionHandler(ActionExceptionHandler exceptionHandler){
        this.exceptionHandlerList.add(exceptionHandler);
    }

    /**
     * process the action when user execute an action
     */
    public void process(FunctionManager functionManager, final FragmentActivity context, final RegisterItem item) throws Exception {
        Class clazz=item.clazz;
        for (final AbsActionProcessor processor:processorList) {
            if (processor.isInstance(clazz)) {
                Object instance = null;
                try {
                    instance = processor.getInstance(context,item,clazz);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(null!=instance){
                    if(exceptionHandlerList.isEmpty()){
                        processor.run(context, item, instance);
                        //process all the functions
                        functionManager.execute(context,item,instance);
                    } else {
                        try {
                            processor.run(context, item, instance);
                            //process all the functions
                            functionManager.execute(context,item,instance);
                        } catch (Exception e) {
                            for(ActionExceptionHandler exceptionHandler:exceptionHandlerList){
                                exceptionHandler.handleException(context,e,item, instance);
                            }
                        }
                    }
                }
            }
        }
    }

}
