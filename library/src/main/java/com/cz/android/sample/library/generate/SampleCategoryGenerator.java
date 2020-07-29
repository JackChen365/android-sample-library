package com.cz.android.sample.library.generate;

import android.content.Context;

import androidx.annotation.NonNull;

import com.cz.android.sample.api.item.CategoryItem;
import com.cz.android.sample.api.item.RegisterItem;

import java.util.List;

public interface SampleCategoryGenerator {
    List<CategoryItem> generate(@NonNull Context context,@NonNull List<RegisterItem> registerItemList);
}
