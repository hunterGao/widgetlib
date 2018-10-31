package com.colonle.webviewlib;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 * Created by gaojian on 2018/8/5.
 */

public class BaseWebChromeClient extends WebChromeClient {

    protected Context mContext;
    public BaseWebChromeClient(Context context) {
        mContext = context;
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        if (view instanceof BaseWebView) {
            BaseWebView baseWebView = (BaseWebView) view;
            baseWebView.mProgressView.setProgress(newProgress);
            if (newProgress == 100) {
                baseWebView.mProgressView.setVisibility(View.GONE);
            }
        }
        super.onProgressChanged(view, newProgress);
    }

    //获取h5页面标题
    @Override
    public void onReceivedTitle(WebView view, String title) {
        super.onReceivedTitle(view, title);
        // NOTE 有可能为null，最好有url对应的title

    }

    //web前端页面地理位置请求
    @Override
    public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
        super.onGeolocationPermissionsShowPrompt(origin, callback);
    }

    //web前端页面
    @Override
    public void onGeolocationPermissionsHidePrompt() {
        super.onGeolocationPermissionsHidePrompt();
    }

    // js对话框，webview只是载体，内容渲染需要使用webviewchromclient类实现
    // 响应js的Alert函数，返回true表示由应用处理，返回false由webview处理
    @Override
    public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Alert");
        builder.setMessage(message);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                result.confirm();
            }
        });
        builder.setCancelable(false);
        builder.create().show();
        return true;
    }
}
