package com.cz.sample.function

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.cz.android.sample.api.RefCategory
import com.cz.android.sample.api.RefRegister
import com.cz.android.sample.library.function.permission.PermissionViewModel
import com.cz.android.sample.library.function.permission.PermissionViewModelProviders
import com.cz.android.sample.library.function.permission.SamplePermission
import com.cz.sample.R

/**
 * @author :Created by cz
 * @date 2019-05-09 14:56
 * @email bingo110@126.com
 * @see SamplePermission This annotation allow you request runtime permission easily
 * @see com.cz.android.sample.library.function.permission.SamplePermissionFunction This function implementation extend from SampleFunction
 * @see com.cz.android.sample.library.function.permission.PermissionObserver This observer responsible for receiving permission request result
 */
@SamplePermission(Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE)
@RefCategory(title=R.string.sample_function,desc = R.string.sample_function_description,priority = 2)
@RefRegister(title=R.string.function_permission_sample1,desc = R.string.function_permission_sample1_desc,category = R.string.sample_function)
class SamplePermissionActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_function_permission_sample)
        PermissionViewModelProviders.getViewModel(this).addObserver { result->
            if(result.granted){
                val text = getString(R.string.permission_granted, result.name)
                Toast.makeText(applicationContext,text, Toast.LENGTH_SHORT).show()
            } else {
                val text = getString(R.string.permission_denied, result.name)
                Toast.makeText(applicationContext,text, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
