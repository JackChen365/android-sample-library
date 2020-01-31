package com.cz.sample.component

import android.os.Bundle
import com.cz.android.sample.api.RefRegister
import com.cz.android.sample.library.appcompat.SampleAppCompatActivity
import com.cz.android.sample.library.component.memory.SampleMemory
import com.cz.android.sample.library.component.message.SampleMessage
import com.cz.sample.R
import kotlinx.android.synthetic.main.activity_component_sample.*

@SampleMemory
@SampleMessage
@RefRegister(title=R.string.component_sample1,desc=R.string.component_sample1_desc,category = R.string.component_category)
class ComponentSampleActivity : SampleAppCompatActivity() {
    private var index=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_component_sample)

        //Annotation above support two different functions

        //@SampleMemory will add memory panel to the sample
        //@SampleMessage will add message output panel
        testButton.setOnClickListener {
            //This message will show up in message panel automatically
            println("Message from ComponentSampleActivity:${index++}")
        }
    }
}
