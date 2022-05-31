package com.github.jackchen.sample.component

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.jackchen.android.sample.api.Register
import com.github.jackchen.android.sample.library.component.code.SampleSourceCode
import com.github.jackchen.sample.R

@SampleSourceCode
@Register(title = "源码组件", desc = "展示如何为示例添加源码查看组件")
class ComponentSourceSampleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_component_source_code_sample)
    }
}
