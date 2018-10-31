package com.colonle.webviewlib;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.colonel.downloadlib.download.DownloadRequest;
import com.colonel.downloadlib.download.DownloadTask;
import com.colonel.viewlib.LineProgressView;
import com.colonel.viewlib.utils.UIUtils;

import java.util.HashMap;

/**
 * Created by gaojian on 2018/8/5.
 */

public class BaseWebView extends WebView {

    private static final String TAG = "BaseWebView";
    private boolean isTop; // webview是否滑动到顶部
    private OnWebViewScrollListener mScrollListener;

    LineProgressView mProgressView;

    public BaseWebView(Context context) {
        super(context);
        init();
    }

    public BaseWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaseWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void onPause() {
        pauseTimers();
    }

    public void onResume() {
        resumeTimers();
    }

    public void onDestrory() {
        loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
        clearHistory();
        ((ViewGroup) getParent()).removeView(this);
        destroy();
    }

    private void init() {
        mProgressView = new LineProgressView(getContext());
        mProgressView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, UIUtils.dp2px(getContext(), 4)));
        addView(mProgressView);
        setOverScrollMode(OVER_SCROLL_ALWAYS);
        WebSettings webSettings = getSettings();
        //将图片调整到适合webview的大小
        webSettings.setUseWideViewPort(true);
        //隐藏原生的缩放控件
        webSettings.setDisplayZoomControls(false);
        //允许前端页面请求地理位置信息
        webSettings.setGeolocationEnabled(true);
        //允许与javascript进行交互
        webSettings.setJavaScriptEnabled(true);
        //允许Javascript弹窗
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        // 是否自动加载图片
        webSettings.setLoadsImagesAutomatically(true);
        setWebChromeClient(new BaseWebChromeClient(getContext()));
        setWebViewClient(new BaseWebViewClient(this));
        //debug模式下允许在浏览器调试webview
        //文档https://developers.google.com/web/tools/chrome-devtools/remote-debugging/webviews
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                setWebContentsDebuggingEnabled(true);
//            if ((getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0 ) {
//            }
        }

        initDownload();
    }

    private void initDownload() {
        setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                Log.e(TAG, String.format("onDownloadStart: url = %s, userAgent = %s, contentDisposition = %s, minetype = %s, " +
                        "contentLength = %d ", url, userAgent, contentDisposition, mimetype, contentLength));
                DownloadRequest request = new DownloadRequest.Builder(url).build();
                DownloadTask downloadTask = new DownloadTask(getContext());
                downloadTask.download(request, new DownloadTask.DownloadListener() {
                    @Override
                    public void downloadSuccess() {
                        Log.e(TAG, "downloadSuccess: ");
                    }

                    @Override
                    public void downloadFail(int code, String message) {
                        Log.e(TAG, String.format("code = %d, message = %s", code, message));
                    }
                });
            }
        });
    }

    /**
     * 设置referer字段
     * @param url
     * @param refererContent
     */
    protected void setReferer(String url, String refererContent) {
        if (TextUtils.isEmpty(refererContent) || TextUtils.isEmpty(url)) {
            return;
        }

        // 兼容4.4.3和4.4.4两个版本设置referer无效的问题
        if (("4.4.3".equals(Build.VERSION.RELEASE))
                || "4.4.4".equals(Build.VERSION.RELEASE)) {
            if (url != null && url.startsWith("javascript:") && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                evaluateJavascript(url, null);
            else {
                loadDataWithBaseURL(refererContent, "<script>window.location.href=\"" + url + "\";</script>",
                        "text/html", "utf-8", null);
            }
        } else {
            HashMap<String, String> headers = new HashMap<String, String>();
            headers.put("Referer", refererContent);
            if (url != null && url.startsWith("javascript:") && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                evaluateJavascript(url, null);
            } else {
                loadUrl(url, headers);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (getScrollY() <= 0) {
                    scrollTo(0, 1);
                }
                break;
            case MotionEvent.ACTION_MOVE:

                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX,
                                   int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        Log.e(TAG, "overScrollBy: ");
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        Log.e(TAG, "onScrollChanged: ");

    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
        Log.e(TAG, "onOverScrolled: ");
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
    }

    public void setOnWebViewScrollListener(OnWebViewScrollListener listener) {
        mScrollListener = listener;
    }

    public interface OnWebViewScrollListener {
        void onTop();
    }
}
