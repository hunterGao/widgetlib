package com.colonle.webviewlib.util;

import android.text.TextUtils;

/**
 * Created by colonel on 2018/8/5.
 */

public class HttpUtils {

    public static boolean isHttpUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }

        if (url.startsWith("http") || url.startsWith("https")) {
            return true;
        }

        return false;
    }
}
