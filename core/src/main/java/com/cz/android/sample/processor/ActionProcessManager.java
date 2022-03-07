package com.cz.android.sample.processor;

import androidx.appcompat.app.AppCompatActivity;
import com.cz.android.sample.api.SampleItem;
import com.cz.android.sample.exception.SampleFailedException;
import com.cz.android.sample.function.FunctionManager;
import com.cz.android.sample.processor.clazz.ActivityClassActionProcessor;
import com.cz.android.sample.processor.clazz.DialogClassActionProcessor;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Created by cz
 * @date 2020-01-27 15:24
 * @email bingo110@126.com
 * This is an action com.cz.android.sample.library.processor manager
 * @see ActionProcessor
 */
public class ActionProcessManager {
    /**
     * all registered com.cz.android.sample.library.processor list
     */
    private final List<ActionProcessor> processorList = new ArrayList<>();

    {
        //register default action com.cz.android.sample.library.processor
        register(new DialogClassActionProcessor());
        register(new ActivityClassActionProcessor());
    }

    /**
     * Register an new action com.cz.android.sample.library.processor
     *
     * @param processor
     */
    public void register(ActionProcessor processor) {
        this.processorList.add(processor);
    }

    /**
     * Unregister an action com.cz.android.sample.library.processor from list
     *
     * @param processor
     */
    public void unregister(ActionProcessor processor) {
        this.processorList.remove(processor);
    }

    /**
     * process the action when user execute an action
     */
    public void process(FunctionManager functionManager, final AppCompatActivity context,
            final SampleItem item) throws SampleFailedException {
        Class clazz = item.clazz();
        for (final ActionProcessor processor : processorList) {
            if (processor.isAvailable(clazz)) {
                processor.execute(context, item);
                //process all the functions
                functionManager.execute(context, item);
            }
        }
    }

}
