package com.cz.sample.test

import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import com.cz.android.sample.api.Register
import com.cz.sample.databinding.ActivitySourceCodeSampleBinding

/**
 * @author :Created by cz
 * @date 2019-05-09 14:56
 * @email bingo110@126.com
 * @see com.cz.android.sample.library.component.code.view.SourceCodeView A webView that responsible for demonstrate source code.
 */
@Register(title = "title", desc = "演示源码加载,高度展示", path = "其他")
class SourceCodeSampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySourceCodeSampleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.sampleProgressBar.startProgressAnim()
        binding.sampleProgressBar.setOnProgressListener { v -> v.animate().alpha(0f) }
        binding.sourceCodeView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                if (null != binding.sampleProgressBar && newProgress >= binding.sampleProgressBar!!.getFirstProgress()) {
                    binding.sampleProgressBar!!.passAnimation()
                }
            }
        }
        binding.sourceCodeView.loadSourceCodeFromUrl("https://raw.githubusercontent.com/momodae/SuperTextView/master/library/src/main/java/com/cz/widget/supertextview/library/Styled.java")
    }
}
