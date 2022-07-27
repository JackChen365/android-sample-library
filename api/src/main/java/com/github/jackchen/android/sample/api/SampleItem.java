package com.github.jackchen.android.sample.api;

import java.util.Objects;

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

    public boolean isAvailable() {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
            // Ignore
        }
        return false;
    }

    @Override
    public String toString() {
        return "SampleItem{" +
            "title='" + title + '\'' +
            ", desc='" + desc + '\'' +
            ", path='" + path + '\'' +
            ", className='" + className + '\'' +
            ", isTestCase=" + isTestCase +
            '}';
    }

    @Override public int compareTo(final SampleItem sampleItem) {
        return title.compareTo(sampleItem.title);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SampleItem that = (SampleItem) o;
        return Objects.equals(className, that.className);
    }

    @Override
    public int hashCode() {
        return Objects.hash(className);
    }
}
