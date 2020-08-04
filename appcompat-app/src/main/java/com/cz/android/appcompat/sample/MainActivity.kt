package com.cz.android.appcompat.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.cz.android.sample.library.main.SampleApplication
import com.cz.sample.R
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    companion object{
        private const val TAG="MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val projectApplication = SampleApplication.getProjectApplication()
        projectApplication.androidSample.registerExceptionHandler { _, e, _, _ ->
            Log.e(TAG, "Exception occurs:" + e.message)
        }
    }
}
