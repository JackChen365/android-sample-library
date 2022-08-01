package com.github.jackchen.android.core.function

import androidx.appcompat.app.AppCompatActivity
import com.github.jackchen.android.core.extension.ExtensionHandler
import com.github.jackchen.android.sample.api.SampleItem
import java.util.ArrayList

/**
 * @author Created by cz
 * @date 2020-01-27 18:08
 * @email bingo110@126.com
 * @see SampleFunction
 */
object FunctionManager : ExtensionHandler<SampleFunction> {
  private val FUNCTION_CLASS_DESC = SampleFunction::class.java.name.replace('.', '/')

  /**
   * all the action plugin
   */
  private val functionList: MutableList<SampleFunction> = ArrayList()
  override fun handle(
    className: String,
    superClass: String,
    interfaces: List<String>
  ): Boolean {
    if (interfaces.contains(FUNCTION_CLASS_DESC)) {
      try {
        val clazz = Class.forName(className)
        val function = clazz.newInstance() as SampleFunction
        register(function)
      } catch (e: Exception) {
        e.printStackTrace()
      }
      return true
    }
    return false
  }

  /**
   * Register a plugin. It will put new plugin to list.
   *
   * @param plugin
   */
  override fun register(plugin: SampleFunction) {
    functionList.add(plugin)
  }

  /**
   * unregister a plugin, It will remove the plugin from list.
   *
   * @param plugin
   */
  override fun unregister(plugin: SampleFunction) {
    functionList.remove(plugin)
  }

  fun getFunctionList(): List<SampleFunction> {
    return functionList
  }

  /**
   * execute
   *
   * @param context
   * @param item
   */
  fun execute(context: AppCompatActivity, item: SampleItem) {
    for (function in functionList) {
      val clazz = item.clazz()
      if (function.isAvailable(clazz)) {
        function.execute(context, item)
      }
    }
  }
}
