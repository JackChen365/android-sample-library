package com.github.jackchen.sample.component

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.jackchen.android.sample.api.Register
import com.github.jackchen.android.sample.library.component.code.SampleSourceCode
import com.github.jackchen.android.sample.library.component.memory.SampleMemory
import com.github.jackchen.android.sample.library.component.message.SampleMessage
import com.github.jackchen.sample.custom.component.SampleBorder
import com.github.jackchen.sample.databinding.ActivityComponentSampleBinding

@SampleMemory
@SampleMessage
@SampleBorder
@SampleSourceCode
@Register(title = "基础组件演示", desc = "展示通过注解添加三种不同组件为示例添加不同的功能扩展")
class ComponentSampleActivity : AppCompatActivity() {
  private var index = 0
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val binding = ActivityComponentSampleBinding.inflate(layoutInflater)
    setContentView(binding.root)

    // Annotation above support two different functions

    // @SampleMemory will add memory panel to the sample
    // @SampleMessage will add message output panel
    binding.testButton.setOnClickListener {
      // This message will show up in message panel automatically
      println("Message from ComponentSampleActivity:${index++}")
    }
  }
}
