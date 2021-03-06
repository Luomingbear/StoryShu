package com.storyshu.storyshu.mvp.storymap;

import android.os.Bundle;

import com.amap.api.maps.AMap;
import com.storyshu.storyshu.info.CardInfo;
import com.storyshu.storyshu.mvp.view.base.IBaseView;
import com.storyshu.storyshu.widget.story.StoriesAdapterView;

/**
 * mvp模式
 * 故事地图的视图
 * Created by bear on 2017/3/24.
 */

public interface StoryMapView extends IBaseView {
    /**
     * 初始化View
     */
    void initView(Bundle savedInstanceState);

    /**
     * 获得地图控件
     *
     * @return mapView
     */
    AMap getAMap();

    /**
     * 获取卡片的显示控件
     *
     * @return StoriesAdapterView
     */
    StoriesAdapterView getStoryWindow();

    /**
     * 更新故事的图标
     */
    void updateStoryIcons();

    /**
     * 显示卡片窗口
     */
    void showCardWindow();

    /**
     * 隐藏卡片窗口
     */
    void hideCardWindow();

    /**
     * 跳转到故事屋页面
     */
    void intent2StoryRoomActivity(CardInfo cardInfo);
}
