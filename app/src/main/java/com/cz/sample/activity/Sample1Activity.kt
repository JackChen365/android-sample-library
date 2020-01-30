package com.cz.sample.activity

import android.os.Bundle
import com.cz.android.sample.api.Category
import com.cz.android.sample.api.RefCategory
import com.cz.android.sample.api.RefRegister
import com.cz.android.sample.api.Register
import com.cz.android.sample.library.appcompat.SampleAppCompatActivity
import com.cz.android.sample.library.component.memory.SampleMemory
import com.cz.android.sample.library.component.message.SampleMessage
import com.cz.sample.R

/**
 * @author :Created by cz
 * @date 2019-05-09 14:56
 * @email bingo110@126.com
 * 演示1 见配置
 */
@SampleMessage
@SampleMemory
@RefCategory(title=R.string.animation,desc = R.string.animation_desc)
@RefRegister(category = R.string.animation,title=R.string.animation_sample1,desc = R.string.animation_sample_desc1)
class Sample1Activity : SampleAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo1)
    }

}
