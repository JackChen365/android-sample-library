package com.cz.sample.component

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cz.android.sample.api.Register
import com.cz.android.sample.library.component.code.SampleSourceCode
import com.cz.android.sample.library.component.document.SampleDocument
import com.cz.android.sample.library.component.memory.SampleMemory
import com.cz.android.sample.library.component.message.SampleMessage
import com.cz.sample.R
import com.cz.sample.databinding.ActivityComponentListSampleBinding

@SampleMemory
@SampleMessage
@SampleSourceCode
@SampleDocument("documentSample.md")
@Register(title="组件集",desc="展示当前己扩展的组件",path = "组件示例")
class ComponentListSampleActivity : AppCompatActivity() {
    private var index=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityComponentListSampleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.testButton.setOnClickListener {
            //This message will show up in message panel automatically
            println("Message from ComponentListSampleActivity:${index++}\n")
        }
    }
}
