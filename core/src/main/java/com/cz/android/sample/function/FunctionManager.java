package com.cz.android.sample.function;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.cz.android.sample.api.SampleItem;
import com.cz.android.sample.extension.ExtensionHandler;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Created by cz
 * @date 2020-01-27 18:08
 * @email bingo110@126.com
 * @see SampleFunction
 */
public class FunctionManager implements ExtensionHandler<SampleFunction> {
    /**
     * all the action plugin
     */
    private List<SampleFunction> functionList = new ArrayList<>();

    @Override
    public boolean handle(@NonNull final String className, @NonNull final String superClass,
            @NonNull final List<String> interfaces) {
        if (interfaces.contains(SampleFunction.class.getName())) {
            try {
                final Class<?> clazz = Class.forName(className);
                SampleFunction function = (SampleFunction) clazz.newInstance();
                register(function);
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    /**
     * Register a plugin. It will put new plugin to list.
     *
     * @param plugin
     */
    @Override
    public void register(SampleFunction plugin) {
        functionList.add(plugin);
    }

    /**
     * unregister a plugin, It will remove the plugin from list.
     *
     * @param plugin
     */
    @Override
    public void unregister(SampleFunction plugin) {
        functionList.remove(plugin);
    }

    public List<SampleFunction> getFunctionList() {
        return functionList;
    }

    /**
     * execute
     *
     * @param context
     * @param item
     */
    public <T extends Activity> void execute(final AppCompatActivity context, final SampleItem item) {
        for (SampleFunction function : functionList) {
            final Class<?> clazz = item.clazz();
            if (function.isAvailable(clazz)) {
                function.execute(context, item);
            }
        }
    }
}
