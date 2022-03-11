package com.cz.sample.custom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cz.android.sample.api.Register
import com.cz.android.sample.library.component.code.SampleSourceCode
import com.cz.sample.R


@SampleSourceCode
@Register(title = "源码查看", desc = "查看多层级的源码目录", path = "other")
class SampleFileTestFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_source_test_sample, container, false);
    }
}