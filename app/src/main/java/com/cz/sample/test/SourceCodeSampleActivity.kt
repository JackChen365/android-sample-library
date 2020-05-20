package com.cz.sample.test

import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import com.cz.android.sample.api.RefRegister
import com.cz.sample.R
import kotlinx.android.synthetic.main.activity_source_code_sample.*

/**
 * @author :Created by cz
 * @date 2019-05-09 14:56
 * @email bingo110@126.com
 * @see com.cz.android.sample.library.component.code.view.SourceCodeView A webView that responsible for demonstrate source code.
 */
@RefRegister(title = R.string.other_sample2,desc=R.string.other_sample2_desc,category = R.string.other)
class SourceCodeSampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_source_code_sample)

        sampleProgressBar.startProgressAnim()
        sampleProgressBar.setOnProgressListener { v -> v.animate().alpha(0f) }
        sourceCodeView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                if (null != sampleProgressBar && newProgress >= sampleProgressBar!!.getFirstProgress()) {
                    sampleProgressBar!!.passAnimation()
                }
            }
        }
        sourceCodeView.loadSourceCodeFromUrl("https://raw.githubusercontent.com/momodae/SuperTextView/master/library/src/main/java/com/cz/widget/supertextview/library/Styled.java")
    }
}
