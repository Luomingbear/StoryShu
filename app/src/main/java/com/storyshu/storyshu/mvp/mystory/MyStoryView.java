package com.storyshu.storyshu.mvp.mystory;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.storyshu.storyshu.info.CardInfo;
import com.storyshu.storyshu.mvp.view.base.IBaseView;

/**
 * mvp
 * 我的故事的View
 * Created by bear on 2017/5/21.
 */

public interface MyStoryView extends IBaseView {

    /**
     * 获取我的故事的列表现实控件
     *
     * @return
     */
    RecyclerView getMyStoryRV();

    /**
     * 获取刷新控件·
     *
     * @return
     */
    SwipeRefreshLayout getRefreshLayout();


    /**
     * 跳转到故事详情
     */
    void intent2StoryRoom(CardInfo cardInfo);

}
