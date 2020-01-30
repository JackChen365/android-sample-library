package com.cz.android.sample.core;

import com.cz.android.sample.analysis.image.ImageAnalyzer;
import com.cz.android.sample.analysis.image.ImageSource;

import org.junit.Test;

import java.util.List;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ImageAnalyzerTest {
    @Test
    public void downloadPicture() {
        ImageAnalyzer imageAnalyzer = new ImageAnalyzer();
        imageAnalyzer.setDataSource(new ImageSource());
        List<String> imageList = imageAnalyzer.analysis("https://tieba.baidu.com/p/4991388321");
        for(String url:imageList){
            System.out.println(url);
        }
        assert null!=imageList;
    }
}