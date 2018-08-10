package com.colonel.widgetlib;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.colonel.baselib.utils.ApplicationHelper;
import com.colonel.baselib.utils.PermissionHelper;
import com.colonle.webviewlib.BaseWebView;

/**
 * Created by gaojian on 2018/8/5.
 */

public class WebActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout rootView = new FrameLayout(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        rootView.setLayoutParams(params);
        BaseWebView webView = new BaseWebView(this);
        rootView.addView(webView, params);
        setContentView(rootView);

//        webView.loadUrl("http://www.baidu.com");
//        webView.loadUrl("http://shouji.baidu.com/software/24398454.html");
        webView.loadUrl("http://market.faw-benteng.com/2018/x80-x40-t0620/index_a.html#radid=3EgTpbFZffQe&aadid=716-123");
//        if (!PermissionHelper.inst().hasPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//            PermissionHelper.inst().requestPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        }

        PermissionHelper.inst().requestPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        ApplicationHelper.inst(this).isPermissionDeclare("123");

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e("Hunter", "onRequestPermissionsResult: " + permissions.length);
    }
}
