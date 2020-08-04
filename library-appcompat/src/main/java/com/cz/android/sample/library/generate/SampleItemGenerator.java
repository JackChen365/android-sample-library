package com.cz.android.sample.library.generate;

import android.content.Context;
import android.support.annotation.NonNull;

import com.cz.android.sample.api.item.RegisterItem;

import java.util.List;

/**
 * @author Created by cz
 * @date 2020/7/29 10:40 AM
 * @email bingo110@126.com
 */
public interface SampleItemGenerator {
    List<RegisterItem> generate(@NonNull Context context, @NonNull List<String> sampleClassList);
}
