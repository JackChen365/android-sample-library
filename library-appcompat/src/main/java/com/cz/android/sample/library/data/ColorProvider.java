package com.cz.android.sample.library.data;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.annotation.IntRange;
import android.util.SparseArray;

import com.cz.android.sample.library.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * The color data provider.
 * We have all the material color for you to use in you application.
 */
public class ColorProvider {
    /**
     * an random object
     */
    public static final Random RANDOM=new Random();
    /**
     * material color array attribute
     */
    public static final int COLOR_RED=0;
    public static final int COLOR_PINK=1;
    public static final int COLOR_PURPLE=2;
    public static final int COLOR_DEEP_PURPLE=3;

    public static final int COLOR_INDIGO=4;
    public static final int COLOR_blue=5;
    public static final int COLOR_light_Blue=6;
    public static final int COLOR_CYAN=7;
    public static final int COLOR_TEAL=8;
    public static final int COLOR_LIGHT_GREEN=9;
    public static final int COLOR_LIME=10;
    public static final int COLOR_YELLOW=11;

    public static final int COLOR_AMBER=12;
    public static final int COLOR_ORANGE=13;
    public static final int COLOR_DEEP_ORANGE=14;
    public static final int COLOR_BROWN=15;

    public static final int COLOR_GREY=16;
    public static final int COLOR_BLUE_GREY=17;
    private static final int MD_COLOR_ARRAY_SIZE=18;

    /**
     * 缓存的颜色数组列
     */
    private static final SparseArray<int[]> cachedColorArray =new SparseArray<>();
    private static final List<Integer> cachedColorList =new ArrayList<>();
    /**
     * 颜色引用数据
     */
    private static final int[] COLOR_ATTR_ARRAY={
            R.array.redTextValues,
            R.array.pinkTextValues,
            R.array.purpleTextValues,
            R.array.deepPurpleTextValues,

            R.array.indigoTextValues,
            R.array.blueTextValues,
            R.array.lightBlueTextValues,
            R.array.cyanTextValues,

            R.array.tealTextValues,
            R.array.greenTextValues,
            R.array.lightGreenTextValues,
            R.array.limeTextValues,

            R.array.yellowTextValues,
            R.array.amberTextValues,
            R.array.orangeTextValues,
            R.array.deepOrangeTextValues,

            R.array.brownTextValues,
            R.array.greyTextValues,
            R.array.blueGreyTextValues,
    };

    private final Context context;

    public ColorProvider(Context context) {
        this.context = context.getApplicationContext();
    }

    /**
     * Get a random color
     * @return
     */
    public int getRandomColor(){
        float red = RANDOM.nextInt(255);
        float green = RANDOM.nextInt(255) / 2f;
        float blue = RANDOM.nextInt(255) / 2f;
        return 0xff000000 | ((int) (red   * 255.0f + 0.5f) << 16) |
                ((int) (green * 255.0f + 0.5f) <<  8) | (int) (blue  * 255.0f + 0.5f);
    }

    /**
     * Return a random color array. The parameter determine how many color inside the array.
     * @param count
     * @return
     */
    public int[] getRandomColorArray(int count){
        int[] colorArray=new int[count];
        for(int i=0;i<count;i++){
            colorArray[i] = getRandomColor();
        }
        return colorArray;
    }

    /**
     * Get a specific color array
     * @param index
     * @return
     */
    public int[] getColorArray(@IntRange(from=COLOR_RED,to=COLOR_BLUE_GREY) int index){
        int[] cacheArray = cachedColorArray.get(index);
        if(null!=cacheArray){
            return cacheArray;
        } else {
            Resources resources = context.getResources();
            String[] stringArray = resources.getStringArray(COLOR_ATTR_ARRAY[index]);
            int[] colorArray=new int[stringArray.length];
            for(int i=0;i<colorArray.length;i++){
                colorArray[i]= Color.parseColor(stringArray[i]);
            }
            cachedColorArray.put(index,colorArray);
            return colorArray;
        }
    }

    /**
     * Return all the material color.
     * @return
     */
    public List<Integer> getColorArray(){
        if(cachedColorList.isEmpty()){
            for(int i =0;i<MD_COLOR_ARRAY_SIZE;i++){
                int[] colorArray = getColorArray(i);
                for(int i1=0;i1<colorArray.length;i1++){
                    cachedColorList.add(colorArray[i1]);
                }
            }
        }
        return cachedColorList;
    }
}
