package com.colonel.downloadlib.download.db;


import com.colonel.downloadlib.download.bean.ThreadInfo;

import java.util.List;

/**
 * Created by mi on 17-10-12.
 */

public interface ThreadDAO {

    /**
     * 插入线程信息
     * @param threadInfo
     */
    void insertThread(ThreadInfo threadInfo);

    /**
     * 删除线程信息
     * @param threadInfo
     */
    void deleteThread(ThreadInfo threadInfo);

    /**
     * 更新线程信息
     * @param threadInfo
     */
    void updateThread(ThreadInfo threadInfo);

    /**
     * 查询线程信息
     * @param url
     * @return
     */
    List<ThreadInfo> getThread(String url);

    /**
     * 判断线程是否存在
     * @param url
     * @param threadId
     * @return
     */
    boolean isExists(String url, int threadId);
}
