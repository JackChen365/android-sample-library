package com.cz.android.sample.library.component.code;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cz.android.sample.library.R;
import com.cz.android.sample.library.component.code.view.SourceCodeView;
import com.cz.android.sample.library.view.WebViewProgressBar;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

/**
 * @author Created by cz
 * @date 2020-01-28 19:48
 * @email bingo110@126.com
 */
public class SampleSourceCodeDialogFragment extends BottomSheetDialogFragment {
    private final static String SAMPLE_FILE_URL ="fileUrl";

    public static BottomSheetDialogFragment newInstance(String url){
        Bundle argument=new Bundle();
        argument.putString(SAMPLE_FILE_URL,url);
        BottomSheetDialogFragment fragment=new SampleSourceCodeDialogFragment();
        fragment.setArguments(argument);
        return fragment;
    }

    private SampleSourceCodeDialogFragment(){
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.sample_fragment_source_code, container, false);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.height = Resources.getSystem().getDisplayMetrics().heightPixels;
        contentView.setLayoutParams(layoutParams);
        return contentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle arguments = getArguments();
        String fileUrl = arguments.getString(SAMPLE_FILE_URL);
        SourceCodeView sampleSourceCodeView=view.findViewById(R.id.sampleSourceCodeView);
        sampleSourceCodeView.loadSourceCodeFromUrl(fileUrl);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View view = getView();
        SourceCodeView sampleSourceCodeView=view.findViewById(R.id.sampleSourceCodeView);
        final WebViewProgressBar sampleProgressBar=view.findViewById(R.id.sampleProgressBar);
        sampleProgressBar.startProgressAnim();
        sampleProgressBar.setOnProgressListener(new WebViewProgressBar.OnProgressListener() {
            @Override
            public void onLoadFinished(View v) {
                v.animate().alpha(0f);
            }
        });
        sampleSourceCodeView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if(null!=sampleProgressBar&&newProgress >= sampleProgressBar.getFirstProgress()){
                    sampleProgressBar.passAnimation();
                }
            }
        });
    }

}
