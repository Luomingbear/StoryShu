package com.storyshu.storyshu.mvp.create;

import com.storyshu.storyshu.info.LocationInfo;
import com.storyshu.storyshu.mvp.view.base.IBaseView;

import java.util.List;

/**
 * MVP模式
 * 写故事页面的View
 * Created by bear on 2017/3/17.
 */

public interface CreateStoryView extends IBaseView {
    /**
     * 获得故事的正文
     *
     * @return
     */
    String getStoryContent();

    /**
     * 选择配图
     *
     * @return
     */
    List<String> getStoryPic();

    /**
     * 故事的发布地点
     *
     * @return
     */
    LocationInfo getLocation();

    /**
     * 故事的生命周期
     *
     * @return
     */
    String getLifeTime();

    /**
     * 故事是否匿名
     *
     * @return
     */
    boolean isAnonymous();

    /**
     * 显示选择地点的弹窗
     */
    void showLocationDialog();

    /**
     * 显示图篇选择器
     */
    void showPicSelector();

    /**
     * 显示故事保质期选择弹窗
     */
    void showLifeTimeDialog();

    /**
     * 改变匿名按钮的状态
     */
    void changeAnonymous();

    /**
     * 返回
     */
    void backActivity();

    /**
     * 回到首页
     */
    void toMainActivity();
}
