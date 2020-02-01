package com.cz.sample.test.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cz.android.sample.library.component.code.adapter.SampleTreeAdapter;
import com.cz.sample.R;

import java.io.File;

/**
 * @author Created by cz
 * @date 2020-02-01 09:27
 * @email bingo110@126.com
 */
public class SampleFileAdapter extends SampleTreeAdapter<File> {
    private final LayoutInflater layoutInflater;

    public SampleFileAdapter(Context context, TreeNode<File> rootNode) {
        super(context,rootNode);
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View onCreateView(ViewGroup viewGroup,int itemViewType,int position) {
        return layoutInflater.inflate(R.layout.sample_list_folder_item,viewGroup,false);
    }

    @Override
    public void onBindView(View view, TreeNode<File> node, File file, int viewType, int position) {
        ImageView imageFlagView=view.findViewById(R.id.imageFlagView);
        ImageView imageFileType=view.findViewById(R.id.imageFileType);
        TextView fileName=view.findViewById(R.id.fileName);
        imageFlagView.setVisibility(file.isDirectory()?View.VISIBLE:View.INVISIBLE);
        fileName.setText(file.getName());
        if(file.isDirectory()){
            imageFlagView.setSelected(node.isExpand);
            imageFileType.setImageResource(R.drawable.sample_source_folder);
        } else {
            imageFileType.setImageResource(R.drawable.sample_file_any_type);
        }
    }
}
