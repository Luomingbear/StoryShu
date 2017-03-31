package com.storyshu.storyshu.data;

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
    public static String EMAIL = "email"; //邮箱
    public static String PHONE = "phone"; //手机号
    public static String NICK_NAME = "nick_name"; //昵称
    public static String USER_ID = "user_id"; //用户id
    public static String PASSWORD = "password"; //密码
    public static String AVATAR = "avatar"; //头像
    public static String ACCOUNT = "account"; //用户名
    // TODO: 2017/3/29 上线前把数据库保存密码取消


    /**
     * 故事数据
     */
    public static String STORY_TABLE = "story_table"; //故事的id
    public static String STORY_ID = "story_id"; //故事的id
    //    public static String COVER_PIC = "cover_pic"; //故事的封面
    public static String STORY_PIC = "story_pic"; //故事配图
    public static String CONTENT = "content"; //故事的内容
    public static String CREATE_DATE = "create_date"; //发布时间
    public static String DESTROY_TIME = "destroy_time"; //生命期
    public static String IS_ANONYMOUS = "is_anonymous"; //是否匿名

    /**
     * 位置数据
     */
    public static String LOCATION_TABLE = "location_table";
    public static String LOCATION_ID = "location_id"; //位置id
    public static String LOCATION_NAME = "location_name"; //位置名字
    public static String LAT = "lat"; //纬度
    public static String LNG = "lng";


    public BaseDataHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        this(context, name, factory, version, null);
    }

    public BaseDataHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version,
                          DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //用户表
        String CREATE_USR_TABLE = "CREATE TABLE " + USER_TABLE + "("
                + USER_ID + " TEXT PRIMARY KEY NOT NULL, " + EMAIL + " TEXT ," + PHONE + " TEXT ," +
                PASSWORD + " TEXT NOT NULL," + NICK_NAME + " TEXT NOT NULL," + AVATAR + " TEXT NOT NULL, " +
                ACCOUNT + " TEXT " + ")";
        db.execSQL(CREATE_USR_TABLE);

        //故事表
        String CREATE_STORY_TABLE = "CREATE TABLE " + STORY_TABLE + "("
                + STORY_ID + " TEXT PRIMARY KEY NOT NULL, " + USER_ID + " TEXT NOT NULL,"
                + LOCATION_ID + " INTEGER, " + LOCATION_NAME + " TEXT NOT NULL, " + LAT + " REAL, " +
                LNG + " REAL," + STORY_PIC + " TEXT, " + CONTENT + " TEXT NOT NULL, "
                + CREATE_DATE + " TEXT NOT NULL, " + DESTROY_TIME + " TEXT NOT NULL, " + IS_ANONYMOUS + " INTEGER" + ")";
        db.execSQL(CREATE_STORY_TABLE);

        //位置表
//        String CREATE_LOCATION_TABLE = "CREATE TABLE " + LOCATION_TABLE + "("
//                + LOCATION_ID + " INTEGER PRIMARY KEY," + LOCATION_NAME + " TEXT,"
//                + LAT + "DOUBLE" + LNG + "DOUBLE" + ")";
//        db.execSQL(CREATE_LOCATION_TABLE);
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
