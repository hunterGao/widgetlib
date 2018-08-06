package com.colonel.widgetlib;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.colonel.viewlib.LineProgressView;
import com.colonel.viewlib.utils.UIUtils;
import com.colonle.webviewlib.BaseWebView;

import java.security.Permission;

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
        webView.loadUrl("http://shouji.baidu.com/software/24398454.html");
        requestPermission();
    }

    private void requestPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
    }
}
