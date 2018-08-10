package com.colonel.baselib.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gaojian on 2018/8/6.
 */

public class PermissionHelper {

    private static PermissionHelper sInstance = new PermissionHelper();

    private Map<Integer, OnPermissionResultListener> listenerMap = new HashMap<>();

    public static PermissionHelper inst() {
        return sInstance;
    }

    private PermissionHelper(){
    }

    public boolean hasPermission(Context context, String permissions) {
        if (context == null || TextUtils.isEmpty(permissions)) {
            return false;
        }

        return ActivityCompat.checkSelfPermission(context, permissions)
                != PackageManager.PERMISSION_GRANTED;
    }

    public void requestPermission(Activity activity, String permission) {
        requestPermission(activity, permission, null);
    }

    public void requestPermission(Activity activity, String permission, OnPermissionResultListener listener) {
        if (activity == null || TextUtils.isEmpty(permission)) {
            return;
        }

        //之前申请该权限被用户拒绝了
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {

        }

        int requestCode = Math.abs(permission.hashCode());
        ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
        if (listener != null) {
            listenerMap.put(requestCode, listener);
        }
    }

    /**
     * 该方法需在activity的onRequestPermissionsResult方法的基类中调用
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        OnPermissionResultListener listener = listenerMap.get(requestCode);
        if (listener != null) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    listener.onGranted(permissions[i]);
                } else {
                    listener.onDenied(permissions[i]);
                }
            }
            listenerMap.remove(requestCode);
        }
    }

    public interface OnPermissionResultListener {

        /**
         * 权限授予
         * NOTE:若一次性请求多个权限，该方法会执行多次，注意判断permission
         * @param permission
         */
        void onGranted(String permission);

        /**
         * 权限拒绝
         * NOTE:若一次性请求多个权限，该方法会执行多次，注意判断permission
         * @param permission
         */
        void onDenied(String permission);
    }

    private boolean isDeclareInManifest(String permission) {
        ApplicationInfo applicationInfo = new ApplicationInfo();
        return false;
    }


}
