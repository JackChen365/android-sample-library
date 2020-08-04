package com.cz.android.sample.library.analysis;

import com.cz.android.sample.analysis.AbsAnalyzer;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Created by cz
 * @date 2020-01-27 14:35
 * @email bingo110@126.com
 * An image analyzer to process html source code and return image url
 * Example usage:
 * <pre>
 *         ImageAnalyzer imageAnalyzer = new ImageAnalyzer();
 *         imageAnalyzer.setDataSource(new ImageSource());
 *         List<String> imageList = imageAnalyzer.analysis("https://tieba.baidu.com/p/4991388321");
 *         assert null!=imageList;
 * </pre>
 */
public class ImageAnalyzer extends AbsAnalyzer<String,String, List<String>> {

    @Override
    protected List<String> analysisSource(String params) {
        List<String> imageList = new ArrayList<>();
        String regex = "\"(https?://[^<>{}]*?\\.(jpg|png))\"";
        Pattern compile = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher pattern = compile.matcher(params);
        while (pattern.find()) {
            String url = pattern.group(1);
            imageList.add(url);
        }
        return imageList;
    }
}
