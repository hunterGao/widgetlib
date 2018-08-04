package com.colonel.downloadlib.download;

import android.text.TextUtils;

/**
 * Created by gaojian on 17-10-12.
 */

public class DownloadRequest implements Comparable<DownloadRequest> {

    private String mUrl;
    private int mDownloadPriority; //下载的优先级 值越大越优先
    private long mFileSize; //下载文件的大小
    private String mFileMd5; //下载素材的md5值
    private String mDirPath; //下载文件存放的目录，默认"/sdcard/download"
    private String mFileName; //下载后的文件命名，默认以url的hash值存储
    private int mRetryCount = 2; //下载失败后的重试次数，默认为2

    private DownloadRequest(Builder builder) {
        mUrl = builder.mUrl;
        mDownloadPriority = builder.mDownloadPriority;
        mFileSize = builder.mFileSize;
        mFileMd5 = builder.mFileMd5;
        mDirPath = builder.mDirPath;
        mFileName = builder.mFileName;
        mRetryCount = builder.mRetryCount;
    }

    public String getUrl() {
        return mUrl;
    }

    public int getPriority() {
        return mDownloadPriority;
    }

    public String getDirPath() {
        if (TextUtils.isEmpty(mDirPath)) {
            return "/sdcard/download/";
        }
        return mDirPath;
    }

    public String getFileName() {
        if (TextUtils.isEmpty(mFileName)) {
            return (mUrl.hashCode() & 0xFFFFFFFFL) + "";
        }
        return mFileName;
    }

    public int getRetryCount() {
        return mRetryCount;
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

    public static class Builder {
        private String mUrl;
        private int mDownloadPriority; //下载的优先级 值越大越优先
        private long mFileSize; //下载文件的大小
        private String mFileMd5; //下载素材的md5值
        private String mDirPath; //下载文件存放的目录，默认"/sdcard/download"
        private String mFileName; //下载后的文件命名，默认以url的hash值存储
        private int mRetryCount = 2; //下载失败后的重试次数，默认为2

        public Builder(String url) {
            mUrl = url;
        }

        /**
         * 下载的优先级
         * @param priority
         * @return
         */
        public Builder downloadPriority(int priority) {
            mDownloadPriority = priority;
            return this;
        }

        /**
         * 下载文件的真实大小，用于下载开始时的大小校验，防止被劫持
         * @param size
         * @return
         */
        public Builder fileSize(long size) {
            mFileSize = size;
            return this;
        }

        /**
         * 下载文件的md5值，用于下载完成后的文件校验，防止被劫持
         * @param md5
         * @return
         */
        public Builder fileMd5(String md5) {
            mFileMd5 = md5;
            return this;
        }

        /**
         * 文件下载完成后的文件命名
         * @param fileName
         * @return
         */
        public Builder fileName(String fileName) {
            mFileName = fileName;
            return this;
        }

        /**
         * 文件下载后的存储目录
         * @param dirPath
         * @return
         */
        public Builder cacheDir(String dirPath) {
            mDirPath = dirPath;
            return this;
        }

        /**
         * 下载失败的重试次数
         * @param count
         * @return
         */
        public Builder retryCount(int count) {
            mRetryCount = count;
            return this;
        }

        public DownloadRequest build() {
            return new DownloadRequest(this);
        }
    }
}
