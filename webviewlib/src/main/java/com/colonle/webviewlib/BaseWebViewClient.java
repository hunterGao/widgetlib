package com.colonle.webviewlib;

import android.net.http.SslError;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by gaojian on 2018/8/5.
 */

public class BaseWebViewClient extends WebViewClient {

    protected WebView mWebView;

    public BaseWebViewClient(WebView webView) {
        mWebView = webView;
    }
    /**
     * 证书加载错误时回调
     * @param view
     * @param handler 处理方式，handler.proceed()表示继续加载；handler.cancel()表示取消加载。默认是handler.cancel()
     * @param error
     */
    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        switch (error.getPrimaryError()) {
        }
        super.onReceivedSslError(view, handler, error);
    }

    /**
     * 网页加载,连接服务器错误回调，不仅仅是主页面，也包含页面中的img， iframe等
     * @param view
     * @param request
     * @param error
     */
    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        super.onReceivedError(view, request, error);
    }

    /**
     * 网页加载，服务器返回错误回调
     * @param view
     * @param request
     * @param errorResponse
     */
    @Override
    public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
        super.onReceivedHttpError(view, request, errorResponse);
    }

    /**
     * 加载当前url
     * @param view
     * @param url
     */
    @Override
    public void onLoadResource(WebView view, String url) {
        super.onLoadResource(view, url);
    }

    /**
     * 是否加载当前url,可用于http劫持，也可用于js调用
     * @param view
     * @param request
     * @return true 表示由当前应用处理 false表示由当前WebView处理,默认返回false
     */
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        return super.shouldOverrideUrlLoading(view, request);
    }

    /**
     * 是否拦截加载请求，可用于http劫持，API 21开始引入
     * @param view
     * @param request
     * @return null表示不拦截，非null则使用返回的资源数据
     */
    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        return super.shouldInterceptRequest(view, request);
    }

    /**
     * 是否拦截加载请求，可用于http劫持，API 21开始弃用
     * @param view
     * @param url
     * @return null表示不拦截，非null则使用返回的资源数据
     */
    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
        return super.shouldInterceptRequest(view, url);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        loadJavaScript("javascript:callJS()");
    }

    private void loadJavaScript(String jsUrl) {
        if (TextUtils.isEmpty(jsUrl)) {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mWebView.evaluateJavascript(jsUrl, new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                    //JS返回的结果
                    Log.e("Hunter", "onReceiveValue: " + value);
                }
            });
        } else {
            //加载效率慢，没有返回值
            mWebView.loadUrl(jsUrl);
        }
    }
}
