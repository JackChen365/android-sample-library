package com.cz.sample

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.cz.android.sample.api.ProjectRepository
import com.cz.android.sample.library.main.SampleApplication

@ProjectRepository("https://raw.githubusercontent.com/momodae/AndroidSampleLibrary/master/app/src/main/java/")
class MainActivity : AppCompatActivity() {
    companion object{
        private const val TAG="MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val projectApplication = SampleApplication.getProjectApplication()
        projectApplication.androidSample.registerExceptionHandler { context, e, registerItem, item ->
            Log.e(TAG, "Exception occurs:" + e.message)
        }
    }
}
