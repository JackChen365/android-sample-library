package com.cz.sample.activity

import android.Manifest
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.cz.android.sample.api.RefRegister
import com.cz.android.sample.api.Register
import com.cz.android.sample.library.appcompat.SampleAppCompatActivity
import com.cz.android.sample.library.function.permission.SamplePermission
import com.cz.sample.R
import com.cz.sample.component.SampleBorder

/**
 * @author :Created by cz
 * @date 2019-05-09 14:56
 * @email bingo110@126.com
 * 演示2 见配置
 */
@SampleBorder
@SamplePermission(Manifest.permission.CAMERA)
@RefRegister(category = R.string.animation,title=R.string.animation_sample2,desc = R.string.animation_sample_desc2)
class Sample2Activity : SampleAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo2)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

}
