package com.github.jackchen.android.sample.library.component.code;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.jackchen.android.sample.library.R;
import com.github.jackchen.android.sample.library.component.code.adapter.SampleSourceCodeAdapter;
import com.cz.widget.recyclerview.adapter.listener.OnTreeNodeClickListener;
import com.cz.widget.recyclerview.adapter.support.tree.TreeNode;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.IOException;

/**
 * @author Created by cz
 * @date 2020-01-28 19:48
 * @email bingo110@126.com
 */
public class SampleSourceFileFragmentListFragment extends Fragment {
    private final static String SAMPLE_PACKAGE_NAME = "samplePackage";
    private final static String SAMPLE_FILE_FILTER_NAME = "sampleFileFilter";
    private final static String SAMPLE_SOURCE_DIALOG = "sample_source_dialog";

    private final SparseArray<BottomSheetDialogFragment> cachedDialogFragments = new SparseArray<>();

    public static Fragment newInstance(String samplePackageName, String filter) {
        Bundle argument = new Bundle();
        argument.putString(SAMPLE_PACKAGE_NAME, samplePackageName);
        argument.putString(SAMPLE_FILE_FILTER_NAME, filter);
        Fragment fragment = new SampleSourceFileFragmentListFragment();
        fragment.setArguments(argument);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sample_fragment_source_code_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Context context = getContext();
        Bundle arguments = getArguments();
        String packageName = arguments.getString(SAMPLE_PACKAGE_NAME);
        String filter = arguments.getString(SAMPLE_FILE_FILTER_NAME);
        RecyclerView sampleSourceCodeList = view.findViewById(R.id.sampleSourceCodeList);

        TreeNode<String> rootNode = new TreeNode<>(null);
        AssetManager assets = context.getAssets();
        String[] filterFileArray = null;
        if (!TextUtils.isEmpty(filter)) {
            filterFileArray = filter.split("\\|");
        }
        buildFileTree(assets, rootNode, packageName.replace('.', '/'), filterFileArray);
        final SampleSourceCodeAdapter sampleSourceCodeAdapter = new SampleSourceCodeAdapter(context, rootNode);

        sampleSourceCodeList.setLayoutManager(new LinearLayoutManager(context));
        sampleSourceCodeList.addItemDecoration(new SampleFileItemDecoration(context));
        sampleSourceCodeList.setAdapter(sampleSourceCodeAdapter);
        sampleSourceCodeAdapter.expandAll();
        sampleSourceCodeAdapter.setOnTreeNodeClickListener(new OnTreeNodeClickListener<String>() {
            @Override
            public void onNodeItemClick(@Nullable TreeNode<String> node, @Nullable String item, @Nullable View v,
                    int i) {
                String filePath = sampleSourceCodeAdapter.getItem(i);
                FragmentActivity activity = getActivity();
                FragmentManager supportFragmentManager = activity.getSupportFragmentManager();
                BottomSheetDialogFragment bottomSheetDialogFragment = cachedDialogFragments.get(i);
                if (null != bottomSheetDialogFragment) {
                    bottomSheetDialogFragment.show(supportFragmentManager, SAMPLE_SOURCE_DIALOG);
                } else {
                    BottomSheetDialogFragment dialogFragment = SampleSourceCodeDialogFragment.newInstance(filePath);
                    cachedDialogFragments.put(i, dialogFragment);
                    dialogFragment.show(supportFragmentManager, SAMPLE_SOURCE_DIALOG);
                }
            }
        });
    }

    private void buildFileTree(AssetManager assets, TreeNode<String> parentNode, String path,
            @Nullable String[] filterFileArray) {
        try {
            String[] fileList = assets.list(path);
            if (null != fileList) {
                for (String filePath : fileList) {
                    String classFilePath = path + "/" + filePath;
                    if (null == filterFileArray) {
                        TreeNode<String> childNode = new TreeNode<>(parentNode, classFilePath);
                        parentNode.children.add(childNode);
                        buildFileTree(assets, childNode, classFilePath, null);
                    } else {
                        boolean filterFile = false;
                        for (String filterPath : filterFileArray) {
                            if (filePath.contains(filterPath)) {
                                filterFile = true;
                                break;
                            }
                        }
                        if (!filterFile) {
                            TreeNode<String> childNode = new TreeNode<>(parentNode, classFilePath);
                            parentNode.children.add(childNode);
                            buildFileTree(assets, childNode, classFilePath, filterFileArray);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDetach() {
        cachedDialogFragments.clear();
        super.onDetach();
    }
}
