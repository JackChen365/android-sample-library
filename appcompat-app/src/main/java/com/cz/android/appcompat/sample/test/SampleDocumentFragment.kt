package com.cz.android.appcompat.sample.test

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cz.android.sample.library.component.document.SampleDocument
import com.cz.sample.R

@SampleDocument("assets://documentSample.md")
//@RefCategory(title = R.string.other,desc = R.string.other_desc,priority = 4)
//@RefRegister(title = R.string.other_sample1,desc=R.string.other_sample1_desc,category = R.string.other)
class SampleDocumentFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sample_document, container, false)
    }
}