package com.colonel.downloadlib.download;

/**
 * Created by colonel on 2018/8/6.
 */

public class DownloadConstant {

    public static String[] DOWNLOAD_ERROR_MESSAGE = {"下载链接为空", "没有sdcard写入权限", "没有下载空间", "重定向次数过多"};

    public static class DownloadErrorCode {
        public static final int DOWNLOAD_URL_EMPTY = 1;

        public static final int NO_WRITE_EXTERNAL_STORAGE_PERMISSION = 2;

        public static final int EXTERNAL_STORAGE_NO_SPACE = 3;

        public static final int REDIRECT_TOO_MUCH = 4;
    }
}
