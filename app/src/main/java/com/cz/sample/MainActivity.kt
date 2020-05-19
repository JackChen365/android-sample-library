package com.cz.sample

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.cz.android.sample.library.main.SampleApplication

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
