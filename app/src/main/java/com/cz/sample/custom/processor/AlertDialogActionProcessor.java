package com.cz.sample.custom.processor;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.cz.android.sample.api.Extension;
import com.cz.android.sample.exception.SampleFailedException;
import com.cz.android.sample.processor.SampleInterfaceProcessor;

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
