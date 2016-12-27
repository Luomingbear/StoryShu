package com.storyshu.storyshu.model.database;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库操作的基类
 * Created by bear on 2016/12/27.
 */

public class BaseDataHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;


    /**
     * 用户数据表
     */
    public static String USER_TABLE = "user_table";
    public static String NICK_NAME = "nick_name";
    public static String USER_ID = "user_id";
    public static String AVATAR = "avatar";
    public static String ACCOUNT = "account";


    /**
     * 故事数据
     */
    public static String STORY_TABLE = "story_table"; //故事的id
    public static String STORY_ID = "story_id"; //故事的id
    public static String COVER_PIC = "cover_pic"; //故事的说明图
    public static String TITLE = "title"; //故事的标题
    public static String EXTRACT = "extract"; //故事的摘要
    public static String CREATE_DATE = "create_date"; //发布时间

    /**
     * 位置数据
     */
    public static String LOCATION_TABLE = "location_table";
    public static String LOCATION_ID = "location_id";
    public static String LOCATION_NAME = "location_name";
    public static String LAT = "lat";
    public static String LNG = "lng";


    public BaseDataHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public BaseDataHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //用户表
        String CREATE_USR_TABLE = "CREATE TABLE " + USER_TABLE + "("
                + USER_ID + " INTEGER PRIMARY KEY NOT NULL," + NICK_NAME + " TEXT NOT NULL,"
                + AVATAR + "TEXT" + ACCOUNT + "TEXT" + ")";
        db.execSQL(CREATE_USR_TABLE);

        //故事表
        String CREATE_STORY_TABLE = "CREATE TABLE " + STORY_TABLE + "("
                + STORY_ID + " INTEGER PRIMARY KEY NOT NULL," + USER_ID + "FOREIGN KEY NOT NULL,"
                + LOCATION_ID + "INTEGER," + TITLE + " TEXT NOT NULL,"
                + COVER_PIC + "TEXT NOT NULL" + EXTRACT + "TEXT NOT NULL" + CREATE_DATE + "TEXT NOT NULL" + ")";
        db.execSQL(CREATE_STORY_TABLE);

        //位置表
        String CREATE_LOCATION_TABLE = "CREATE TABLE " + LOCATION_TABLE + "("
                + LOCATION_ID + " INTEGER PRIMARY KEY," + LOCATION_NAME + " TEXT,"
                + LAT + "DOUBLE" + LNG + "DOUBLE" + ")";
        db.execSQL(CREATE_LOCATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + STORY_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + LOCATION_TABLE);
        // Creating tables again
        onCreate(db);
    }
}
