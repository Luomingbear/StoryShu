package com.storyshu.storyshu.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.amap.api.maps.model.LatLng;
import com.storyshu.storyshu.info.BaseUserInfo;
import com.storyshu.storyshu.info.RegisterUserInfo;
import com.storyshu.storyshu.info.StoryInfo;
import com.storyshu.storyshu.utils.ListUtil;
import com.storyshu.storyshu.utils.time.TimeUtils;

import java.util.ArrayList;

/**
 * 故事数据库
 * Created by bear on 2016/12/27.
 */

public class DateBaseHelperIml extends BaseDataHelper {
    private static String DataBaseName = "storyShuData";

    public DateBaseHelperIml(Context context) {
        super(context, DataBaseName, null, 1);
    }

    /**
     * 获取正文
     *
     * @param storyId
     * @return
     */
    public String getContent(int storyId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "select_text " + CONTENT + " from " + STORY_TABLE + " where " + STORY_ID + " = " + storyId;
        String content = "";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst())
            content = cursor.getString(cursor.getColumnIndex(CONTENT));
        cursor.close();
        return content;
    }

    /**
     * 查询本地的故事集
     *
     * @return
     */
    public ArrayList<StoryInfo> getLocalStory() {
        ArrayList<StoryInfo> storyList = new ArrayList<>();
        String currTime = TimeUtils.getCurrentTime();
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "select * from " + STORY_TABLE + "," + USER_TABLE + " where " + STORY_TABLE
                + "." + USER_ID + " = " + USER_TABLE + "." + USER_ID + " order by " + STORY_TABLE +
                "." + CREATE_DATE + " desc";
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                StoryInfo story = new StoryInfo();
                story.setContent(cursor.getString(cursor.getColumnIndex(CONTENT)));
                story.setLocation(cursor.getString(cursor.getColumnIndex(LOCATION_NAME)));
                story.setCreateDate(cursor.getString(cursor.getColumnIndex(CREATE_DATE)));
                story.setDestroyTime(cursor.getString(cursor.getColumnIndex(DESTROY_TIME)));
                story.setStoryPic(cursor.getString(cursor.getColumnIndex(STORY_PIC)));
                story.setStoryId(cursor.getString(cursor.getColumnIndex(STORY_ID)));
                story.setAnonymous((cursor.getInt(cursor.getColumnIndex(IS_ANONYMOUS))) != 0);

                LatLng latLonPoint = new LatLng(cursor.getFloat(cursor.getColumnIndex(LAT)), cursor.getFloat(cursor.getColumnIndex(LNG)));
                story.setLatLng(latLonPoint);

                BaseUserInfo user = new BaseUserInfo();
                user.setUserId(cursor.getString(cursor.getColumnIndex(USER_ID)));
                user.setAvatar(cursor.getString(cursor.getColumnIndex(AVATAR)));
                user.setNickname(cursor.getString(cursor.getColumnIndex(NICK_NAME)));
                story.setUserInfo(user);
                // Adding contact to list
                storyList.add(story);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return storyList;
    }

    /**
     * 查询本地的未过期的故事集
     *
     * @return
     */
    public ArrayList<StoryInfo> getLifeStory() {
        ArrayList<StoryInfo> storyList = new ArrayList<>();
        String currTime = "'" + TimeUtils.getCurrentTime() + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "select * from " + STORY_TABLE + "," + USER_TABLE + " where datetime(" + DESTROY_TIME
                + ") > datetime(" + currTime + ") order by " + STORY_TABLE + "." + CREATE_DATE + " desc";
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                StoryInfo story = new StoryInfo();
                story.setContent(cursor.getString(cursor.getColumnIndex(CONTENT)));
                story.setLocation(cursor.getString(cursor.getColumnIndex(LOCATION_NAME)));
                story.setCreateDate(cursor.getString(cursor.getColumnIndex(CREATE_DATE)));
                story.setDestroyTime(cursor.getString(cursor.getColumnIndex(DESTROY_TIME)));
                story.setStoryPic(cursor.getString(cursor.getColumnIndex(STORY_PIC)));
                story.setStoryId(cursor.getString(cursor.getColumnIndex(STORY_ID)));
                story.setAnonymous((cursor.getInt(cursor.getColumnIndex(IS_ANONYMOUS))) != 0);

                LatLng latLonPoint = new LatLng(cursor.getFloat(cursor.getColumnIndex(LAT)), cursor.getFloat(cursor.getColumnIndex(LNG)));
                story.setLatLng(latLonPoint);

                BaseUserInfo user = new BaseUserInfo();
                user.setUserId(cursor.getString(cursor.getColumnIndex(USER_ID)));
                user.setAvatar(cursor.getString(cursor.getColumnIndex(AVATAR)));
                user.setNickname(cursor.getString(cursor.getColumnIndex(NICK_NAME)));
                story.setUserInfo(user);
                // Adding contact to list
                storyList.add(story);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return storyList;
    }

    /**
     * 插入用户信息
     *
     * @param userInfo
     */
    public void insertUserData(BaseUserInfo userInfo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        try {

            values.put(USER_ID, userInfo.getUserId());
            values.put(NICK_NAME, userInfo.getNickname());
            values.put(AVATAR, userInfo.getAvatar());

            db.insert(USER_TABLE, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
    }

    /**
     * 插入用户信息
     *
     * @param userInfo
     */
    public void insertUserData(RegisterUserInfo userInfo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        try {
            values.put(USER_ID, userInfo.getEmail());
            values.put(EMAIL, userInfo.getEmail());
            values.put(PHONE, userInfo.getPhone());
            values.put(PASSWORD, userInfo.getPassword());
            values.put(NICK_NAME, userInfo.getNickname());
            values.put(AVATAR, userInfo.getAvatar());

            db.insert(USER_TABLE, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
    }

    /**
     * 插入故事数据
     *
     * @param storyInfo
     */
    public void insertStoryData(StoryInfo storyInfo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        try {
            values.put(STORY_ID, storyInfo.getStoryId());
            values.put(USER_ID, storyInfo.getUserInfo().getUserId());

            values.put(CONTENT, storyInfo.getContent());
//            values.put(COVER_PIC, storyInfo.getCover());
            values.put(STORY_PIC, ListUtil.ListToString(storyInfo.getStoryPic()));
            values.put(LOCATION_NAME, storyInfo.getLocation());
            values.put(CREATE_DATE, storyInfo.getCreateDate());
            values.put(DESTROY_TIME, storyInfo.getDestroyTime());
            values.put(LAT, storyInfo.getLatLng().latitude);
            values.put(LNG, storyInfo.getLatLng().longitude);
            values.put(IS_ANONYMOUS, storyInfo.isAnonymous());

            db.insert(STORY_TABLE, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        }

        db.close();
    }
}
