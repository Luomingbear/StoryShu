package com.storyshu.storyshu.mvp.search;

/**
 * mvp
 * 搜索地点页面的控制
 * Created by bear on 2017/4/1.
 */

public interface SearchLocationPresenter {

    /**
     * 初始化输入
     */
    void initSearchEdit();

    /**
     * 搜索
     */
    void search();
}
