package com.cz.sample.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cz.android.sample.api.Category
import com.cz.android.sample.api.Register
import com.cz.android.sample.library.appcompat.SampleAppCompatFragment
import com.cz.android.sample.library.component.code.SampleSourceCode
import com.cz.android.sample.library.component.document.SampleDocument
import com.cz.sample.R

/**
 * 示例 Fragment类型的Demo
 */
@SampleSourceCode
@Category(title="Fragment",desc = "Fragment different cases")
@Register(category = "Fragment",title="Basic fragment",desc = "Basic fragment function")
@SampleDocument("https://raw.githubusercontent.com/momodae/SuperTextView/master/readme.md")
class SampleDocumentFragment : SampleAppCompatFragment() {
    override fun onCreateView(container: ViewGroup?, inflater: LayoutInflater, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sample_document, container, false)
    }
}