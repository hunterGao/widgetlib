package com.colonel.downloadlib.download.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by gaojian on 17-10-12.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "download.db";
    private static final int DB_VERSION = 1;
    private static final String CREATE_THREAD_TABLE_SQL = "create table thread_info(_id integer primary key autoincrement," +
            "thread_id integer,url text,start double,end double,finished double)";
    private static final String DROP_THREAD_TABLE_SQL = "drop table if exists thread_info";
    private static final String CREATE_FILE_TABLE_SQL = "create table file_info(_id integer primary key autoincrement," +
            "url text, length double";
    private static final String DROP_FILE_TABLE_SQL = "drop table if exists file_info";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_FILE_TABLE_SQL);
        db.execSQL(CREATE_THREAD_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_FILE_TABLE_SQL);
        db.execSQL(DROP_THREAD_TABLE_SQL);

        db.execSQL(CREATE_FILE_TABLE_SQL);
        db.execSQL(CREATE_THREAD_TABLE_SQL);
    }
}
