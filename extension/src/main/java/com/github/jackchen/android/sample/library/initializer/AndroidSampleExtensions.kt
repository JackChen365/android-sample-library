package com.github.jackchen.android.sample.library.initializer

import android.content.Context
import com.github.jackchen.android.core.AndroidSample
import com.github.jackchen.android.sample.library.component.SamplePagerComponentContainer
import com.github.jackchen.android.sample.library.component.memory.SampleMemoryComponent
import com.github.jackchen.android.sample.library.component.message.SampleMessageComponent
import com.github.jackchen.android.sample.library.function.permission.SamplePermissionFunction

class AndroidSampleExtensions {

  fun initialize(context: Context) {
    val functionManager = AndroidSample.instance.getFunctionManager()
    functionManager.register(SamplePermissionFunction())

    val componentManager = AndroidSample.instance.getComponentManager()
    componentManager.register(SamplePagerComponentContainer())
    componentManager.register(SampleMemoryComponent())
    componentManager.register(SampleMessageComponent())
  }
}
