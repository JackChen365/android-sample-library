package com.cz.android.appcompat.sample.component

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.cz.android.appcompat.sample.custom.component.SampleBorder
import com.cz.android.sample.api.RefRegister
import com.cz.android.sample.library.component.code.SampleSourceCode
import com.cz.android.sample.library.component.memory.SampleMemory
import com.cz.android.sample.library.component.message.SampleMessage
import com.cz.sample.R
import kotlinx.android.synthetic.main.activity_component_sample.*

@SampleMemory
@SampleMessage
@SampleBorder
@SampleSourceCode
@RefRegister(title=R.string.component_sample1,desc=R.string.component_sample1_desc,category = R.string.component_category)
class ComponentSampleActivity : AppCompatActivity() {
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
