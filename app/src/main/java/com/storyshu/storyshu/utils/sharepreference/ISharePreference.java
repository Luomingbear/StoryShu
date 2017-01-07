package com.storyshu.storyshu.utils.sharepreference;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.amap.api.maps.model.LatLng;
import com.storyshu.storyshu.R;
import com.storyshu.storyshu.info.UserInfo;

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
    public static final String LAT = "lat";
    public static final String LNG = "lng";


    /**
     * 登录信息
     */
    public static final String LOGIN_DATA = "LOGIN_DATA";
    public static final String IS_LOGIN = "isLogin";


    public static void saveUserData(Context context, UserInfo userInfo) {
        SharedPreferences sp = context.getSharedPreferences(USER_DATA,
                Activity.MODE_PRIVATE);
        // 获取Editor对象
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(NICK_NAME, userInfo.getNickname());
        editor.putString(AVATAR, userInfo.getAvatar());
        editor.putInt(USER_ID, userInfo.getUserId());
        editor.apply();
    }

    public static UserInfo getUserData(Context context) {

        SharedPreferences sp = context.getSharedPreferences(USER_DATA,
                Activity.MODE_PRIVATE);
        UserInfo userInfo = new UserInfo();
        userInfo.setNickname(sp.getString(NICK_NAME, context.getResources().getString(R.string.app_name)));
        userInfo.setAvatar(sp.getString(AVATAR, ""));
        userInfo.setUserId(sp.getInt(USER_ID, UserInfo.Visitor));
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
        LatLng latLng = new LatLng(sp.getFloat(LAT, 360),
                sp.getFloat(LNG, 360));
        if (latLng.latitude == 360 && latLng.longitude == 360)
            return null;
        return latLng;
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
}
