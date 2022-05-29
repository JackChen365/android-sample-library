package com.github.jackchen.sample.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.jackchen.android.sample.api.Register
import com.github.jackchen.android.sample.library.component.document.SampleDocument
import com.github.jackchen.sample.R

@SampleDocument("assets://documentSample.md")
@Register(title = "演示文档", desc = "演示文档常规加载")
class SampleDocumentFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sample_document, container, false)
    }
}