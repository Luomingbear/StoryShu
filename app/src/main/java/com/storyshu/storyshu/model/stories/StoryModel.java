package com.storyshu.storyshu.model.stories;

import android.content.Context;

import com.amap.api.maps.model.LatLng;
import com.storyshu.storyshu.data.DateBaseHelperIml;
import com.storyshu.storyshu.info.StoryInfo;
import com.storyshu.storyshu.utils.sharepreference.ISharePreference;

import java.util.ArrayList;

/**
 * mvp模式
 * 故事数据的获取
 * Created by bear on 2017/3/26.
 */

public class StoryModel {
    private Context mContext;
    private OnStoryModelListener onStoryModelListener;
    private int mUserId; //用户

    /**
     * 设置获取故事的监听
     *
     * @return
     */
    public OnStoryModelListener getOnStoryModelListener() {
        return onStoryModelListener;
    }

    public interface OnStoryModelListener {
        void onStoriesGot(ArrayList<StoryInfo> storyList);
    }

    public StoryModel(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 请求服务器
     */
    private void startGetStories(LatLng latLng) {
        // TODO: 2017/3/26 请求服务器的数据
        ArrayList<StoryInfo> list;

        DateBaseHelperIml dateBaseHelperIml = new DateBaseHelperIml(mContext);
        list = dateBaseHelperIml.getLifeStory(ISharePreference.getUserId(mContext));
        if (onStoryModelListener != null)
            onStoryModelListener.onStoriesGot(list);
    }

    /**
     * 获取用户附近的故事
     *
     * @param userId
     * @param latLng
     */
    public void getNearStories(int userId, LatLng latLng, OnStoryModelListener onStoryModelListener) {
        mUserId = userId;
        this.onStoryModelListener = onStoryModelListener;
        startGetStories(latLng);
    }
}
