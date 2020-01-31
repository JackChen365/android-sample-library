package com.cz.sample.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cz.android.sample.api.Register
import com.cz.android.sample.library.appcompat.SampleAppCompatFragment
import com.cz.android.sample.library.component.message.SampleMessage
import com.cz.sample.R
import kotlinx.android.synthetic.main.fragment_message_layout.*

/**
 * 示例 Fragment类型的Demo
 */
@SampleMessage
@Register(category = "Fragment",title="Message fragment",desc = "Fragment message function")
class SampleMessageFragment : SampleAppCompatFragment() {
    override fun onCreateView(container: ViewGroup?, inflater: LayoutInflater, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_message_layout, container, false)
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