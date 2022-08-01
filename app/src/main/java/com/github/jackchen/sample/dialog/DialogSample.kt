package com.github.jackchen.sample.dialog

import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.github.jackchen.android.core.main.sample.SampleInterface
import com.github.jackchen.android.sample.api.Register
import com.github.jackchen.sample.custom.processor.AlertDialogActionProcessor

/**
 * @author Created by cz
 * @date 2020-01-29 21:31
 * @email bingo110@126.com
 * @see AlertDialogActionProcessor Our own action processor
 */
@Suppress("unused")
class DialogSample {
  @Register(title = "登陆提示", desc = "一个简单的登陆提示Dialog示例")
  class DialogSample1 : SampleInterface<AlertDialog?> {
    override fun getObject(context: AppCompatActivity): AlertDialog {
      val builder = AlertDialog.Builder(context)
      builder.setTitle("Login Alert")
        .setMessage("Are you sure, you want to continue ?")
        .setCancelable(false)
        .setPositiveButton("Yes") { dialog, which ->
          Toast.makeText(
            context,
            "Selected Option: YES",
            Toast.LENGTH_SHORT
          ).show()
        }
        .setNegativeButton("No") { dialog, which ->
          Toast.makeText(
            context,
            "Selected Option: No",
            Toast.LENGTH_SHORT
          ).show()
        }
      // Creating dialog box
      return builder.create()
    }
  }

  @Register(title = "选择颜色", desc = "一个简单的多选Dialog示例...")
  class DialogSample2 : SampleInterface<AlertDialog?> {
    private val colors = arrayOf<CharSequence>("Pink", "Red", "Yellow", "Blue")
    private val selectList: MutableList<Int> = ArrayList()
    private val icount = BooleanArray(colors.size)
    override fun getObject(context: AppCompatActivity): AlertDialog {
      val builder = AlertDialog.Builder(context)
      builder.setTitle("Choose Colors")
        .setMultiChoiceItems(colors, icount) { arg0, arg1, arg2 ->
          if (arg2) {
            // If user select a item then add it in selected items
            selectList.add(arg1)
          } else if (selectList.contains(arg1)) {
            // if the item is already selected then remove it
            selectList.remove(Integer.valueOf(arg1))
          }
        }.setCancelable(false)
        .setPositiveButton("Yes") { dialog, which ->
          var msg = ""
          for (i in selectList.indices) {
            msg = "$msg${i + 1} : ${colors[selectList[i]]}"
          }
          Toast.makeText(
            context, "Total ${selectList.size} Items Selected.$msg",
            Toast.LENGTH_SHORT
          ).show()
        }
        .setNegativeButton("No") { dialog, which ->
          Toast.makeText(
            context,
            "No Option Selected",
            Toast.LENGTH_SHORT
          ).show()
        }
      // Creating dialog box
      return builder.create()
    }
  }
}
