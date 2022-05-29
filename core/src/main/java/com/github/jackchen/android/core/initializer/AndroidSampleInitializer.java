package com.github.jackchen.android.core.initializer;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.startup.Initializer;
import com.github.jackchen.android.core.AndroidSample;
import java.util.Collections;
import java.util.List;

public class AndroidSampleInitializer implements Initializer<AndroidSample> {
    @NonNull @Override public AndroidSample create(@NonNull final Context context) {
        final AndroidSample androidSample = AndroidSample.Companion.getInstance();
        androidSample.attachToContext(context);
        return androidSample;
    }

    @NonNull @Override public List<Class<? extends Initializer<?>>> dependencies() {
        return Collections.emptyList();
    }
}
