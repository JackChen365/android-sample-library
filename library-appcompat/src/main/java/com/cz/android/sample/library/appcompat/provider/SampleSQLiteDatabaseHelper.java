package com.cz.android.sample.library.appcompat.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * @author :Created by cz
 * @date 2019-06-21 16:29
 * @email bingo110@126.com
 */
public class SampleSQLiteDatabaseHelper extends SQLiteOpenHelper {
    /**
     * Default version of this database
     */
    private static final int DEFAULT_VERSION = 1;

    public SampleSQLiteDatabaseHelper(Context context, String name) {
        super(context, name, null, DEFAULT_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
        int databaseVersion = databaseHelper.getDatabaseVersion();
        onUpgrade(db, db.getVersion(), databaseVersion);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            DatabaseHelper instance = DatabaseHelper.getInstance();
            instance.onUpgrade(db,oldVersion,newVersion);
        } finally {
            db.setVersion(newVersion);
        }
    }

}
