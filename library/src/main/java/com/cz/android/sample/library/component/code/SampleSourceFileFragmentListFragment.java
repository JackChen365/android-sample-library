package com.cz.android.sample.library.component.code;

import android.content.Context;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.cz.android.sample.library.R;
import com.cz.android.sample.library.component.code.adapter.SampleSourceCodeAdapter;
import com.cz.android.sample.library.file.SampleProjectFileSystemManager;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

/**
 * @author Created by cz
 * @date 2020-01-28 19:48
 * @email bingo110@126.com
 */
public class SampleSourceFileFragmentListFragment extends Fragment {
    private final static String SAMPLE_PACKAGE_NAME ="samplePackage";
    private final static String SAMPLE_FILE_FILTER_NAME ="sampleFileFilter";
    private final static String SAMPLE_SOURCE_DIALOG="sample_source_dialog";

    private final SparseArray<BottomSheetDialogFragment> cachedDialogFragments=new SparseArray<>();

    public static Fragment newInstance(String samplePackageName,String filterRegex){
        Bundle argument=new Bundle();
        argument.putString(SAMPLE_PACKAGE_NAME,samplePackageName);
        argument.putString(SAMPLE_FILE_FILTER_NAME,filterRegex);
        Fragment fragment=new SampleSourceFileFragmentListFragment();
        fragment.setArguments(argument);
        return fragment;
    }

    private SampleSourceFileFragmentListFragment(){
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sample_fragment_source_code_list,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Context context = getContext();
        Bundle arguments = getArguments();
        String packageName = arguments.getString(SAMPLE_PACKAGE_NAME);
        String fileFilter = arguments.getString(SAMPLE_FILE_FILTER_NAME);
        ListView sampleSourceCodeList=view.findViewById(R.id.sampleSourceCodeList);
        SampleProjectFileSystemManager fileSystemManager = SampleProjectFileSystemManager.getInstance();
        List<String> projectFileList = fileSystemManager.getProjectFileList(packageName,fileFilter);
        final SampleSourceCodeAdapter sampleSourceCodeAdapter = new SampleSourceCodeAdapter(context, projectFileList);
        sampleSourceCodeList.setAdapter(sampleSourceCodeAdapter);
        sampleSourceCodeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String fileUrl = sampleSourceCodeAdapter.getItem(i);
                FragmentActivity activity = getActivity();
                FragmentManager supportFragmentManager = activity.getSupportFragmentManager();
                BottomSheetDialogFragment bottomSheetDialogFragment = cachedDialogFragments.get(i);
                if(null!=bottomSheetDialogFragment){
                    bottomSheetDialogFragment.show(supportFragmentManager,SAMPLE_SOURCE_DIALOG);
                } else{
                    BottomSheetDialogFragment dialogFragment = SampleSourceCodeDialogFragment.newInstance(fileUrl);
                    cachedDialogFragments.put(i,dialogFragment);
                    dialogFragment.show(supportFragmentManager,SAMPLE_SOURCE_DIALOG);
                }
            }
        });
    }

    @Override
    public void onDetach() {
        cachedDialogFragments.clear();
        super.onDetach();
    }
}
