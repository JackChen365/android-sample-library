package com.cz.android.sample.library.category;

import androidx.annotation.NonNull;

import com.cz.android.sample.api.item.CategoryItem;
import com.cz.android.sample.api.item.RegisterItem;

import java.util.List;

public interface CategoryGenerator {
    List<CategoryItem> generate(@NonNull List<RegisterItem> registerItemList);
}
