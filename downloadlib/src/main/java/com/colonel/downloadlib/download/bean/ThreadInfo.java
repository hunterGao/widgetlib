package com.colonel.downloadlib.download.bean;

/**
 * Created by gaojian6 on 17-10-12.
 */

public class ThreadInfo {

    /**
     * 线程id
     */
    private int mId;

    /**
     * 线程下载的url
     */
    private String mUrl;

    /**
     * 该线程下载的起始位置
     */
    private long mStartPos;

    /**
     * 该线程下载的结束位置
     */
    private long mEndPos;

    /**
     * 该线程已经下载的长度
     */
    private long mHasDownloadLength;

    public ThreadInfo() {}

    public ThreadInfo(int id, String url, long startPos, long endPos, long hasDownloadLength) {
        mId = id;
        mUrl = url;
        mStartPos = startPos;
        mEndPos = endPos;
        mHasDownloadLength = hasDownloadLength;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public long getStartPos() {
        return mStartPos;
    }

    public void setStartPos(long startPos) {
        mStartPos = startPos;
    }

    public long getEndPos() {
        return mEndPos;
    }

    public void setEndPos(long endPos) {
        mEndPos = endPos;
    }

    public long getHasDownloadLength() {
        return mHasDownloadLength;
    }

    public void setHasDownloadLength(long hasDownloadLength) {
        mHasDownloadLength = hasDownloadLength;
    }
}
