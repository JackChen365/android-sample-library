package com.cz.android.sample.api.item;

/**
 * @author Created by cz
 * @date 2019-12-10 15:42
 * @email bingo110@126.com
 */
public interface Demonstrable extends Comparable<Demonstrable>{
    /**
     * check is this demonstrable is belong to this category
     * @param category
     * @return
     */
    boolean isCategory(String category);

    /**
     * Return sample's title
     * @return
     */
    String getTitle();

    /**
     * Return sample's description
     * @return
     */
    String getDescription();

    int getPriority();
}
