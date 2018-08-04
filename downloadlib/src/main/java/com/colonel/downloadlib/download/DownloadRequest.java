package com.colonel.downloadlib.download;

import android.text.TextUtils;

/**
 * Created by gaojian on 17-10-12.
 */

public class DownloadRequest implements Comparable<DownloadRequest> {

    private final String mUrl;

    // 下载的优先级 值越大越优先
    private int mDownloadPriority;

    // 下载素材的md5值
    private String mMd5;

    private String mDirName;

    private String mFileName;

    private int mRetryCount = 2;

    public DownloadRequest(String url) {
        mUrl = url;
    }

    public String getUrl() {
        return mUrl;
    }

    public DownloadRequest setPriority(int priority) {
        mDownloadPriority = priority;
        return this;
    }

    public int getPriority() {
        return mDownloadPriority;
    }

    public DownloadRequest setMd5(String md5) {
        mMd5 = md5;
        return this;
    }

    public String getDirName() {
        if (TextUtils.isEmpty(mDirName)) {
            return "/sdcard/download/";
        }
        return mDirName;
    }

    public DownloadRequest setDirName(String dirName) {
        mDirName = dirName;
        return this;
    }

    public String getFileName() {
        if (TextUtils.isEmpty(mFileName)) {
            return (mUrl.hashCode() & 0xFFFFFFFFL) + "";
        }
        return mFileName;
    }

    public DownloadRequest setFileName(String fileName) {
        mFileName = fileName;
        return this;
    }

    public int getRetryCount() {
        return mRetryCount;
    }

    public DownloadRequest setRetryCount(int retryCount) {
        mRetryCount = retryCount;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DownloadRequest that = (DownloadRequest) o;
        return mUrl.equals(that.mUrl);
    }

    @Override
    public int hashCode() {
        return mUrl.hashCode();
    }

    @Override
    public int compareTo(DownloadRequest another) {
        if (another.getPriority() > mDownloadPriority) {
            return 1;
        } else if (another.getPriority() < mDownloadPriority) {
            return -1;
        }
        return 0;
    }
}
