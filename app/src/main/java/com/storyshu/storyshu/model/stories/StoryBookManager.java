package com.storyshu.storyshu.model.stories;

import android.content.Context;

import com.amap.api.maps.MapView;
import com.storyshu.storyshu.info.StoryInfo;
import com.storyshu.storyshu.data.StoryDateBaseHelper;
import com.storyshu.storyshu.model.location.IMapManager;

import java.util.List;

/**
 * 故事集图标管理
 * Created by bear on 2017/1/7.
 */

public class StoryBookManager {
    private static StoryBookManager instance;
    private Context mContext;
    private IMapManager mMapManager;

    public static StoryBookManager getInstance() {
        if (instance == null) {
            synchronized (StoryBookManager.class) {
                if (instance == null)
                    instance = new StoryBookManager();
            }
        }
        return instance;
    }

    public StoryBookManager init(Context context, MapView mapView) {
        mMapManager = new IMapManager(context, mapView);
        mContext = context;
        return this;
    }

    /**
     * 显示故事集
     */
    public void showStoryBooks() {
        // TODO: 2017/1/7 距离检测，如果故事集的地点和用户很近则显示
        StoryDateBaseHelper storyDateBaseHelper = new StoryDateBaseHelper(mContext);
        List<StoryInfo> storyList = storyDateBaseHelper.getLocalStory();
        for (StoryInfo storyInfo : storyList) {
            mMapManager.showStoryIcon(storyInfo.getLatLng(), storyInfo.getContent(), storyInfo.getCover());
        }
    }

}