package com.cz.android.sample.api.item;

import com.cz.android.sample.api.AndroidSampleConstant;

import java.util.Objects;

/**
 * @author Created by cz
 * @date 2019/4/18 下午4:15
 * @email bingo110@126.com
 */
public class CategoryItem implements Demonstrable{
    public String title;//分类名称
    public int titleRes;
    public String desc;//分类描述
    public int descRes;
    public int priority;//排序位置
    public String category= null;//父级
    public int categoryRes;
    public int type= AndroidSampleConstant.NORMAL_TYPE;

    public CategoryItem() {
    }

    public CategoryItem(String title, String desc, String category) {
        this(title,desc,category,0);
    }

    public CategoryItem(String title, String desc,String category,int priority) {
        this.title = title;
        this.desc = desc;
        this.category = category;
        this.priority = priority;
    }

    public CategoryItem(int titleRes, int descRes, int categoryRes,int priority,int type) {
        this.titleRes = titleRes;
        this.descRes = descRes;
        this.categoryRes = categoryRes;
        this.priority = priority;
        this.type=type;
    }

    @Override
    public boolean isCategory(String category) {
        return this.category.equals(category);
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getDescription() {
        return desc;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryItem that = (CategoryItem) o;
        return Objects.equals(title, that.title) &&
                Objects.equals(desc, that.desc) &&
                Objects.equals(category, that.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, desc, category);
    }

    @Override
    public String toString() {
        return title;
    }

    @Override
    public int compareTo(Demonstrable demonstrable) {
        int i = demonstrable.getPriority() - this.getPriority();
        return 0==i ? -1 : 0;
    }

}
