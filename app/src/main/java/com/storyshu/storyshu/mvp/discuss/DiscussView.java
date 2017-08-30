package com.storyshu.storyshu.mvp.discuss;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;

import com.storyshu.storyshu.mvp.view.base.IBaseView;

/**
 * 讨论的view
 * Created by bear on 2017/5/24.
 */

public interface DiscussView extends IBaseView {

    /**
     * 获取下拉刷新按钮
     *
     * @return
     */
    SwipeRefreshLayout getRefreshLayout();

    /**
     * 获取讨论信息显示的控件
     *
     * @return
     */
    RecyclerView getDiscussRv();

    /**
     * 获取文本编辑框
     *
     * @return
     */
    EditText getEditText();


    /**
     * 获取聊天室id
     *
     * @return
     */
    String getStoryId();

    /**
     * 更新数据
     */
    void notifyData();
}
