package com.github.jackchen.sample.test

import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import com.github.jackchen.android.sample.api.Register
import com.github.jackchen.android.sample.library.component.code.view.SourceCodeView
import com.github.jackchen.android.sample.library.component.document.DocumentAssetsManager
import com.github.jackchen.sample.databinding.ActivitySourceCodeSampleBinding

/**
 * @author Created by cz
 * @date 2019-05-09 14:56
 * @email bingo110@126.com
 * @see SourceCodeView A webView that responsible for demonstrate source code.
 */
@Register(title = "演示源码加载", desc = "演示源码加载,高度展示")
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
                if (null != binding.sampleProgressBar && newProgress >= binding.sampleProgressBar.firstProgress) {
                    binding.sampleProgressBar.passAnimation()
                }
            }
        }
        binding.sourceCodeView.loadSourceCodeFromAssets(this, "MutableListAdapter.kt")
    }
}
