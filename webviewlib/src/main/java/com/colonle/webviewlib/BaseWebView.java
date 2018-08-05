package com.colonle.webviewlib;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.colonel.viewlib.LineProgressView;
import com.colonel.viewlib.utils.UIUtils;

import java.util.HashMap;

/**
 * Created by gaojian on 2018/8/5.
 */

public class BaseWebView extends WebView {

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

    private void init() {
        mProgressView = new LineProgressView(getContext());
        mProgressView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, UIUtils.dp2px(getContext(), 4)));
        addView(mProgressView);
        WebSettings webSettings = getSettings();
        //允许前端页面请求地理位置信息
        webSettings.setGeolocationEnabled(true);
        //允许与javascript进行交互
        webSettings.setJavaScriptEnabled(true);
        //允许Javascript弹窗
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        setWebChromeClient(new BaseWebChromeClient(getContext()));
        setWebViewClient(new BaseWebViewClient(this));
        //debug模式下允许在浏览器调试webview
        //文档https://developers.google.com/web/tools/chrome-devtools/remote-debugging/webviews
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            if ((getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0 ) {
//                setWebContentsDebuggingEnabled(true);
//            }
        }
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
}
