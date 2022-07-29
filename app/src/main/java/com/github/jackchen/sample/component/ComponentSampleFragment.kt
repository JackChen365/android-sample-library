package com.github.jackchen.sample.component

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.jackchen.android.sample.api.Register
import com.github.jackchen.android.sample.library.component.code.SampleSourceCode
import com.github.jackchen.android.sample.library.component.document.SampleDocument
import com.github.jackchen.android.sample.library.component.memory.SampleMemory
import com.github.jackchen.android.sample.library.component.message.SampleMessage
import com.github.jackchen.sample.databinding.FragmentComponentSampleBinding

@SampleMemory
@SampleMessage
@SampleDocument("documentSample.md")
@SampleSourceCode
@Register(title = "Fragment显示组件", desc = "演示在 Fragment 中展示所有基础组件")
class ComponentSampleFragment : Fragment() {
  private lateinit var binding: FragmentComponentSampleBinding
  private var index = 0
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    binding = FragmentComponentSampleBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    binding.testButton.setOnClickListener {
      println("Message from ComponentSampleActivity:${index++}")
    }
  }
}
