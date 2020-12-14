package com.cz.android.sample.library.data;

import android.content.Context;
import android.content.res.AssetManager;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @author Created by cz
 * @date 2020-01-30 18:20
 * @email bingo110@126.com
 */
public class DataProvider {
    /**
     * an random object
     */
    public static final Random RANDOM=new Random();
    /**
     * English word array
     */
    private String[] stringArray;
    /**
     * some cached image url
     */
    private String[] imageArray =null;

    private final Context context;

    public DataProvider(Context context) {
        this.context = context.getApplicationContext();
    }

    @NonNull
    public String[] getItemArray(){
        return getItemArrayInternal();
    }

    @NonNull
    public String[] getItemArray(@IntRange(from = 0,to = 200) int length){
        String[] newArray=new String[length];
        String[] itemArray = getItemArrayInternal();
        System.arraycopy(itemArray,0,newArray,0,length);
        return newArray;
    }

    @NonNull
    public List<String> getItemList(@IntRange(from = 0,to = 200) int length){
        return getItemList(0,length);
    }

    @NonNull
    public List<String> getItemList(@IntRange(from = 0) int start, @IntRange(from = 0,to = 200) int length) {
        String[] items = new String[length];
        String[] englishWordArray = getItemArrayInternal();
        for (int i = 0; i < length; i++) {
            items[i] = englishWordArray[start+i];
        }
        return Arrays.asList(items);
    }

    public String getItem() {
        String[] itemArray = getItemArrayInternal();
        return itemArray[RANDOM.nextInt(itemArray.length)];
    }

    /**
     * lazily load family name data from assets
     */
    private String[] getItemArrayInternal() {
        if (null == stringArray) {
            final List<String> wordList = new ArrayList<>();
            AssetManager assets = context.getAssets();
            BufferedReader reader = null;
            try {
                InputStream inputStream = assets.open("data/english_word.txt");
                reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                String line;
                while ((line = reader.readLine()) != null) {
                    wordList.add(line);
                }
                stringArray = wordList.toArray(new String[wordList.size()]);
            } catch (IOException e) {
                //nothing to do
            } finally {
                if (null != reader) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                    }
                }
            }
        }
        return stringArray;
    }

    /**
     * Get test image array
     * @return
     */
    public String[] getImageArray(){
        if (null == imageArray) {
            final List<String> imageList = new ArrayList<>();
            AssetManager assets = context.getAssets();
            BufferedReader reader = null;
            try {
                InputStream inputStream = assets.open("data/image.txt");
                reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                String line;
                while ((line = reader.readLine()) != null) {
                    imageList.add(line);
                }
            } catch (IOException e) {
            } finally {
                imageArray = imageList.toArray(new String[imageList.size()]);
                if (null != reader) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                    }
                }
            }
        }
        return imageArray;
    }
}
