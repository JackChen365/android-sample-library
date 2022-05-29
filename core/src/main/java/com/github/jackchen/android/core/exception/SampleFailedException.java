package com.github.jackchen.android.core.exception;

import androidx.annotation.Nullable;
import com.github.jackchen.android.sample.api.SampleItem;

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
