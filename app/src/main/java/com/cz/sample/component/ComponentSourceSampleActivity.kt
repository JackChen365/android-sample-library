package com.cz.sample.component

import android.os.Bundle
import com.cz.android.sample.api.RefRegister
import com.cz.android.sample.library.appcompat.SampleAppCompatActivity
import com.cz.android.sample.library.component.code.SampleSourceCode
import com.cz.sample.R

@SampleSourceCode
@RefRegister(title=R.string.component_sample3,desc=R.string.component_sample3_desc,category = R.string.component_category,priority = 2)
class ComponentSourceSampleActivity : SampleAppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_component_source_code_sample)
    }
}
