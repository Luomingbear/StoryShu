package com.storyshu.storyshu.model.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.amap.api.services.core.LatLonPoint;
import com.storyshu.storyshu.info.StoryInfo;
import com.storyshu.storyshu.info.UserInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 故事数据库
 * Created by bear on 2016/12/27.
 */

public class StoryDateBaseHelper extends BaseDataHelper {
    private static String DataBaseName = "storyShuData";

    public StoryDateBaseHelper(Context context) {
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
        String sql = "select " + CONTENT + " from " + STORY_TABLE + " where " + STORY_ID + " = " + storyId;
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
    public List<StoryInfo> getLocalStory() {
        List<StoryInfo> storyList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "select * from " + STORY_TABLE + "," + USER_TABLE + " where " + STORY_TABLE
                + "." + USER_ID + " = " + USER_TABLE + "." + USER_ID + " order by " + STORY_TABLE +
                "." + CREATE_DATE + " desc";
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                StoryInfo story = new StoryInfo();
                story.setExtract(cursor.getString(cursor.getColumnIndex(EXTRACT)));
                story.setContent(cursor.getString(cursor.getColumnIndex(CONTENT)));
                story.setLocation(cursor.getString(cursor.getColumnIndex(LOCATION_NAME)));
                story.setCreateDate(cursor.getString(cursor.getColumnIndex(CREATE_DATE)));
                story.setDetailPic(cursor.getString(cursor.getColumnIndex(COVER_PIC)));
                story.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));
                story.setStoryId(cursor.getInt(cursor.getColumnIndex(STORY_ID)));
                LatLonPoint latLonPoint = new LatLonPoint(cursor.getFloat(cursor.getColumnIndex(LAT)), cursor.getFloat(cursor.getColumnIndex(LNG)));
                story.setLatLng(latLonPoint);

                UserInfo user = new UserInfo();
                user.setUserId(cursor.getInt(cursor.getColumnIndex(USER_ID)));
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
    public void insertUserData(UserInfo userInfo) {
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

            values.put(COVER_PIC, storyInfo.getDetailPic());
            values.put(TITLE, storyInfo.getTitle());
            values.put(EXTRACT, storyInfo.getExtract());
            values.put(CONTENT, storyInfo.getContent());
            values.put(CREATE_DATE, storyInfo.getCreateDate());
            values.put(LOCATION_NAME, storyInfo.getLocation());
            values.put(LAT, storyInfo.getLatLng().getLatitude());
            values.put(LNG, storyInfo.getLatLng().getLongitude());

            db.insert(STORY_TABLE, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        }

        db.close();
    }
}
