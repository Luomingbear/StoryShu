package com.storyshu.storyshu.utils.sharepreference;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.amap.api.maps.model.LatLng;
import com.storyshu.storyshu.R;
import com.storyshu.storyshu.info.BaseUserInfo;

/**
 * 简单数据的本地保存
 * Created by bear on 2016/12/26.
 */

public class ISharePreference {
    /**
     * 用户数据
     */
    public static final String USER_DATA = "userData";
    private static String NICK_NAME = "nickName"; //昵称
    private static String AVATAR = "avatar"; //头像
    private static String USER_ID = "userId"; //id


    /**
     * 故事数据
     */
    public static final String STORY_DATA = "storyData";
    private static String CONTENT = "content"; //正文
    private static String TITLE = "title"; //标题
    private static String COVER_PIC = "coverPic"; //封面图
    private static String EXTRA = "extra"; //摘要

    /**
     * 定位数据
     */
    public static final String LOCATION_DATA = "locationData";
    public static final String CITY_NAME = "cityName";
    public static final String LAT = "lat";
    public static final String LNG = "lng";


    /**
     * 登录信息
     */
    public static final String LOGIN_DATA = "LOGIN_DATA";
    public static final String IS_LOGIN = "isLogin";
    public static final String IS_NIGHT_MODE = "isNight"; //夜间模式


//    public static void saveUserData(Context context, BaseUserInfo userInfo) {
//        SharedPreferences sp = context.getSharedPreferences(USER_DATA,
//                Activity.MODE_PRIVATE);
//        // 获取Editor对象
//        SharedPreferences.Editor editor = sp.edit();
//        editor.putString(NICK_NAME, userInfo.getNickname());
//        editor.putString(AVATAR, userInfo.getAvatar());
//        editor.putInt(USER_ID, userInfo.getUserId());
//        editor.apply();
//    }

    /**
     * 保存使用者的用户id
     *
     * @param context
     * @param userId
     */
    public static void saveUserId(Context context, int userId) {
        SharedPreferences sp = context.getSharedPreferences(USER_DATA,
                Activity.MODE_PRIVATE);
        // 获取Editor对象
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(USER_ID, userId);
        editor.apply();
    }

    /**
     * 保存使用者的用户id
     *
     * @param context
     * @param userInfo
     */
    public static void saveUserInfo(Context context, BaseUserInfo userInfo) {
        SharedPreferences sp = context.getSharedPreferences(USER_DATA,
                Activity.MODE_PRIVATE);
        // 获取Editor对象
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(USER_ID, userInfo.getUserId());
        editor.putString(USER_ID, userInfo.getAvatar());
        editor.putString(USER_ID, userInfo.getNickname());
        editor.apply();
    }

    /**
     * 移除用户数据
     * @param context
     */
    public static void removeUserInfo(Context context) {
        SharedPreferences sp = context.getSharedPreferences(USER_DATA,
                Activity.MODE_PRIVATE);
        // 获取Editor对象
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(USER_ID);
        editor.remove(NICK_NAME);
        editor.remove(AVATAR);
        editor.apply();

    }

    /**
     * 获取用户的id
     *
     * @return
     */
    public static int getUserId(Context context) {
        SharedPreferences sp = context.getSharedPreferences(USER_DATA,
                Activity.MODE_PRIVATE);
        return sp.getInt(USER_ID, -1);
    }

    public static void saveNickname(Context context, String nickname) {
        SharedPreferences sp = context.getSharedPreferences(USER_DATA,
                Activity.MODE_PRIVATE);
        // 获取Editor对象
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(NICK_NAME, nickname);
        editor.apply();
    }

    public static String getNickName(Context context) {
        SharedPreferences sp = context.getSharedPreferences(USER_DATA,
                Activity.MODE_PRIVATE);
        return sp.getString(NICK_NAME, "");
    }

    public static void saveAvatar(Context context, String avatar) {
        SharedPreferences sp = context.getSharedPreferences(USER_DATA,
                Activity.MODE_PRIVATE);
        // 获取Editor对象
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(AVATAR, avatar);
        editor.apply();
    }

    public static String getAVATAR(Context context) {
        SharedPreferences sp = context.getSharedPreferences(USER_DATA,
                Activity.MODE_PRIVATE);
        return sp.getString(AVATAR, "");
    }

    public static BaseUserInfo getUserData(Context context) {
        SharedPreferences sp = context.getSharedPreferences(USER_DATA,
                Activity.MODE_PRIVATE);
        BaseUserInfo userInfo = new BaseUserInfo();
        userInfo.setNickname(sp.getString(NICK_NAME, context.getResources().getString(R.string.app_name)));
        userInfo.setAvatar(sp.getString(AVATAR, ""));
        userInfo.setUserId(sp.getInt(USER_ID, -1));
        return userInfo;
    }

    /**
     * 保存正在编辑的故事标题
     *
     * @param context
     * @param title
     */
    public static void saveTitle(Context context, String title) {
        SharedPreferences sp = context.getSharedPreferences(STORY_DATA,
                Activity.MODE_PRIVATE);
        // 获取Editor对象
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(TITLE, title);
        editor.apply();
    }

    /**
     * 保存正在编辑的正文
     *
     * @param context
     * @param content
     */
    public static void saveContent(Context context, String content) {
        SharedPreferences sp = context.getSharedPreferences(STORY_DATA,
                Activity.MODE_PRIVATE);
        // 获取Editor对象
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(CONTENT, content);
        editor.apply();
    }

    /**
     * 保存选择的封面图
     *
     * @param context
     * @param coverPicUrl
     */
    public static void saveCoverPic(Context context, String coverPicUrl) {
        SharedPreferences sp = context.getSharedPreferences(STORY_DATA,
                Activity.MODE_PRIVATE);
        // 获取Editor对象
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(COVER_PIC, coverPicUrl);
        editor.apply();
    }

    /**
     * 保存正在编辑的摘要
     *
     * @param context
     * @param extra
     */
    public static void saveExtra(Context context, String extra) {
        SharedPreferences sp = context.getSharedPreferences(STORY_DATA,
                Activity.MODE_PRIVATE);
        // 获取Editor对象
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(EXTRA, extra);
        editor.apply();
    }

    /**
     * 获取标题
     *
     * @param context
     * @return
     */
    public static String getTitle(Context context) {
        SharedPreferences sp = context.getSharedPreferences(STORY_DATA,
                Activity.MODE_PRIVATE);
        return sp.getString(TITLE, "");
    }

    /**
     * 获取正文
     *
     * @param context
     * @return
     */
    public static String getContent(Context context) {
        SharedPreferences sp = context.getSharedPreferences(STORY_DATA,
                Activity.MODE_PRIVATE);
        return sp.getString(CONTENT, "");
    }

    /**
     * 获取选择的封面图
     *
     * @param context
     * @return
     */
    public static String getCoverPic(Context context) {
        SharedPreferences sp = context.getSharedPreferences(STORY_DATA,
                Activity.MODE_PRIVATE);
        return sp.getString(COVER_PIC, "");
    }

    /**
     * 获取编辑的摘要
     *
     * @param context
     * @return
     */
    public static String getExtra(Context context) {
        SharedPreferences sp = context.getSharedPreferences(STORY_DATA,
                Activity.MODE_PRIVATE);
        return sp.getString(EXTRA, "");
    }

    /**
     * 保存最近的一次经纬度
     */
    public static void saveLatLng(Context context, LatLng latLng) {
        SharedPreferences sp = context.getSharedPreferences(LOCATION_DATA,
                Activity.MODE_PRIVATE);
        // 获取Editor对象
        SharedPreferences.Editor editor = sp.edit();

        editor.putFloat(LAT, (float) latLng.latitude);
        editor.putFloat(LNG, (float) latLng.longitude);
        editor.apply();
    }

    /**
     * 获取上次保存的位置信息
     *
     * @return 经纬度
     */
    public static LatLng getLatLngData(Context context) {
        SharedPreferences sp = context.getSharedPreferences(LOCATION_DATA,
                Activity.MODE_PRIVATE);
        /**
         * latitude - 地点的纬度，在-90 与90 之间的double 型数值。
         longitude - 地点的经度，在-180 与180 之间的double 型数值。
         */

        return new LatLng(sp.getFloat(LAT, 30), sp.getFloat(LNG, 120));
    }

    /**
     * 保存用户所在的城市名字
     *
     * @param context
     * @param cityName
     */
    public static void saveCityName(Context context, String cityName) {
        SharedPreferences sp = context.getSharedPreferences(LOCATION_DATA,
                Activity.MODE_PRIVATE);
        // 获取Editor对象
        SharedPreferences.Editor editor = sp.edit();

        editor.putString(CITY_NAME, cityName);
        editor.apply();
    }

    /**
     * 获取用户所在的城市名字
     *
     * @param context
     */
    public static String getCityName(Context context) {
        SharedPreferences sp = context.getSharedPreferences(LOCATION_DATA,
                Activity.MODE_PRIVATE);
        return sp.getString(CITY_NAME, "");
    }

    /**
     * 登录成功
     */
    public static void setIsLogin(Context context, boolean isFirstLogin) {
        SharedPreferences sp = context.getSharedPreferences(LOGIN_DATA,
                Activity.MODE_PRIVATE);
        // 获取Editor对象
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(IS_LOGIN, isFirstLogin);

        editor.apply();
    }

    /**
     * 是否完成登录
     *
     * @return 经纬度
     */
    public static boolean isLogin(Context context) {
        SharedPreferences sp = context.getSharedPreferences(LOGIN_DATA,
                Activity.MODE_PRIVATE);

        return sp.getBoolean(IS_LOGIN, true);
    }

    /**
     * 登录成功
     */
    public static void setIsNightMode(Context context, boolean isNightMode) {
        SharedPreferences sp = context.getSharedPreferences(LOGIN_DATA,
                Activity.MODE_PRIVATE);
        // 获取Editor对象
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(IS_NIGHT_MODE, isNightMode);

        editor.apply();
    }

    /**
     * 是否完成登录
     *
     * @return 经纬度
     */
    public static boolean isNightMode(Context context) {
        SharedPreferences sp = context.getSharedPreferences(LOGIN_DATA,
                Activity.MODE_PRIVATE);

        return sp.getBoolean(IS_NIGHT_MODE, false);
    }
}
