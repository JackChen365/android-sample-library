package com.cz.sample.component

import android.os.Bundle
import com.cz.android.sample.api.RefRegister
import com.cz.android.sample.library.appcompat.SampleAppCompatActivity
import com.cz.android.sample.library.component.code.SampleSourceCode
import com.cz.android.sample.library.component.document.SampleDocument
import com.cz.android.sample.library.component.memory.SampleMemory
import com.cz.android.sample.library.component.message.SampleMessage
import com.cz.sample.R
import kotlinx.android.synthetic.main.activity_component_list_sample.*

@SampleMemory
@SampleMessage
@SampleSourceCode
@SampleDocument("documentSample.md")
@RefRegister(title=R.string.component_sample4,desc=R.string.component_sample4_desc,category = R.string.component_category,priority = 3)
class ComponentListSampleActivity : SampleAppCompatActivity() {
    private var index=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_component_list_sample)
        testButton.setOnClickListener {
            //This message will show up in message panel automatically
            println("Message from ComponentListSampleActivity:${index++}\n")
        }
    }
}
