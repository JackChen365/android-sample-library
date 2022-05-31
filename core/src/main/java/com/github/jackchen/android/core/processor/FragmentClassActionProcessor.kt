package com.github.jackchen.android.core.processor

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.github.jackchen.android.core.appcompat.SampleFragmentContainerActivity
import com.github.jackchen.android.core.exception.SampleFailedException
import com.github.jackchen.android.sample.api.SampleItem

/**
 * @author Created by cz
 * @date 2020-01-28 20:48
 * @email bingo110@126.com
 */
class FragmentClassActionProcessor : ActionProcessor() {
    override fun isAvailable(clazz: Class<*>?): Boolean {
        return Fragment::class.java.isAssignableFrom(clazz)
    }

    @Throws(SampleFailedException::class)
    override fun execute(context: AppCompatActivity, sampleItem: SampleItem) {
        SampleFragmentContainerActivity.startActivity(context, sampleItem)
    }
}