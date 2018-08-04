package com.colonel.widgetlib.gif;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * Created by mi on 17-3-16.
 */
public class GifImageView extends FrameLayout {

    private static final String TAG = "GifImageView";
    private Handler mDrawableHandler;
    private HandlerThread mDrawableThread;
    private Handler mUIHandler = new Handler(Looper.getMainLooper());
    private int mCurrentIndex;
    private int mFileCount;
    private int[] mDelayTimes;

    private Bitmap mCurrentBitmap;
    private String mDirPath;
    private volatile boolean mIsRunning;

    private GifListener mGifListener;

    private ImageView mImageView;

    private Runnable mDrawBitmapRunnable = new Runnable() {
        @Override
        public void run() {
            // View从窗口消失
            if (!mIsRunning) {
                Log.e(TAG, "mDrawableThread is die or stop!");
                mDrawableHandler.removeCallbacks(mDrawBitmapRunnable);
                if (mDrawableThread != null) {
                    mDrawableThread.quit();
                }
                return;
            }

            // gif图展示完毕
            if (mCurrentIndex == mFileCount) {
                Log.d(TAG, "removeCallback");
                mDrawableHandler.removeCallbacks(mDrawBitmapRunnable);
                if (mDrawableThread != null) {
                    mDrawableThread.quit();
                }
                if (mGifListener != null) {
                    mGifListener.onGifFinish();
                }
                mIsRunning = false;
                return;
            }

            Bitmap tempBitmap = null;
            if (mCurrentBitmap != null) {
                tempBitmap = mCurrentBitmap;
            }
            final Bitmap lastBitmap = tempBitmap;
            final Bitmap bitmap = GifManager.getBitmap(mCurrentIndex, mDirPath);
            if (bitmap != null) {
                mCurrentBitmap = bitmap;
                mUIHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mImageView.setImageBitmap(bitmap);
                        if (lastBitmap != null && !lastBitmap.isRecycled()) {
                            lastBitmap.recycle();
                        }
                    }
                });
            }

            mDrawableHandler.postDelayed(mDrawBitmapRunnable, mDelayTimes[mCurrentIndex]);
            mCurrentIndex++;
        }
    };

    public GifImageView(Context context, String dirPath, int[] delayTimes, int fileCount) {
        super(context);
        mDirPath = dirPath;
        mDelayTimes = delayTimes;
        mFileCount = fileCount;
        mImageView = new ImageView(context);
        mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        addView(mImageView, layoutParams);
    }

    private void initThread() {
        mDrawableThread = new HandlerThread("drawable");
        mDrawableThread.start();
        mDrawableHandler = new Handler(mDrawableThread.getLooper());
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!mIsRunning && mDelayTimes != null && mDelayTimes.length == mFileCount) {
            initThread();
            mDrawableHandler.post(mDrawBitmapRunnable);
            mIsRunning = true;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.d(TAG, "onDetachedFromWindow");
        mIsRunning = false;
    }

    public void setGifListener(GifListener gifListener) {
        mGifListener = gifListener;
    }

    public interface GifListener {
        void onGifFinish();
    }
}
