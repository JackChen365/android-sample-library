package com.cz.sample.activity

import android.os.Bundle
import com.cz.android.sample.api.Register
import com.cz.android.sample.library.appcompat.SampleAppCompatActivity
import com.cz.sample.R

/**
 * 控件测试
 */
@Register(title = "控件性能测试",desc = "演示常规性能测试",priority = -1)
class TestViewSampleActivity : SampleAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_view_sample)

//        ViewTrace.trace(testTextView)
//        //设置显示消息类型
//        ViewTrace.setMessageFlag(this,ViewTrace.MEASURE)
//        //演示文本性能
//        val text=assets.open("text/text_chapter1").bufferedReader().readText()
//        testTextView.text=text
    }
}
