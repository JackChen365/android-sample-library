package com.cz.android.sample.library.provider;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Pair;

import com.cz.android.sample.library.main.SampleConfiguration;
import com.cz.android.sample.library.provider.annotations.FieldFilter;
import com.cz.android.sample.library.provider.annotations.Table;
import com.cz.android.sample.library.provider.annotations.TableField;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author :Created by cz
 * @date 2019-06-21 16:40
 * @email bingo110@126.com
 * 数据库操作对象
 */
public class DatabaseHelper implements SampleConfiguration {
    /**
     * 保存字节码对象与uri路径
     */
    private static final Map<Class<?>, Uri> objectUriMap =new HashMap<>();
    /**
     * 保存字节码对象与uri路径
     */
    private static final Map<Class<?>, Uri> classUriMap =new HashMap<>();
    /**
     * 单例内部对象
     */
    private static DatabaseHelper databaseHelper =new DatabaseHelper();
    /**
     * 操作上下文对象
     */
    private static Context applicationContext;
    /**
     * 数据库版本变化监听
     */
    private OnDatabaseUpgradeListener upgradeListener;
    /**
     * 数据库版本,最低版本为1
     */
    private int version=1;

    @Override
    public void onCreate(Context context) {
        applicationContext = context.getApplicationContext();
    }

    private DatabaseHelper(){
    }

    /**
     * 获得操作对象
     * @return
     */
    public static DatabaseHelper getInstance(){
        return databaseHelper;
    }

    /**
     * 插入数据
     * @param item
     */
    public final Uri insert(Object item){
        Uri rtnUri=null;
        if(null!=item){
            Uri uri = getObjectUri(item.getClass());
            if (null != applicationContext && null != uri) {
                ContentResolver resolver = applicationContext.getContentResolver();
                rtnUri=resolver.insert(uri, DatabaseHelper.getContentValue(item));
            }
        }
        return rtnUri;
    }

    /**
     * 批量插入对象
     * @param items
     * @return
     */
    public final int bulkInsert(final List items) {
        int code=-1;
        if (null != items &&!items.isEmpty()){
            Object obj = items.get(0);
            Uri uri = getObjectUri(obj.getClass());
            ContentValues[] values = new ContentValues[items.size()];
            for (int i = 0; i < items.size(); i++) {
                values[i] = DatabaseHelper.getContentValue(items.get(i));
            }
            if (null != applicationContext && null != uri) {
                ContentResolver resolver = applicationContext.getContentResolver();
                code=resolver.bulkInsert(uri,values);
            }
        }
        return code;
    }

    /**
     * 替换数据
     * @param item
     * @param where
     * @param whereArgs
     * @return
     */
    public final void replace(Object item, String where, String... whereArgs){
        if(null!=item){
            Class<?> itemClass = item.getClass();
            Uri uri = getObjectUri(itemClass);
            if (null != applicationContext && null != uri) {
                ContentResolver resolver = applicationContext.getContentResolver();
                int queryCount = queryCount(itemClass, where, whereArgs);
                if(0==queryCount){
                    resolver.insert(uri, DatabaseHelper.getContentValue(item));
                } else {
                    resolver.update(uri, DatabaseHelper.getContentValue(item), where, whereArgs);
                }
            }
        }
    }

    /**
     * 根据指定条件更新对象
     * @param item
     * @param where
     * @param whereArgs
     * @return
     */
    public final int update(Object item, String where, String... whereArgs){
        int code=-1;
        if(null!=item){
            Class<?> itemClass = item.getClass();
            Uri uri = getObjectUri(itemClass);
            if (null != applicationContext && null != uri) {
                ContentResolver resolver = applicationContext.getContentResolver();
                Object query = query(itemClass, where, whereArgs, null);
                code = resolver.update(uri, DatabaseHelper.getContentValue(item), where, whereArgs);
            }
        }
        return code;
    }

    /**
     * 根据对象属性删除此记录
     * @param item
     * @return
     */
    public final int delete(Object item){
        int code=-1;
        if(null!=item){
            Pair<String, String[]> where = getWhere(item);
            code= delete(item.getClass(),where.first,where.second);
        }
        return code;
    }

    /**
     * 根据条件删除指定对象
     * @param clazz
     * @param where
     * @param whereArgs
     * @return
     */
    public final int delete(Class clazz, String where, String... whereArgs){
        int code=-1;
        Uri uri = getObjectUri(clazz);
        if (null != applicationContext && null != uri) {
            ContentResolver resolver = applicationContext.getContentResolver();
            code = resolver.delete(uri, where, whereArgs);
        }
        return code;
    }

    /**
     * 根据指定对象,获得查询条件
     * @param item
     * @return
     */
    final Pair<String, String[]> getWhere(Object item){
        Pair<String, String[]> wherePair=new Pair<>(null,null);
        if(null!=item){
            Field[] fields = item.getClass().getDeclaredFields();
            HashMap<String, String> fieldItems=new HashMap<>();
            for(int i=0;i<fields.length;i++){
                FieldFilter fieldFilter = fields[i].getAnnotation(FieldFilter.class);
                if(Modifier.STATIC!=(fields[i].getModifiers() & Modifier.STATIC)&&(null==fieldFilter||!fieldFilter.value())){
                    fields[i].setAccessible(true);
                    try {
                        Object value = fields[i].get(item);
                        if(null!=value){
                            fieldItems.put(fields[i].getName(),value.toString());
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
            int index=0;
            if(!fieldItems.isEmpty()){
                String where=new String();
                String[] whereArgs=new String[fieldItems.size()];
                for(Map.Entry<String, String> entry:fieldItems.entrySet()){
                    where+=(entry.getKey()+"=? "+(index!=fieldItems.size()-1?"and ":""));
                    whereArgs[index++]=entry.getValue();
                }
                wherePair=new Pair<>(where,whereArgs);
            }
        }
        return wherePair;
    }

    /**
     * 根据指定对象字节码文件,与查询条件查询指定对象
     *
     * @param clazz
     * @param <E>
     * @return
     */
    public final <E> E query(Class<E> clazz, String where, String[] whereArgs, String order) {
        E item = null;
        Uri uri = getObjectUri(clazz);
        if (null != applicationContext && null != uri) {
            ContentResolver resolver = applicationContext.getContentResolver();
            Cursor cursor = null;
            try {
                String[] selection = DatabaseHelper.getSelection(clazz);
                cursor = resolver.query(uri, selection, where, whereArgs, order);
                if (null != cursor&&cursor.moveToFirst()) {
                    item = DatabaseHelper.getItemByCursor(clazz, cursor);
                }
            }catch(Exception e){
                e.printStackTrace();
            } finally {
                if(null!=cursor){
                    cursor.close();
                }
            }
        }
        return item;
    }

    /**
     * 查询条目个数
     * @param clazz
     * @param <E>
     * @return
     */
    public final<E> int queryCount(Class<E> clazz){
        return queryCount(clazz,null);
    }

    /**
     * 查询条目个数
     * @param clazz
     * @param <E>
     * @return
     */
    public final<E> int queryCount(Class<E> clazz, String where, String... whereArgs){
        int count=-1;
        Uri uri = getObjectUri(clazz);
        if (null != applicationContext && null != uri) {
            ContentResolver resolver = applicationContext.getContentResolver();
            Cursor cursor = null;
            try {
                cursor = resolver.query(uri, null, where,whereArgs,null);
                if(null != cursor){
                    count = cursor.getCount();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (null != cursor) {
                    cursor.close();
                }
            }
        }
        return count;
    }

    /**
     * 查询数据集
     * @param clazz 当前操作对象字节码
     * @param <E>
     * @return
     */
    public final <E> List<E> queryList(Class<E> clazz) {
        return queryList(clazz, null, null, null);
    }

    /**
     * 查询数据集
     *
     * @param clazz 当前操作对象字节码
     * @param <E>
     * @return
     */
    public final <E> List<E> queryList(Class<E> clazz, String where, String[] whereArgs, String order) {
        List<E> items = new ArrayList<>();
        Uri uri = getObjectUri(clazz);
        if (null != applicationContext && null != uri) {
            ContentResolver resolver = applicationContext.getContentResolver();
            Cursor cursor = null;
            try {
                String[] selection = DatabaseHelper.getSelection(clazz);
                cursor = resolver.query(uri, selection, where, whereArgs, order);
                if (null != cursor) {
                    while (cursor.moveToNext()) {
                        items.add(DatabaseHelper.getItemByCursor(clazz, cursor));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (null != cursor) {
                    cursor.close();
                }
            }
        }
        return items;
    }

    /**
     * 清空指定表
     *
     * @param clazz
     */
    public void truncate(Class<?> clazz) {
        Uri uri = getObjectUri(clazz);
        ContentResolver contentResolver = applicationContext.getContentResolver();
        contentResolver.delete(uri, null, null);
    }

    /**
     * 设置数据库版本
     * @param version
     */
    public void setDatabaseVersion(int version){
        this.version=version;
    }

    /**
     * 获得数据库版本
     * @return
     */
    int getDatabaseVersion(){
        return version;
    }

    /**
     * 数据库升级
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        if(null!=upgradeListener){
            upgradeListener.onUpgrade(db,oldVersion,newVersion);
        }
    }

    /**
     * 设置数据库升级监听
     * @param listener
     */
    public void setOnDbUpgradeListener(OnDatabaseUpgradeListener listener){
        this.upgradeListener=listener;
    }


    /**
     * 数据库版本变化监听
     */
    public interface OnDatabaseUpgradeListener {
        void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);
    }



    /**
     * 根据class获得访问uri地址
     *
     * @param clazz
     * @return
     */
    static Uri getObjectUri(Class<?> clazz) {
        Uri tableUri = objectUriMap.get(clazz);
        if(null==tableUri){
            tableUri= Uri.parse("content://" + applicationContext.getPackageName() + "/class:" + clazz.getName());
            objectUriMap.put(clazz,tableUri);
        }
        return tableUri;
    }

    /**
     * 根据原始使用的uri地址
     *
     * @param clazz
     * @return
     */
    public static Uri getUri(Class<?> clazz) {
        Uri tableUri = classUriMap.get(clazz);
        if(null==tableUri){
            final String tableName=getTable(clazz);
            tableUri=Uri.parse("content://" + applicationContext.getPackageName() +"/"+ tableName);
            classUriMap.put(clazz,tableUri);
        }
        return tableUri;
    }

    /**
     * 获得对象字段列
     *
     * @param clazz
     * @return
     */
    public static String[] getSelection(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        ArrayList<String> selectionLists = new ArrayList<>();
        Table table = clazz.getAnnotation(Table.class);
        if(null!=table&&!TextUtils.isEmpty(table.primaryKey())){
            selectionLists.add(table.primaryKey());
        }
        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
            String fieldName;
            TableField tableField = fields[i].getAnnotation(TableField.class);
            if (null != tableField && !TextUtils.isEmpty(tableField.value())) {
                fieldName = tableField.value();
            } else {
                fieldName = fields[i].getName();
            }
            FieldFilter fieldFilter = fields[i].getAnnotation(FieldFilter.class);
            if (Modifier.STATIC!=(fields[i].getModifiers() & Modifier.STATIC)&&(null == fieldFilter || !fieldFilter.value())) {
                selectionLists.add(fieldName);
            }
        }
        return selectionLists.toArray(new String[selectionLists.size()]);
    }

    public static String getTable(Class<?> clazz) {
        String tableName;
        Table table = clazz.getAnnotation(Table.class);
        if (null != table&&!TextUtils.isEmpty(table.value())) {
            tableName = table.value();
        } else {
            tableName = clazz.getSimpleName();
        }
        return tableName;
    }

    /**
     * 根据对象获得指定数据库ContentValues对象
     *
     * @param item
     * @return
     */
    public static ContentValues getContentValue(Object item) {
        Class<?> clazz = item.getClass();
        ContentValues values = new ContentValues();
        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);
            String name;
            TableField tableField = field.getAnnotation(TableField.class);
            if (null != tableField && !TextUtils.isEmpty(tableField.value())) {
                name = tableField.value();
            } else {
                name = field.getName();
            }
            Class<?> type = field.getType();
            FieldFilter fieldFilter = fields[i].getAnnotation(FieldFilter.class);
            if (Modifier.STATIC!=(fields[i].getModifiers() & Modifier.STATIC)&&(null == fieldFilter || !fieldFilter.value())) {
                try {
                    if (int.class == type || Integer.class == type) {
                        values.put(name, field.getInt(item));
                    } else if (short.class == type || Short.class == type) {
                        values.put(name, field.getShort(item));
                    } else if (float.class == type || Float.class == type) {
                        values.put(name, field.getFloat(item));
                    } else if (double.class == type || Double.class == type) {
                        values.put(name, field.getDouble(item));
                    } else if (boolean.class == type || Boolean.class == type) {
                        values.put(name, field.getBoolean(item));
                    } else if (long.class == type || Long.class == type) {
                        values.put(name, field.getLong(item));
                    } else if (null != field.get(item)) {
                        values.put(name, field.get(item).toString());
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return values;
    }

    /**
     * 获得内容提供的path
     * @return
     */
    static String getAuthority(Context context){
        return context.getPackageName();
    }

    /**
     * 根据Cursor获得数据集
     * @param clazz
     * @param cursor
     * @param <E>
     * @return
     */
    public static <E> List<E> getListByCursor(Class<E> clazz, Cursor cursor){
        final List<E> items=new ArrayList<>();
        try {
            while (null != cursor&&cursor.moveToNext()) {
                items.add(DatabaseHelper.getItemByCursor(clazz, cursor));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;
    }

    /**
     * 根据 class 映射cursor信息
     * @param clazz
     * @param cursor
     * @param <E>
     */
    static <E> E getItemByCursor(Class<E> clazz, Cursor cursor) throws InstantiationException, IllegalAccessException {
        E item = clazz.newInstance();
        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);
            String name;
            TableField tableField = field.getAnnotation(TableField.class);
            if (null != tableField && !TextUtils.isEmpty(tableField.value())) {
                name = tableField.value();
            } else {
                name = field.getName();
            }
            Class<?> type = field.getType();
            FieldFilter fieldFilter = fields[i].getAnnotation(FieldFilter.class);
            if (Modifier.STATIC!=(fields[i].getModifiers() & Modifier.STATIC)&&(null == fieldFilter || !fieldFilter.value())) {
                int columnIndex = cursor.getColumnIndex(name);
                if (0 <= columnIndex) {
                    if (int.class == type || Integer.class == type) {
                        field.set(item, cursor.getInt(columnIndex));
                    } else if (short.class == type || Short.class == type) {
                        field.set(item, cursor.getShort(columnIndex));
                    } else if (float.class == type || Float.class == type) {
                        field.set(item, cursor.getFloat(columnIndex));
                    } else if (double.class == type || Double.class == type) {
                        field.set(item, cursor.getDouble(columnIndex));
                    } else if (boolean.class == type || Boolean.class == type) {
                        field.set(item, 1 == cursor.getInt(columnIndex));
                    } else if (long.class == type || Long.class == type) {
                        field.set(item, cursor.getLong(columnIndex));
                    } else {
                        field.set(item, cursor.getString(columnIndex));
                    }
                }
            }
        }
        return item;
    }

}
