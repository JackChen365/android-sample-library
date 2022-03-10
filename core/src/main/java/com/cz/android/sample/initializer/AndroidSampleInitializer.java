package com.cz.android.sample.initializer;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.startup.Initializer;
import com.cz.android.sample.AndroidSample;
import java.util.List;

/**
 * Created on 2022/3/5.
 *
 * @author Jack Chen
 * @email zhenchen@tubi.tv
 */
public class AndroidSampleInitializer implements Initializer<AndroidSample> {
    @NonNull @Override public AndroidSample create(@NonNull final Context context) {
        final AndroidSample androidSample = AndroidSample.Companion.getInstance();
        androidSample.attachToContext(context);
        return androidSample;
    }

    @NonNull @Override public List<Class<? extends Initializer<?>>> dependencies() {
        return null;
    }
}
