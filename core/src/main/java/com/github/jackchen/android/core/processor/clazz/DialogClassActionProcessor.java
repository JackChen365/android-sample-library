package com.github.jackchen.android.core.processor.clazz;

import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import com.github.jackchen.android.core.exception.SampleFailedException;
import com.github.jackchen.android.core.processor.ActionProcessor;
import com.github.jackchen.android.sample.api.SampleItem;
import java.lang.reflect.Constructor;

/**
 * @author Created by cz
 * @date 2020-01-27 15:13
 * @email bingo110@126.com
 */
public class DialogClassActionProcessor extends ActionProcessor {

    @Override public boolean isAvailable(final Class clazz) {
        return DialogFragment.class.isAssignableFrom(clazz);
    }

    @Override public void execute(final AppCompatActivity context, final SampleItem sampleItem)
            throws SampleFailedException {
        try {
            Class clazz = sampleItem.clazz();
            Constructor<DialogFragment> constructor = clazz.getConstructor(Context.class);
            DialogFragment dialog = constructor.newInstance(context);
            dialog.show(context.getSupportFragmentManager(), null);
        } catch (Exception e) {
            throw new SampleFailedException(sampleItem);
        }
    }

}
