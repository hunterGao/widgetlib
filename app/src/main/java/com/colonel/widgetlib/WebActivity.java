package com.colonel.widgetlib;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.widget.FrameLayout;

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

        Float.parseFloat()
    }
}
