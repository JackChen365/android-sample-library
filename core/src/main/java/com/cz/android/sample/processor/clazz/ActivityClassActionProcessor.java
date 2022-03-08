package com.cz.android.sample.processor.clazz;

import android.app.Activity;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import com.cz.android.sample.SampleConstants;
import com.cz.android.sample.api.SampleItem;
import com.cz.android.sample.exception.SampleFailedException;
import com.cz.android.sample.processor.ActionProcessor;

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
