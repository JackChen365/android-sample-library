package com.cz.sample.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cz.android.sample.api.RefRegister
import com.cz.android.sample.library.component.code.SampleSourceCode
import com.cz.android.sample.library.component.message.SampleMessage
import com.cz.sample.R
import kotlinx.android.synthetic.main.fragment_message_layout.*

/**
 * This sample demonstrated how to output message and show it to your sample
 * @see SampleSourceCode add additional panel that show all the source code
 */
@SampleMessage
@SampleSourceCode
@RefRegister(title=R.string.other_sample3,desc = R.string.other_sample3_desc,category = R.string.other)
class SampleMessageFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_message_layout, container, false);
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        outButton.setOnClickListener {
            println("System.out from fragment.")
        }
    }
}