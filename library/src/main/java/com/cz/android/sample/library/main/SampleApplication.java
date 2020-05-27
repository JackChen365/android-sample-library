package com.cz.android.sample.library.main;

import android.content.Context;

import com.cz.android.sample.AndroidSample;
import com.cz.android.sample.library.component.document.DocumentAssetsManager;
import com.cz.android.sample.library.provider.DatabaseHelper;

import java.util.ArrayList;


/**
 * @author Created by cz
 * @date 2020-01-31 10:23
 * @email bingo110@126.com
 *
 * Project Application is not an android application.
 * Its duty was try to make the different part of this project have the change to initialize something with context
 * Most importantly we try to do this by ourselves not let user call init in application
 *
 *
 * @hide Even through is a singleton do not use this object outside
 */
public class SampleApplication{

    private final static SampleApplication projectApplication =new SampleApplication();

    public static SampleApplication getProjectApplication(){
        return projectApplication;
    }

    public SampleApplication() {
        registerConfiguration(DatabaseHelper.getInstance());
        registerConfiguration(AndroidSampleImpl.getInstance());
        registerConfiguration(DocumentAssetsManager.getInstance());
        androidSampleImpl=AndroidSampleImpl.getInstance();
    }

    /**
     * The list of configurations.  An configuration can be in the list at most
     * once and will never be null.
     */
    protected final ArrayList<SampleConfiguration> configurations = new ArrayList<>();

    /**
     * Android sample object
     */
    private AndroidSample androidSampleImpl;
    /**
     * When this project attach to context. We will notify all the configurations
     * @param context
     */
    public void attachToContext(Context context){
        synchronized(configurations) {
            for(SampleConfiguration configuration:configurations){
                configuration.onCreate(context);
            }
        }
    }

    public AndroidSample getAndroidSample() {
        return androidSampleImpl;
    }

    /**
     * Adds an configuration to the list. The configuration cannot be null and it must not already
     * be registered.
     * @param configuration the configuration to register
     * @throws IllegalArgumentException the configuration is null
     * @throws IllegalStateException the configuration is already registered
     */
    public void registerConfiguration(SampleConfiguration configuration) {
        if (configuration == null) {
            throw new IllegalArgumentException("The configuration is null.");
        }
        synchronized(configurations) {
            if (configurations.contains(configuration)) {
                throw new IllegalStateException("Observer " + configuration + " is already registered.");
            }
            configurations.add(configuration);
        }
    }
    /**
     * Removes a previously registered configuration. The configuration must not be null and it
     * must already have been registered.
     * @param configuration the configuration to unregister
     * @throws IllegalArgumentException the configuration is null
     * @throws IllegalStateException the configuration is not yet registered
     */
    public void unregisterObserver(SampleConfiguration configuration) {
        if (configuration == null) {
            throw new IllegalArgumentException("The configuration is null.");
        }
        synchronized(configurations) {
            int index = configurations.indexOf(configuration);
            if (index == -1) {
                throw new IllegalStateException("Observer " + configuration + " was not registered.");
            }
            configurations.remove(index);
        }
    }

    /**
     * Remove all registered configurations.
     */
    public void unregisterAll() {
        synchronized(configurations) {
            configurations.clear();
        }
    }

}
