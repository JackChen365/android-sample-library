package com.tubi.android.testapp.sample

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.cz.android.sample.api.Register
import com.tubi.android.testapp.R

@Register(title="activity sample")
class ComponentListSampleActivity : AppCompatActivity() {
    private var index=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_component_list_sample)
        findViewById<View>(R.id.testButton).setOnClickListener {
            //This message will show up in message panel automatically
            println("Message from ComponentListSampleActivity:${index++}\n")
        }
    }
}
