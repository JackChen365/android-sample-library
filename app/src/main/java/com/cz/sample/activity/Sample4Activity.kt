package com.cz.sample.activity

import android.os.Bundle
import com.cz.android.sample.api.Category
import com.cz.android.sample.api.Register
import com.cz.android.sample.library.appcompat.SampleAppCompatActivity
import com.cz.sample.R
import kotlinx.android.synthetic.main.activity_demo4.*

/**
 * @author :Created by cz
 * @date 2019-05-09 14:56
 * @email bingo110@126.com
 * 演示4,见演示配置
 */
@Register(title = "源码查看",desc = "查看 java 源码")
class Sample4Activity : SampleAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo4)
        sourceCodeView.loadSourceCodeFromUrl("https://raw.githubusercontent.com/momodae/SuperTextView/master/library/src/main/java/com/cz/widget/supertextview/library/Styled.java")
    }
}
