package com.colonel.downloadlib.download;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.colonel.downloadlib.download.bean.FileInfo;
import com.colonel.downloadlib.download.bean.ThreadInfo;
import com.colonel.downloadlib.download.db.FileDAO;
import com.colonel.downloadlib.download.db.FileDAOImpl;
import com.colonel.downloadlib.download.db.ThreadDAO;
import com.colonel.downloadlib.download.db.ThreadDAOImpl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by gaojian on 17-10-12.
 */

public class DownloadTask {

    private ExecutorService mExecutor = Executors.newSingleThreadExecutor();
    private Map<DownloadRequest, DownloadListener> mDownloadMap = new HashMap<DownloadRequest, DownloadListener>();
    private List<DownloadRequest> mDownloadQueue = Collections.synchronizedList(new LinkedList<DownloadRequest>());
    private Object syncObject = new Object();
    private FileDAO mFileDAO;
    private ThreadDAO mThreadDAO;
    private Thread mPrepareThread;
    private volatile boolean mIsDownloading;

    public DownloadTask(Context context) {
        mFileDAO = new FileDAOImpl(context);
        mThreadDAO = new ThreadDAOImpl(context);
        mPrepareThread = new Thread(new PrepareRunnable());
    }

    public void download(DownloadRequest downloadRequest, DownloadListener downloadListener) {
        if (downloadRequest == null) {
            return;
        }

        String url = downloadRequest.getUrl();
        if (TextUtils.isEmpty(url)) {
            return;
        }

        mDownloadMap.put(downloadRequest, downloadListener);
        if (!mDownloadQueue.contains(downloadRequest)) {
            mDownloadQueue.add(downloadRequest);
        }

        if (!mIsDownloading) {
            mPrepareThread.start();
            mIsDownloading = true;
        }
    }

    private class PrepareRunnable implements Runnable {

        @Override
        public void run() {
            while (!mDownloadQueue.isEmpty()) {
                DownloadRequest downloadRequest = mDownloadQueue.remove(0);
                String url = downloadRequest.getUrl();
                FileInfo fileInfo = mFileDAO.getFileInfo(url);
                long length;
                if (fileInfo == null) {
                    length = 0;
                } else {
                    length = fileInfo.getLength();
                }

                ThreadInfo threadInfo;
                List<ThreadInfo> threadInfos = mThreadDAO.getThread(url);
                if (threadInfos == null || threadInfos.isEmpty()) {
                    threadInfo = new ThreadInfo(1, url, 0, length, 0);
                } else {
                    threadInfo = threadInfos.get(0);
                }
                mExecutor.execute(new DownloadRunnable(threadInfo, downloadRequest));
                synchronized (syncObject) {
                    try {
                        syncObject.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            mIsDownloading = false;
        }
    }

    private class DownloadRunnable implements Runnable {

        private static final String TAG = "DownloadRunnable";
        private int mRedirectCount = 1;
        private ThreadInfo mThreadInfo;
        private DownloadRequest mDownloadRequest;

        public DownloadRunnable(ThreadInfo threadInfo, DownloadRequest downloadRequest) {
            mThreadInfo = threadInfo;
            mDownloadRequest = downloadRequest;
        }

        @Override
        public void run() {
            if (mThreadInfo == null) {
                return;
            }

            executeDownload(mDownloadRequest.getUrl());
        }

        private void executeDownload(String downloadUrl) {
            if (mRedirectCount == 3) {
                return;
            }

            try {
                URL url = new URL(downloadUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);
                connection.setRequestMethod("GET");
                long startRange = mThreadInfo.getStartPos() + mThreadInfo.getHasDownloadLength();
                long endPos = mThreadInfo.getEndPos();
                if (endPos == 0) {
                    connection.setRequestProperty("Range", "bytes=" + 0 + "-");
                } else {
                    connection.setRequestProperty("Range", "bytes=" + startRange + "-" + endPos);
                }
                int responseCode = connection.getResponseCode();
                Log.e(TAG, "response = " + responseCode);
                if (responseCode == HttpURLConnection.HTTP_OK
                        || responseCode == HttpURLConnection.HTTP_PARTIAL) {
                    parseResponse(connection);
                } else if (responseCode / 100 == 3) {
                    String redirectUrl = connection.getHeaderField("Location");
                    mRedirectCount++;
                    executeDownload(redirectUrl);
                }
            } catch (ProtocolException e) {
                retryDonwnload(e);
            } catch (MalformedURLException e) {
                retryDonwnload(e);
                e.printStackTrace();
            } catch (IOException e) {
                retryDonwnload(e);
                e.printStackTrace();
            }
        }

        private void parseResponse(HttpURLConnection connection) {
            long fileLength = -1;
            if (mThreadInfo.getEndPos() == 0) {
                fileLength =  connection.getContentLength();
            }
            File downloadFile = new File(mDownloadRequest.getDirPath(), mDownloadRequest.getFileName());
            InputStream is = null;
            RandomAccessFile raf = null;
            try {
                is = connection.getInputStream();
                raf = new RandomAccessFile(downloadFile, "rwd");
                long lastStart = mThreadInfo.getStartPos() + mThreadInfo.getHasDownloadLength();
                raf.seek(lastStart);
                byte[] buffer = new byte[4 * 1024];
                int length;
                long offset = mThreadInfo.getHasDownloadLength();
                while ((length = is.read(buffer)) != -1) {
                    raf.write(buffer, 0, length);
                    offset += length;
                    mThreadInfo.setHasDownloadLength(offset);
                }
                // 从数据库中删除文件信息
                if (mThreadInfo.getEndPos() != 0) {
                    mFileDAO.deleteFileInfo(mThreadInfo.getUrl());
                }
                // 从数据库中删除线程信息
                mThreadDAO.deleteThread(mThreadInfo);
                DownloadListener downloadListener = mDownloadMap.get(mDownloadRequest);
                if (downloadListener != null) {
                    downloadListener.downloadSucess();
                }
                synchronized (syncObject) {
                    syncObject.notifyAll();
                }
            } catch (IOException e) {
                retryDonwnload(e);
                e.printStackTrace();
                if (fileLength != -1) {
                    mFileDAO.insertFileInfo(new FileInfo(mThreadInfo.getUrl(), fileLength));
                }
                mThreadDAO.updateThread(mThreadInfo);
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                    if (raf != null) {
                        raf.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void retryDonwnload(Exception e) {
            int retryCount = mDownloadRequest.getRetryCount();
            if (mDownloadRequest.getRetryCount() > 0) {
                Log.e("Hunter", "retryDonwnload: " + mDownloadRequest);
                executeDownload(mDownloadRequest.getUrl());
                mDownloadRequest.setRetryCount(retryCount--);
            } else {
                DownloadListener downloadListener = mDownloadMap.get(mDownloadRequest);
                if (downloadListener != null) {
                    downloadListener.downloadFail(0, "");
                }
                synchronized (syncObject) {
                    syncObject.notifyAll();
                }
            }
        }
    }

    public interface DownloadListener {
        void downloadSucess();

        void downloadFail(int code, String message);
    }
}
