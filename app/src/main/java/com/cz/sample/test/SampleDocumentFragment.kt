package com.cz.sample.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cz.android.sample.api.Register
import com.cz.android.sample.library.component.document.SampleDocument
import com.cz.sample.R

@SampleDocument("assets://documentSample.md")
@Register(title = "演示文档", desc = "演示文档常规加载", path = "其他")
class SampleDocumentFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sample_document, container, false)
    }
}