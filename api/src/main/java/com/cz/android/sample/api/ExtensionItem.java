package com.cz.android.sample.api;

/**
 * Created on 2022/3/5.
 *
 * @author Jack Chen
 * @email zhenchen@tubi.tv
 */
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
