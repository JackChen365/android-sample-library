package com.cz.android.appcompat.sample.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.cz.android.sample.api.RefCategory;
import com.cz.android.sample.api.RefRegister;
import com.cz.android.sample.library.sample.SampleObject;
import com.cz.sample.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Created by cz
 * @date 2020-01-29 21:31
 * @email bingo110@126.com
 * @see com.cz.android.appcompat.sample.custom.processor.AlertDialogActionProcessor Our own action processor
 */
@RefCategory(title= R.string.dialog,desc=R.string.dialog_desc)
public class DialogSample{

    public DialogSample() {
    }

    @RefRegister(title = R.string.dialog_sample1,desc =R.string.dialog_sample1_desc,category = R.string.dialog)
    public class DialogSample1 implements SampleObject {
        @Override
        public Object getObject(final Context context) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Login Alert")
                    .setMessage("Are you sure, you want to continue ?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(context,"Selected Option: YES",Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(context,"Selected Option: No",Toast.LENGTH_SHORT).show();
                        }
                    });
            //Creating dialog box
            return builder.create();
        }
    }

    @RefRegister(title = R.string.dialog_sample2,desc =R.string.dialog_sample2_desc,category = R.string.dialog)
    public class DialogSample2 implements SampleObject {
        final CharSequence[] colors = { "Pink", "Red", "Yellow", "Blue" };
        private List<Integer> selectList=new ArrayList<>();
        private boolean icount[] = new boolean[colors.length];

        @Override
        public Object getObject(final Context context) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Choose Colors")
                    .setMultiChoiceItems(colors,icount, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1, boolean arg2) {
                            if (arg2) {
                                // If user select a item then add it in selected items
                                selectList.add(arg1);
                            } else if (selectList.contains(arg1)) {
                                // if the item is already selected then remove it
                                selectList.remove(Integer.valueOf(arg1));
                            }
                        }
                    })      .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String msg = "";
                            for (int i = 0; i < selectList.size(); i++) {
                                msg = msg + "\n" + (i + 1) + " : " + colors[selectList.get(i)];
                            }
                            Toast.makeText(context, "Total " + selectList.size() + " Items Selected.\n" + msg, Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(context,"No Option Selected",Toast.LENGTH_SHORT).show();
                        }
                    });
            //Creating dialog box
            return builder.create();
        }
    }

}
