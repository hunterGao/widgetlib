package com.colonel.viewlib.utils;

import android.content.Context;

/**
 * Created by colonel on 2018/8/5.
 */

public class UIUtils {

    public static int dp2px(Context context, float dp) {
        if (context == null) {
            return 0;
        }
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
