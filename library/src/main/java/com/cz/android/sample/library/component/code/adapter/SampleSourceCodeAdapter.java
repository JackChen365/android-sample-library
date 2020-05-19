package com.cz.android.sample.library.component.code.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.cz.android.sample.library.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Created by cz
 * @date 2020-01-31 11:52
 * @email bingo110@126.com
 */
public class SampleSourceCodeAdapter extends BaseAdapter {
    private final LayoutInflater layoutInflater;
    private final List<String> repositoryFileList;

    public SampleSourceCodeAdapter(@NonNull Context context,@NonNull List<String> fileList) {
        this.layoutInflater = LayoutInflater.from(context);
        this.repositoryFileList=new ArrayList<>();
        if(null!=fileList){
            this.repositoryFileList.addAll(fileList);
        }
    }

    @Override
    public int getCount() {
        return this.repositoryFileList.size();
    }

    @Override
    public String getItem(int i) {
        return this.repositoryFileList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(null==view){
            view=layoutInflater.inflate(R.layout.sample_file_list_item,viewGroup,false);
        }
        TextView text1=view.findViewById(android.R.id.text1);
        String item = getItem(i);
        int index = item.lastIndexOf("/");
        String fileName = item.substring(index + 1);
        text1.setText(fileName);
        return view;
    }
}
