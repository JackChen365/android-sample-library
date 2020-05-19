package com.cz.sample.function

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cz.android.sample.api.RefCategory
import com.cz.android.sample.api.RefRegister
import com.cz.android.sample.library.function.permission.Permission
import com.cz.android.sample.library.function.permission.PermissionObserver
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
class SamplePermissionActivity : AppCompatActivity(),PermissionObserver{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_function_permission_sample)
    }

    override fun onGranted(permission: Permission) {
        val text = getString(R.string.permission_granted, permission.name)
        Toast.makeText(applicationContext,text,Toast.LENGTH_SHORT).show()
    }

    override fun onDenied(permission: Permission) {
        val text = getString(R.string.permission_denied, permission.name)
        Toast.makeText(applicationContext,text,Toast.LENGTH_SHORT).show()
    }
}
