package com.cz.android.sample.library.component.document;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.cz.android.sample.library.R;
import com.cz.android.sample.library.component.document.view.MarkdownView;
import com.cz.android.sample.library.view.WebViewProgressBar;
import com.cz.android.sample.library.file.SampleProjectFileSystemManager;

/**
 * @author Created by cz
 * @date 2020-01-28 19:48
 * @email bingo110@126.com
 */
public class SampleDocumentFragment extends Fragment {
    private final static String URL="url";

    public static Fragment newInstance(String url){
        Bundle argument=new Bundle();
        argument.putString(URL,url);
        Fragment fragment=new SampleDocumentFragment();
        fragment.setArguments(argument);
        return fragment;
    }

    private SampleDocumentFragment(){
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sample_fragment_document,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //初始化markdown
        Bundle arguments = getArguments();
        String url = arguments.getString(URL);
        if(null!=url){
            //设置base_url session
            initLoadProgress();
            initMarkdown(url);
        }
    }

    /**
     * 初始化markdown显示进度
     */
    private void initLoadProgress() {
        //关联进度显示
        View view = getView();
        MarkdownView markdownView=view.findViewById(R.id.sampleMarkdownView);
        final WebViewProgressBar sampleProgressBar=view.findViewById(R.id.sampleProgressBar);
        sampleProgressBar.startProgressAnim();
        sampleProgressBar.setOnProgressListener(new WebViewProgressBar.OnProgressListener() {
            @Override
            public void onLoadFinished(View v) {
                v.animate().alpha(0f);
            }
        });
        markdownView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if(null!=sampleProgressBar&&newProgress >= sampleProgressBar.getFirstProgress()){
                    sampleProgressBar.passAnimation();
                }
            }
        });
    }

    /**
     * 初始化markdown
     */
    private void initMarkdown(String documentUrl) {
        FragmentActivity activity = getActivity();
        MarkdownView markdownView=activity.findViewById(R.id.sampleMarkdownView);
        if (TextUtils.isEmpty(documentUrl)) {
            markdownView.loadUrl("about:blank");
        } else {
            if(documentUrl.startsWith("http")){
                markdownView.loadMarkdownFromUrl(documentUrl);
            } else if(documentUrl.startsWith("assets://")){
                documentUrl=documentUrl.substring("assets://".length());
                markdownView.loadMarkdownFromAssets(documentUrl,null);
            } else {
                SampleProjectFileSystemManager projectFileSystemManager = SampleProjectFileSystemManager.getInstance();
                String repositoryUrl = projectFileSystemManager.getRepositoryUrl();
                if(null==repositoryUrl){
                    //warning
                } else {
                    if(repositoryUrl.endsWith("/")){
                        markdownView.loadUrl(repositoryUrl+documentUrl);
                    } else {
                        markdownView.loadUrl(repositoryUrl+"/"+documentUrl);
                    }
                }
            }
        }
    }

}
