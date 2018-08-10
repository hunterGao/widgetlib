package com.colonel.baselib.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

import java.util.concurrent.TimeoutException;

/**
 * Created by gaojian on 2018/8/8.
 */

public class ApplicationHelper {

    private static ApplicationHelper instance = null;
    private ApplicationInfo mApplicationInfo;

    public static ApplicationHelper inst(Context context) {
        if (instance == null) {
            synchronized (ApplicationHelper.class) {
                if (instance == null) {
                    instance = new ApplicationHelper(context);
                }
            }
        }

        return instance;
    }

    private ApplicationHelper(Context context) {
        PackageManager packageManager = context.getApplicationContext().getPackageManager();
        try {
            mApplicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {

        }
    }

    public boolean isPermissionDeclare(String permission) {
        if (TextUtils.isEmpty(permission)) {
            return false;
        }
        Log.e("Hunter", "isPermissionDeclare: " +  mApplicationInfo.permission);
        return false;
    }


}
