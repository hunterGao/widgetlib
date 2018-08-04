package com.colonel.downloadlib.download.bean;

/**
 * Created by gapjian6 on 17-10-12.
 */

public class FileInfo {

    private String mUrl; // 文件下载的地址

    private long mLength; // 文件的长度

    public FileInfo() {}

    public FileInfo(String url, long length) {
        mUrl = url;
        mLength = length;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public long getLength() {
        return mLength;
    }

    public void setLength(long length) {
        mLength = length;
    }
}
