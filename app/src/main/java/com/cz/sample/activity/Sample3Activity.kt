package com.cz.sample.activity

import android.os.Bundle
import com.cz.android.sample.api.RefRegister
import com.cz.android.sample.api.Register
import com.cz.android.sample.library.appcompat.SampleAppCompatActivity
import com.cz.sample.R
import kotlinx.android.synthetic.main.activity_demo3.*


/**
 * @author :Created by cz
 * @date 2019-05-09 14:56
 * @email bingo110@126.com
 * 演示3 见配置
 */
@RefRegister(category = R.string.animation,title=R.string.animation_sample3,desc = R.string.animation_sample_desc3)
class Sample3Activity: SampleAppCompatActivity(){
    companion object{
        private const val TAG="Sample3Activity"
    }

    /**
     * 计数
     */
    private var count=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo3)

        //点击输出日志
        testButton.setOnClickListener {
//            SampleLog.i(TAG,"Message:${count++}")
        }
    }
}