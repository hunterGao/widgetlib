package com.colonel.downloadlib.download.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.colonel.downloadlib.download.bean.FileInfo;

/**
 * Created by gaojian6 on 17-10-12.
 */

public class FileDAOImpl implements FileDAO {

    private DBHelper mDBHelper;

    public FileDAOImpl(Context context) {
        mDBHelper = new DBHelper(context);
    }

    @Override
    public void insertFileInfo(FileInfo fileInfo) {
        if (fileInfo == null) {
            return;
        }

        try {
            SQLiteDatabase db = mDBHelper.getWritableDatabase();
            db.execSQL("insert into file_info(url, length) values(?,?)",
                    new Object[]{fileInfo.getUrl(), fileInfo.getLength()});
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteFileInfo(String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }

        try {
            SQLiteDatabase db = mDBHelper.getWritableDatabase();
            db.execSQL("delete from file_info where url=?", new Object[]{url});
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public FileInfo getFileInfo(String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }

        try {
            SQLiteDatabase db = mDBHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery("select * from file_info where url=?", new String[]{url});
            if (cursor != null && cursor.moveToNext()) {
                FileInfo fileInfo = new FileInfo();
                fileInfo.setUrl(cursor.getString(cursor.getColumnIndex("url")));
                fileInfo.setLength(cursor.getLong(cursor.getColumnIndex("length")));
                cursor.close();
                db.close();
                return fileInfo;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
