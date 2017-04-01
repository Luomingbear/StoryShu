package com.storyshu.storyshu.mvp.search;

import android.support.v7.widget.RecyclerView;
import android.widget.EditText;

import com.storyshu.storyshu.mvp.view.base.IBaseActivityView;

/**
 * mvp
 * 搜索故事的视图；
 * Created by bear on 2017/4/1.
 */

public interface SearchLocationView extends IBaseActivityView {
    /**
     * 获取输入地点的编辑框
     *
     * @return
     */
    EditText getSearchEt();

    /**
     * 获取显示地点列表的recycleView
     *
     * @return
     */
    RecyclerView getSearchItemRv();
}
