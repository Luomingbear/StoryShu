package com.storyshu.storyshu.model.create;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * 保存编辑的故事到本地
 * Created by bear on 2016/12/25.
 */

public class CreateStorySharedPreferences {
    public static final String STORY_DATA = "storyData";
    private static String CONTENT = "content"; //正文
    private static String TITLE = "title"; //标题
    private static String COVER_PIC = "coverPic"; //封面图
    private static String EXTRA = "extra"; //摘要

    public static void saveTitle(Context context, String title) {
        SharedPreferences sp = context.getSharedPreferences(STORY_DATA,
                Activity.MODE_PRIVATE);
        // 获取Editor对象
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(TITLE, title);
        editor.apply();
    }

    public static void saveContent(Context context, String content) {
        SharedPreferences sp = context.getSharedPreferences(STORY_DATA,
                Activity.MODE_PRIVATE);
        // 获取Editor对象
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(CONTENT, content);
        editor.apply();
    }

    public static void saveCoverPic(Context context, String coverPicUrl) {
        SharedPreferences sp = context.getSharedPreferences(STORY_DATA,
                Activity.MODE_PRIVATE);
        // 获取Editor对象
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(COVER_PIC, coverPicUrl);
        editor.apply();
    }

    public static void saveExtra(Context context, String extra) {
        SharedPreferences sp = context.getSharedPreferences(STORY_DATA,
                Activity.MODE_PRIVATE);
        // 获取Editor对象
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(EXTRA, extra);
        editor.apply();
    }

    public static String getTitle(Context context) {
        SharedPreferences sp = context.getSharedPreferences(STORY_DATA,
                Activity.MODE_PRIVATE);
        return sp.getString(TITLE, "");
    }

    public static String getContent(Context context) {
        SharedPreferences sp = context.getSharedPreferences(STORY_DATA,
                Activity.MODE_PRIVATE);
        return sp.getString(CONTENT, "");
    }

    public static String getCoverPic(Context context) {
        SharedPreferences sp = context.getSharedPreferences(STORY_DATA,
                Activity.MODE_PRIVATE);
        return sp.getString(COVER_PIC, "");
    }

    public static String getExtra(Context context) {
        SharedPreferences sp = context.getSharedPreferences(STORY_DATA,
                Activity.MODE_PRIVATE);
        return sp.getString(EXTRA, "");
    }
}
