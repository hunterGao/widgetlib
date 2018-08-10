package com.colonel.baselib.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

/**
 * Created by gaojian on 2018/8/9.
 */

public class PackageUtils {


    private PackageUtils() {

    }

    /**
     * 获取应用所有权限
     * @param context
     * @return
     */
    public static String[] getAllPermissions(@NonNull Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS);
            return packageInfo.requestedPermissions;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return null;
    }

    public static Drawable getAppIcon(Context context) {
        PackageManager pm = context.getPackageManager();
        return null;
    }

    /**
     * 获取应用的签名
     * @param context
     * @return
     */
    public static String getAppSignature(@NonNull Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            return packageInfo.signatures[0].toCharsString();
        } catch (PackageManager.NameNotFoundException e) {
        }
        return null;
    }
}
