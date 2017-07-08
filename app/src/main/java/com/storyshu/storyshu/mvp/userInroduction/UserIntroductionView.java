package com.storyshu.storyshu.mvp.userInroduction;

import com.storyshu.storyshu.mvp.view.base.IBaseView;
import com.storyshu.storyshu.widget.discretescrollview.DiscreteScrollView;

/**
 * mvp
 * 用户简介
 * Created by bear on 2017/6/5.
 */

public interface UserIntroductionView extends IBaseView {
    /**
     * 获取显示卡片的RecyclerView
     *
     * @return
     */
    DiscreteScrollView getRecyclerView();

}
