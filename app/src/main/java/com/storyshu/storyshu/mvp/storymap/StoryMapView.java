package com.storyshu.storyshu.mvp.storymap;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps.MapView;
import com.storyshu.storyshu.info.CardInfo;
import com.storyshu.storyshu.mvp.view.base.IBaseFragmentView;
import com.storyshu.storyshu.widget.story.StoriesAdapterView;

/**
 * mvp模式
 * 故事地图的视图
 * Created by bear on 2017/3/24.
 */

public interface StoryMapView extends IBaseFragmentView {
    /**
     * 初始化View
     */
    void initView(Bundle savedInstanceState);

    /**
     * 显示签到的弹窗
     *
     * @param signDays 累计签到的天数
     */
    void showSignDialog(int signDays);

    /**
     * 获得地图控件
     *
     * @return mapView
     */
    MapView getMapView();

    /**
     * 获得标题栏签到提示文本控件
     *
     * @return
     */
    TextView getSignInTV();

    /**
     * 获取卡片的显示控件
     *
     * @return StoriesAdapterView
     */
    StoriesAdapterView getStoryWindow();

    /**
     * 获取定位的按钮
     *
     * @return
     */
    View getLocationBtn();

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
