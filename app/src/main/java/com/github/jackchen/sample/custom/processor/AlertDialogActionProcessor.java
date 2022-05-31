package com.github.jackchen.sample.custom.processor;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.github.jackchen.android.core.exception.SampleFailedException;
import com.github.jackchen.android.core.processor.SampleInterfaceProcessor;
import com.github.jackchen.android.sample.api.Extension;

/**
 * @author Created by cz
 * @date 2020-01-29 22:02
 * @email bingo110@126.com
 */
@Extension
public class AlertDialogActionProcessor extends SampleInterfaceProcessor<AlertDialog> {

    @Override public void execute(final AppCompatActivity context, final AlertDialog dialog)
            throws SampleFailedException {
        dialog.show();
    }
}
