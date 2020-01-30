package com.cz.android.sample.library.component.document;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.cz.android.sample.library.AndroidSample;
import com.cz.android.sample.library.R;

/**
 * @author Created by cz
 * @date 2020-01-28 19:48
 * @email bingo110@126.com
 */
public class DocumentFragment extends Fragment {
    private final static String URL="url";

    public static Fragment newInstance(String url){
        Bundle argument=new Bundle();
        argument.putString(URL,url);
        Fragment fragment=new DocumentFragment();
        fragment.setArguments(argument);
        return fragment;
    }

    private DocumentFragment(){
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
        FragmentActivity activity = getActivity();
        MarkdownView markdownView=activity.findViewById(R.id.sampleMarkdownView);
        final WebViewProgressBar sampleProgressBar=activity.findViewById(R.id.sampleProgressBar);
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
            final WebViewProgressBar sampleProgressBar=activity.findViewById(R.id.sampleProgressBar);
            //检测文档地址
            if(documentUrl.startsWith("http")){
                //远程地址
                markdownView.loadMarkdownFromUrl(documentUrl);
            } else if(documentUrl.startsWith("assets://")){
                //资产目录地址
                documentUrl=documentUrl.substring("assets://".length());
                //加载markdown
                markdownView.loadMarkdownFromAssets(documentUrl,null);
            } else {
                //相对路径地址
//                String repositoryUrl = AndroidSample.getInstance().getRepositoryUrl();
//                if(null==repositoryUrl){
//                    //warning
//                } else {
//                    val documentPackagePath = TemplateConfiguration.getDocumentPath(className,documentUrl)
//                    if(null!=documentPackagePath){
//                        //动态获取相对路径
//                        documentUrl=repositoryUrl+documentPackagePath;
//                    } else {
//                        //直接拼接
//                        documentUrl=serverUrl+documentUrl
//                    }
//                    //网络加载
//                    markdownView.loadUrl(documentUrl)
//                }
            }
        }
    }

}
