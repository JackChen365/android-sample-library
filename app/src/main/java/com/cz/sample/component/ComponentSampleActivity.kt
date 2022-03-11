package com.cz.sample.component

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cz.android.sample.api.Register
import com.cz.android.sample.library.component.code.SampleSourceCode
import com.cz.android.sample.library.component.document.SampleDocument
import com.cz.android.sample.library.component.memory.SampleMemory
import com.cz.android.sample.library.component.message.SampleMessage
import com.cz.sample.R
import com.cz.sample.custom.component.SampleBorder
import com.cz.sample.databinding.ActivityComponentSampleBinding

@SampleMemory
@SampleMessage
@SampleBorder
@SampleSourceCode
@Register(title="基础组件演示",desc="展示通过注解添加三种不同组件为示例添加不同的功能扩展",path = "组件示例")
class ComponentSampleActivity : AppCompatActivity() {
    private var index=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityComponentSampleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Annotation above support two different functions

        //@SampleMemory will add memory panel to the sample
        //@SampleMessage will add message output panel
        binding.testButton.setOnClickListener {
            //This message will show up in message panel automatically
            println("Message from ComponentSampleActivity:${index++}")
        }
    }
}
