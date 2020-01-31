package com.cz.sample

import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import com.cz.android.sample.api.ProjectRepository

@ProjectRepository("https://raw.githubusercontent.com/momodae/AndroidSampleLibrary/master/app/src/main/java/")
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
