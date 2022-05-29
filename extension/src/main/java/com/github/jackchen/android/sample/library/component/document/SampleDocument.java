package com.github.jackchen.android.sample.library.component.document;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author :Created by cz
 * @date 2019/4/18 上午9:53
 * @email bingo110@126.com
 * 标记此示例的关联文档
 *
 * 这里提供两种写法
 * 1. 完全以上传git仓库的全路径
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SampleDocument {
    /**
     * 当前文档的url地址
     */
    String value();
}
