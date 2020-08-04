package com.cz.sample.function

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.cz.android.sample.api.RefRegister
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
@SamplePermission(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
@RefRegister(title=R.string.function_permission_sample2,desc = R.string.function_permission_sample2_desc,category = R.string.sample_function)
class SamplePermissionFunctionFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_function_permission_sample, container, false);
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        PermissionViewModelProviders.getViewModel(this).addObserver { result->
            if(result.granted){
                val text = getString(R.string.permission_granted, result.name)
                Toast.makeText(context,text, Toast.LENGTH_SHORT).show()
            } else {
                val text = getString(R.string.permission_denied, result.name)
                Toast.makeText(context,text, Toast.LENGTH_SHORT).show()
            }
        }
    }

}