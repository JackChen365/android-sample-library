package com.cz.android.sample.library.component.code.view;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.annotation.WorkerThread;

import com.cz.android.sample.library.analysis.HtmlSource;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author Created by cz
 * @date 2020-01-27 22:07
 * @email bingo110@126.com
 * A web view that support display source code
 * Check resources: asset/highlight
 */
public class SourceCodeView extends WebView {
    public static final String BASE_URL="file:///android_asset/";
    private static final Executor threadExecutor = Executors.newSingleThreadExecutor();

    public SourceCodeView(Context context) {
        this(context,null,0);
    }

    public SourceCodeView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SourceCodeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOverScrollMode(View.OVER_SCROLL_IF_CONTENT_SCROLLS);
        WebSettings settings = getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
    }
    /**
     * Load Markdown from url to the view as rich formatted HTML. The
     * HTML output will be styled based on the given CSS file.
     *
     * @param url
     * - input in markdown format
     */
    public void loadSourceCodeFromUrl(final String url){
        final Context context = getContext().getApplicationContext();
       threadExecutor.execute(new Runnable() {
           @Override
           public void run() {
               final HtmlSource htmlSource=new HtmlSource();
               String source = htmlSource.getSource(context,url);
               loadSourceCode(context,source,url);
           }
       });
    }

    /**
     * Load source code by text,Here we will use highlight.js to display source code
     * see assets/highlight
     *
     */
    @WorkerThread
    private void loadSourceCode(Context context, String text,String url) {
        if(TextUtils.isEmpty(text)){
            post(new Runnable() {
                @Override
                public void run() {
                    loadUrl("about:blank");
                }
            });
        } else {
            // load source code by text
            try {
                AssetManager assets = context.getAssets();
                InputStream inputStream = assets.open("highlight/highlight_template.html");
                String templateSource = IOUtils.toString(inputStream, Charset.defaultCharset());
                int i = url.lastIndexOf(".");
                String language = url.substring(i+1);
                final String html=String.format(templateSource,language,Html.escapeHtml(text));
                post(new Runnable() {
                    @Override
                    public void run() {
                        loadDataWithBaseURL(BASE_URL, html, "text/html", "UTF-8", null);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
