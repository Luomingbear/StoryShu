package com.storyshu.storyshu.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * 权限管理的工具
 * Created by bear on 2017/2/21.
 */

public class PermissionUtil {

    /**
     * 判断权限是否得到
     *
     * @param context    上下文
     * @param permission 权限 Manifest.permission.
     * @return true 得到 flase 没有得到
     */
    public static boolean checkPermission(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) ==
                PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 判断多个权限是否得到
     *
     * @param context
     * @param permissions
     * @return
     */
    public static boolean checkPermission(Context context, String... permissions) {
        for (String pemission : permissions) {
            if (!checkPermission(context, pemission))
                return false;
        }
        return true;
    }

    public static void requestPermission(Activity activity, String request, int permissionCode) {
        ActivityCompat.requestPermissions(activity, new String[]{request}, permissionCode);
    }

}
