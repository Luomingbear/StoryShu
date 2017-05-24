package com.storyshu.storyshu.mvp.mystory;

/**
 * mvp
 * 我的故事的代理接口
 * Created by bear on 2017/5/21.
 */

public interface MyStoryPresenter {

    /**
     * 获取我的故事数据
     */
    void getStoryData();

    /**
     * 更新数据
     */
    void refreshData();
}
