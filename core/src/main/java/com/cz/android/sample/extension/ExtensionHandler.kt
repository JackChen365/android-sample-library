package com.cz.android.sample.extension

/**
 * Created on 2022/3/7.
 *
 * @author Jack Chen
 * @email zhenchen@tubi.tv
 */
interface ExtensionHandler<E> {
    fun handle(className: String, superClass: String, interfaces: List<String>): Boolean
    fun register(extension: E)
    fun unregister(extension: E)
}