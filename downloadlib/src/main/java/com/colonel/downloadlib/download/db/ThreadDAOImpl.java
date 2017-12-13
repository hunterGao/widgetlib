package com.colonel.downloadlib.download.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.colonel.downloadlib.download.bean.ThreadInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mi on 17-10-12.
 */

public class ThreadDAOImpl implements ThreadDAO {

    private DBHelper mDBHelper;

    public ThreadDAOImpl(Context context) {
        mDBHelper = new DBHelper(context);
    }

    @Override
    public void insertThread(ThreadInfo threadInfo) {
        if (threadInfo == null) {
            return;
        }

        try {
            SQLiteDatabase db = mDBHelper.getWritableDatabase();
            db.execSQL("insert into thread_info(thread_id, url, start, end, finished) values(?,?,?,?,?)",
                    new Object[]{threadInfo.getId(), threadInfo.getUrl(), threadInfo.getStartPos(),
                    threadInfo.getEndPos(), threadInfo.getHasDownloadLength()});
            db.close();
        } catch (Exception e) {
            // ignore
        }
    }

    @Override
    public void deleteThread(ThreadInfo threadInfo) {
        if (threadInfo == null || TextUtils.isEmpty(threadInfo.getUrl())) {
            return;
        }

        try {
            SQLiteDatabase db = mDBHelper.getWritableDatabase();
            db.execSQL("delete from thread_info where url=? and thread_id=?",
                    new Object[]{threadInfo.getUrl(), threadInfo.getId()});
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateThread(ThreadInfo threadInfo) {
        if (threadInfo == null || TextUtils.isEmpty(threadInfo.getUrl())) {
            return;
        }

        try {
            SQLiteDatabase db = mDBHelper.getWritableDatabase();
            db.execSQL("update thread_info set finished=? where url=? and thread_id=?",
                    new Object[]{threadInfo.getHasDownloadLength(), threadInfo.getUrl(), threadInfo.getId()});
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<ThreadInfo> getThread(String url) {
        try {
            List<ThreadInfo> list = new ArrayList<ThreadInfo>();
            SQLiteDatabase db = mDBHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery("select * from thread_info where url=?", new String[]{url});
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    ThreadInfo threadInfo = new ThreadInfo();
                    threadInfo.setId(cursor.getInt(cursor.getColumnIndex("thread_id")));
                    threadInfo.setUrl(cursor.getString(cursor.getColumnIndex("url")));
                    threadInfo.setStartPos(cursor.getLong(cursor.getColumnIndex("start")));
                    threadInfo.setEndPos(cursor.getLong(cursor.getColumnIndex("end")));
                    threadInfo.setHasDownloadLength(cursor.getLong(cursor.getColumnIndex("finished")));
                    list.add(threadInfo);
                }
                cursor.close();
                db.close();
                return list;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean isExists(String url, int threadId) {
        try {
            SQLiteDatabase db = mDBHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery("select * from thread_info where url=? and threadId=?",
                    new String[]{url, String.valueOf(threadId)});
            if (cursor != null) {
                boolean isExist = cursor.moveToNext();
                cursor.close();
                db.close();
                return isExist;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
