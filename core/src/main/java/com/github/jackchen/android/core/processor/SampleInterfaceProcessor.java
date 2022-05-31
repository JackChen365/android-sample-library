package com.github.jackchen.android.core.processor;

import androidx.appcompat.app.AppCompatActivity;
import com.github.jackchen.android.core.exception.SampleFailedException;
import com.github.jackchen.android.sample.api.SampleItem;
import com.github.jackchen.android.core.main.sample.SampleInterface;

/**
 * @author Created by cz
 * @date 2020-01-29 22:02
 * @email bingo110@126.com
 */
public abstract class SampleInterfaceProcessor<T> extends ActionProcessor {
    @Override public boolean isAvailable(final Class clazz) {
        return SampleInterface.class.isAssignableFrom(clazz);
    }

    @Override public void execute(final AppCompatActivity context, final SampleItem sampleItem)
            throws SampleFailedException {
        try {
            Class<SampleInterface<T>> clazz = (Class<SampleInterface<T>>) Class.forName(sampleItem.className);
            final SampleInterface<T> sampleInterface = clazz.newInstance();
            final T object = sampleInterface.getObject(context);
            execute(context, object);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public abstract void execute(final AppCompatActivity context, T object) throws SampleFailedException;
}
