package com.cz.android.appcompat.sample.custom

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cz.android.sample.api.RefRegister
import com.cz.android.sample.library.component.code.SampleSourceCode
import com.cz.sample.R


@SampleSourceCode
@RefRegister(title=R.string.other_sample5,desc = R.string.other_sample5_desc,category = R.string.other)
class SampleFileTestFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_source_test_sample, container, false);
    }
}