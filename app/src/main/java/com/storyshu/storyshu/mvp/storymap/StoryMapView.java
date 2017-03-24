package com.storyshu.storyshu.mvp.storymap;

import android.os.Bundle;

import com.storyshu.storyshu.mvp.view.base.IBaseView;

/**
 * mvp模式
 * 故事地图的视图
 * Created by bear on 2017/3/24.
 */

public interface StoryMapView extends IBaseView {
    /**
     * 初始化View
     *
     */
    void initView(Bundle savedInstanceState);
}
