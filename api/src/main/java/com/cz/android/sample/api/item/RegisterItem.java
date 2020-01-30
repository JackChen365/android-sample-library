package com.cz.android.sample.api.item;

import com.cz.android.sample.api.AndroidSampleConstant;

import static com.cz.android.sample.api.AndroidSampleConstant.NORMAL_TYPE;

/**
 * @author Created by cz
 * @date 2019/4/18 下午4:13
 * @email bingo110@126.com
 * 示例注册对象
 */
public class RegisterItem implements Demonstrable {
    public String title;//描述标题
    public int titleRes;
    public String desc;//示例描述
    public int descRes;
    public int priority;//排序位置
    public Class<?> clazz;//当前注册的class
    public String category;//关联的category对象
    public int categoryRes;
    public int type= AndroidSampleConstant.NORMAL_TYPE;

    public RegisterItem() {
    }

    public RegisterItem(String title,String desc,Class<?> clazz, String category,int priority) {
        this.title = title;
        this.desc = desc;
        this.clazz = clazz;
        this.category = category;
        this.priority = priority;
    }

    public RegisterItem(int titleRes, int descRes, Class<?> clazz, int categoryRes,int priority,int type) {
        this.titleRes = titleRes;
        this.descRes = descRes;
        this.categoryRes = categoryRes;
        this.priority = priority;
        this.clazz = clazz;
        this.type=type;
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
    public boolean isCategory(String category) {
        return this.category.equals(category);
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public int compareTo(Demonstrable demonstrable) {
        int i = demonstrable.getPriority() - this.getPriority();
        return 0==i ? -1 : 0;
    }
}
