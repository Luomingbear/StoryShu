package com.storyshu.storyshu.activity.base;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.storyshu.storyshu.R;
import com.storyshu.storyshu.utils.PermissionUtil;

/**
 * 带有权限获取和检查的activity基类
 * Created by bear on 2017/2/21.
 */

public class IPermissionActivity extends IBaseActivity {
    private static final String TAG = "IPermissionActivity";
    protected static final int LOCATION_PERMISSION = 0; //定位权限
    protected static final int FILE_PERMISSION = 1; //文件权限
    protected static final int PERMISSION_INTENT = 3; //跳转到权限管理
    private AlertDialog.Builder dialogBuilder; //显示设置权限的窗
    private String mPermission;

    public boolean checkAndGetPermission(String permission, int requestCode) {
        mPermission = permission;

        //如果没有权限
        if (!PermissionUtil.checkPermission(this, permission)) {
            Log.e(TAG, "checkAndGetPermission: 没有获取到权限");
            // 显示解释框,已经被拒绝一次的时候
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                showRequestPermissionRationale();
            } else {
                //不显示解释框，直接申请权限
                PermissionUtil.requestPermission(this, permission, requestCode);
//                requestPermissions(new String[]{permission}, requestCode);
                Log.i(TAG, "checkAndGetPermission: 获取权限");
            }
            return false;
        } else
            return true;

    }

    private void goToSet() {
        Uri packageURI = Uri.parse("package:" + getPackageName());
        // 进入设置系统应用权限界面
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
        startActivityForResult(intent, PERMISSION_INTENT);
    }

    /**
     * 显示权限解释框
     */
    public void showRequestPermissionRationale() {
        if (dialogBuilder != null)
            return;

        Log.i(TAG, "showRequestPermissionRationale: 显示弹窗");
        dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setMessage(R.string.need_location_permission)
                .setPositiveButton("Setting", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        goToSet();
                    }
                })
                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .create()
                .show();

        dialogBuilder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                dialogBuilder = null;

                if (!PermissionUtil.checkPermission(IPermissionActivity.this, mPermission))
                    finish();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case LOCATION_PERMISSION:
                //获取权限失败：
                if (grantResults.length > 0)
                    if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                        showRequestPermissionRationale();
                    }
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PERMISSION_INTENT) {
            if (!PermissionUtil.checkPermission(this, mPermission))
                finish();
        }
    }
}
