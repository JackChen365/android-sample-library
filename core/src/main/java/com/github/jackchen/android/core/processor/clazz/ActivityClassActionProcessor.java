package com.github.jackchen.android.core.processor.clazz;

import android.app.Activity;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import com.github.jackchen.android.core.SampleConstants;
import com.github.jackchen.android.core.exception.SampleFailedException;
import com.github.jackchen.android.core.processor.ActionProcessor;
import com.github.jackchen.android.sample.api.SampleItem;

/**
 * @author Created by cz
 * @date 2020-01-27 15:48
 * @email bingo110@126.com
 */
public class ActivityClassActionProcessor extends ActionProcessor {
    @Override public boolean isAvailable(final Class clazz) {
        return Activity.class.isAssignableFrom(clazz);
    }

    @Override public void execute(final AppCompatActivity context, final SampleItem sampleItem)
            throws SampleFailedException {
        Class<?> clazz = sampleItem.clazz();
        Intent intent = new Intent(context, clazz);
        intent.putExtra(SampleConstants.PARAMETER_TITLE, sampleItem.title);
        intent.putExtra(SampleConstants.PARAMETER_DESC, sampleItem.desc);
        context.startActivity(intent);
    }
}
