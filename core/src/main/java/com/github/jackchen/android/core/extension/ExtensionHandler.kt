package com.github.jackchen.android.core.extension

interface ExtensionHandler<E> {
    fun handle(className: String, superClass: String, interfaces: List<String>): Boolean
    fun register(extension: E)
    fun unregister(extension: E)
}