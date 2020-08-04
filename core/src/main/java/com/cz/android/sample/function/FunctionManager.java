package com.cz.android.sample.function;


import android.app.Activity;

import com.cz.android.sample.api.item.RegisterItem;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Created by cz
 * @date 2020-01-27 18:08
 * @email bingo110@126.com
 * @see SampleFunction
 *
 */
public class FunctionManager {
    /**
     * all the action plugin
     */
    private List<SampleFunction> functionList =new ArrayList<>();
    /**
     * Register a plugin. It will put new plugin to list.
     * @param plugin
     */
    public void register(SampleFunction plugin){
        functionList.add(plugin);
    }

    /**
     * unregister a plugin, It will remove the plugin from list.
     * @param plugin
     */
    public void unregister(SampleFunction plugin){
        functionList.remove(plugin);
    }

    public List<SampleFunction> getFunctionList() {
        return functionList;
    }

    /**
     * execute
     * @param context
     * @param item
     * @param object
     */
    public<T extends Activity> void execute(final T context, final RegisterItem item, final Object object) {
        for(SampleFunction function:functionList){
            executeFunction(function,context,object,item);
        }
    }

    /**
     * Execute one function when this function is available
     * @param function
     * @param context
     * @param object
     * @param item
     */
    private<T extends Activity> void executeFunction(SampleFunction function,final T context,final Object object,final RegisterItem item){
        if(function.isAvailable(item.clazz)){
            function.run(context,object,item);
        }
    }
}
