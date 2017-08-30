package com.storyshu.storyshu.utils

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager


/**
 * 当前软件版本的信息
 * Created by bear on 2017/5/13.
 */

object VersionUtil {

    //版本号
    fun getVersionCode(context: Context): Int {
        return getPackageInfo(context)?.versionCode?:1
    }

    //版本名
    fun getVersionName(context: Context): String {
        return getPackageInfo(context)?.versionName?:"0.20"
    }

    private fun getPackageInfo(context: Context): PackageInfo? {
        var pi: PackageInfo?=null

        try {
            val pm = context.getPackageManager()
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS)

            return pi
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return pi
    }
}
