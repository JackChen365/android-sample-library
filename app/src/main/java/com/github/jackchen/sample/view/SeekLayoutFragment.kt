package com.github.jackchen.sample.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.jackchen.android.sample.api.Register
import com.github.jackchen.android.sample.library.component.message.SampleMessage
import com.github.jackchen.android.sample.library.view.SeekLayout
import com.github.jackchen.sample.R

/**
 * @author airsaid
 */
@Register(
  title = "SeekLayout",
  desc = "Layout with TextView and SeekBar for fast development."
)
@SampleMessage
class SeekLayoutFragment : Fragment() {

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_seek_layout, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    view.findViewById<SeekLayout>(R.id.seekLayout).apply {
      onSeekBarChangeListener = { _, progress, fromUser ->
        println("onSeekBarChangeListener progress: $progress, fromUser: $fromUser")
      }
    }
  }
}