package com.cz.sample.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cz.android.sample.api.RefRegister
import com.cz.android.sample.library.component.message.SampleMessage
import com.cz.sample.R
import kotlinx.android.synthetic.main.fragment_message_layout.*

/**
 * 示例 Fragment类型的Demo
 */
@SampleMessage
@RefRegister(title=R.string.other_sample1,desc = R.string.other_sample3_desc,category = R.string.other)
class SampleMessageFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_message_layout, container, false);
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        outButton.setOnClickListener {
            println("Message from fragment!\n")
        }

        outErrButton.setOnClickListener {
            System.err.println("Message from fragment!\n")
        }
    }
}