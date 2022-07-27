package com.github.jackchen.android.sample.api;

import java.util.Arrays;
import java.util.Objects;

public class ExtensionItem {
    public String className;
    public String superClass;
    public String[] interfaces;

    public ExtensionItem() {
    }

    public ExtensionItem(final String className, final String superClass, final String[] interfaces) {
        this.className = className;
        this.superClass = superClass;
        this.interfaces = interfaces;
    }

    @Override
    public String toString() {
        return "ExtensionItem{" +
            "className='" + className + '\'' +
            ", superClass='" + superClass + '\'' +
            ", interfaces=" + Arrays.toString(interfaces) +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExtensionItem that = (ExtensionItem) o;
        return Objects.equals(className, that.className);
    }

    @Override
    public int hashCode() {
        return Objects.hash(className);
    }
}
