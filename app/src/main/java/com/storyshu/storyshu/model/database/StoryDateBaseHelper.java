package com.storyshu.storyshu.model.database;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;

import com.storyshu.storyshu.info.StoryInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 故事数据库
 * Created by bear on 2016/12/27.
 */

public class StoryDateBaseHelper extends BaseDataHelper {
    public StoryDateBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public StoryDateBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    public List<StoryInfo> getLocalStory() {

        List<StoryInfo> storyList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "";
//        Cursor cursor = db.query(STORY_TABLE, sql, null);
//        if (cursor != null)
//            cursor.moveToFirst();
//        if (cursor.moveToFirst()) {
//            do {
//                StoryInfo story = new StoryInfo();
//                story.setId(Integer.parseInt(cursor.getString(0)));
//                story.setName(cursor.getString(1));
//                story.setAddress(cursor.getString(2));
//
//                // Adding contact to list
//                storyList.add(story);
//            } while (cursor.moveToNext());
//        }
        return storyList;
    }
}
