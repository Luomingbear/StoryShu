package com.storyshu.storyshu.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase

/**
 * 数据库管理
 * Created by bear on 2017/7/31.
 */
object DBManager {
    private val dbName = "daqin"
    private var openHelper: DaoMaster.DevOpenHelper? = null

    /**
     * 获取单例引用

     * @param context
     * *
     * @return
     */
    private fun getDaoHelper(context: Context): DaoMaster.DevOpenHelper? {
        openHelper = DaoMaster.DevOpenHelper(context, dbName, null)
        return openHelper
    }

    /**
     * 获取可写数据库
     */
    private fun getWritableDatabase(context: Context): SQLiteDatabase? {
        if (openHelper == null) {
            openHelper = DaoMaster.DevOpenHelper(context, dbName, null)
        }
        val db = openHelper?.getWritableDatabase()
        return db
    }

    /**
     * 获取可读数据库
     */
    private fun getReadableDatabase(context: Context): SQLiteDatabase? {
        if (openHelper == null) {
            openHelper = DaoMaster.DevOpenHelper(context, dbName, null)
        }
        val db = openHelper?.getReadableDatabase()
        return db
    }

    /**
     * 获取可读的dao session
     */
    fun getReadDaoSession(context: Context): DaoSession {
        var daoMaster = DaoMaster(getReadableDatabase(context))
        return daoMaster.newSession()
    }

    /**
     * 获取可写的dao session
     */
    fun getWriteDaoSession(context: Context): DaoSession {
        var daoMaster = DaoMaster(getWritableDatabase(context))
        return daoMaster.newSession()
    }
}