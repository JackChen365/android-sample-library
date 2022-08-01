package com.github.jackchen.android.sample.library.function.permission

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * @author :Created by cz
 * @date 2019/4/18 上午9:53
 * @email bingo110@126.com
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS)
annotation class SamplePermission(vararg val value: String)
