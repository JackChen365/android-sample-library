package com.cz.android.sample.api;

import java.util.Objects;

/**
 * @author Created by cz
 * @date 2019/4/18 下午4:13
 * @email bingo110@126.com
 * The registered demonstration components.
 */
public class SampleItem implements Comparable<SampleItem> {
    public String title;
    public String desc;
    public String path;
    public String className;
    public boolean isTestCase;

    public SampleItem() {
    }

    public SampleItem(String title, String desc, String path, String className) {
        this.title = title;
        this.desc = desc;
        this.path = path;
        this.className = className;
    }

    public Class<?> clazz() {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString() {
        return title;
    }

    @Override public int compareTo(final SampleItem sampleItem) {
        return title.compareTo(sampleItem.title);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        SampleItem that = (SampleItem) o;
        return Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, desc);
    }
}
