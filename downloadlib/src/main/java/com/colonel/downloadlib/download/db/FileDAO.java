package com.colonel.downloadlib.download.db;


import com.colonel.downloadlib.download.bean.FileInfo;

/**
 * Created by gaojian6 on 17-10-12.
 */

public interface FileDAO {

    /**
     * 插入文件信息
     * @param fileInfo
     */
    void insertFileInfo(FileInfo fileInfo);

    /**
     * 删除文件信息
     * @param url
     */
    void deleteFileInfo(String url);

    /**
     * 获取文件信息
     * @param url
     * @return
     */
    FileInfo getFileInfo(String url);
}
