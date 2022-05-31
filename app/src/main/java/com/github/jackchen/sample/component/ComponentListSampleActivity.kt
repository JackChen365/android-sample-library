package com.github.jackchen.sample.component

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.jackchen.android.sample.api.Register
import com.github.jackchen.android.sample.library.component.code.SampleSourceCode
import com.github.jackchen.android.sample.library.component.document.SampleDocument
import com.github.jackchen.android.sample.library.component.memory.SampleMemory
import com.github.jackchen.android.sample.library.component.message.SampleMessage
import com.github.jackchen.sample.databinding.ActivityComponentListSampleBinding

@SampleMemory
@SampleMessage
@SampleSourceCode
@SampleDocument("documentSample.md")
@Register(title = "组件集", desc = "展示当前己扩展的组件")
class ComponentListSampleActivity : AppCompatActivity() {
    private var index = 0
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
