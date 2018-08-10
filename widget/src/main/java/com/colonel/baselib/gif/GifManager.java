package com.colonel.baselib.gif;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by mi on 17-3-16.
 */
public class GifManager {

    private static final String TAG = "GifManager";

    private static final String DIR = "gifCache";
    private static final int ONE_DAY_IN_MS = 24 * 60 * 60 * 1000;
    private static ExecutorService executorService = Executors.newSingleThreadExecutor();

    public static boolean isGif(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return false;
        }
        File gifFile = new File(filePath);
        if (!gifFile.exists()) {
            return false;
        }

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(gifFile);
            String id = "";
            for (int i = 0; i < 6; i++) {
                id += (char) fis.read();
            }
            if (id.startsWith("GIF")) {
                return true;
            }
        } catch (Exception e) {
            Log.e(TAG, "isGif", e);
        } finally {
        }
        return false;
    }

    public static boolean isReady(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return false;
        }
        return GifDecoder.isReady(getDirPath(filePath));
    }

    public static GifImageView createGifView(Context context, String filePath) {
        if (TextUtils.isEmpty(filePath) || context == null) {
            return null;
        }

        String dirPath = getDirPath(filePath);
        int[] delayTimes = GifDecoder.getDelayTimes(dirPath);
        int fileCount = GifDecoder.getFileCount(dirPath);
        if (delayTimes == null || delayTimes.length != fileCount) {
            return null;
        }
        return new GifImageView(context, dirPath, delayTimes, fileCount);
    }

    public static Bitmap getBitmap(int index, String dirPath) {
        File bgFile = GifDecoder.getFile(index, dirPath);
        try {
            if (bgFile != null && bgFile.exists()) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.RGB_565;
                Bitmap bitmap = BitmapFactory.decodeFile(bgFile.getAbsolutePath(), options);
                return bitmap;
            }
        } catch (Exception e) {
            Log.e(TAG, "getBitmap", e);
        }
        return null;
    }

    public static void decodeGifFile(final String filePath) {
        removeFile();
        if (TextUtils.isEmpty(filePath)) {
            return;
        }
        final File gifFile = new File(filePath);
        if (!gifFile.exists() || isReady(filePath)) {
            return;
        }

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                String dirPath = getDirPath(filePath);
                File dir = new File(dirPath);
                try {
                    if (isReady(filePath)) {
                        return;
                    }
                    FileInputStream fis = new FileInputStream(gifFile);
                    if (dir.exists()) {
                        new File(dirPath).delete();
                    }
                    GifDecoder gifDecoder = new GifDecoder();
                    gifDecoder.setDirPath(dirPath);
                    gifDecoder.read(fis);
                } catch (Exception e) {
                    Log.e(TAG, "decodeGifFile", e);
                    if (dir.exists()) {
                        dir.delete();
                    }
                }
            }
        });
    }

    private static void removeFile() {
        File dir = new File(DIR);
        File[] dirs = dir.listFiles();
        if (dirs != null && dir.length() != 0) {
            for (int k = 0; k < dirs.length; k++) {
                File dirFile = dirs[k];
                long modifyTime = dirFile.lastModified();
                Log.d(TAG, "modifyTime = " + modifyTime);
                if (dirFile != null && dirFile.isDirectory() && expired(modifyTime, ONE_DAY_IN_MS * 7)) {
                    dirFile.delete();
                }
            }
        }
    }

    private static boolean expired(long time, long interval) {
        if ((System.currentTimeMillis() - time) > interval) {
            return true;
        }
        return false;
    }

    private static String getDirPath(String filePath) {
        File gifFile = new File(filePath);
        return DIR + gifFile.getName() + "/";
    }

}
