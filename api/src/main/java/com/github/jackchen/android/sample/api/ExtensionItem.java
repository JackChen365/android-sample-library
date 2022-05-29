package com.github.jackchen.android.sample.api;

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
}
