package com.cz.sample.component

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cz.android.sample.api.Register
import com.cz.android.sample.library.component.code.SampleSourceCode
import com.cz.sample.R

@SampleSourceCode
@Register(title="源码组件",desc="展示如何为示例添加源码查看组件",path = "组件示例")
class ComponentSourceSampleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_component_source_code_sample)
    }
}
