package com.github.jackchen.sample.function

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.jackchen.android.sample.api.Register
import com.github.jackchen.android.sample.library.function.permission.SamplePermission
import com.github.jackchen.android.sample.library.function.permission.SamplePermissionFunction
import com.github.jackchen.android.sample.library.function.permission.addPermissionObserver
import com.github.jackchen.sample.R

/**
 * @author :Created by cz
 * @date 2019-05-09 14:56
 * @email bingo110@126.com
 * @see SamplePermission This annotation allow you request runtime permission easily
 * @see SamplePermissionFunction This function implementation extend from SampleFunction
 */
@SamplePermission(
  Manifest.permission.CAMERA,
  Manifest.permission.WRITE_EXTERNAL_STORAGE
)
@Register(title = "权限功能", desc = "演示动行时权限动态扩展功能")
class SamplePermissionActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_function_permission_sample)
    addPermissionObserver { result ->
      if (result.granted) {
        val text = getString(R.string.permission_granted, result.name)
        Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
      } else {
        val text = getString(R.string.permission_denied, result.name)
        Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
      }
    }
  }
}
