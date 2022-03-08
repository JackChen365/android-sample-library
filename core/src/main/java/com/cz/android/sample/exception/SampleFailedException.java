package com.cz.android.sample.exception;

import androidx.annotation.Nullable;
import com.cz.android.sample.api.SampleItem;

/**
 * Created on 2022/3/1.
 *
 * @author Jack Chen
 * @email zhenchen@tubi.tv
 */
public class SampleFailedException extends RuntimeException {
    public final SampleItem sampleItem;

    public SampleFailedException(final SampleItem sampleItem) {
        this.sampleItem = sampleItem;
    }

    @Nullable @Override public String getMessage() {
        final String message = super.getMessage();
        StringBuilder output = new StringBuilder();
        output.append("title:" + sampleItem.title + "\n");
        output.append("desc:" + sampleItem.desc + "\n");
        output.append("class:" + sampleItem.className + "\n");
        output.append("path:" + sampleItem.path + "\n");
        output.append(message);
        return output.toString();
    }
}
