package com.cz.sample.component

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cz.android.sample.api.RefRegister
import com.cz.android.sample.library.component.code.SampleSourceCode
import com.cz.android.sample.library.component.document.SampleDocument
import com.cz.android.sample.library.component.memory.SampleMemory
import com.cz.android.sample.library.component.message.SampleMessage
import com.cz.sample.R
import kotlinx.android.synthetic.main.fragment_component_sample.*


@SampleMemory
@SampleMessage
@SampleDocument("documentSample.md")
@SampleSourceCode("(.*Fragment\\.kt)|(.*\\.md)")
@RefRegister(title=R.string.component_sample5,desc = R.string.component_sample5_desc,category = R.string.component_category)
class ComponentSampleFragment : Fragment() {
    private var index=0
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_component_sample, container, false);
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        testButton.setOnClickListener {
            println("Message from ComponentSampleActivity:${index++}")
        }
    }
}