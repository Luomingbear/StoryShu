package com.storyshu.storyshu.mvp.mine;

import android.widget.ImageView;
import android.widget.TextView;

import com.storyshu.storyshu.mvp.view.base.IBaseView;
import com.storyshu.storyshu.widget.imageview.AvatarImageView;

/**
 * \
 * mvp
 * "我的"页面的视图
 * Created by bear on 2017/4/16.
 */

public interface MineView extends IBaseView {

    /**
     * h获取模糊的控件
     *
     * @return
     */
    ImageView getBlurImageView();

    /**
     * 获取头像控件
     *
     * @return
     */
    AvatarImageView getAvatarView();

    /**
     * 获取用户名控件
     *
     * @return
     */
    TextView getNicknameView();

    /**
     * 获取我的故事的数量控件
     *
     * @return
     */
    TextView getStoryNumView();

    /**
     * 获取我的足迹的数量控件
     *
     * @return
     */
    TextView getFootprintNumView();

    /**
     * 获取排名名次控件
     *
     * @return
     */
    TextView getRankingView();

    /**
     * 获取我的机票的数量控件
     *
     * @return
     */
    TextView getTicketNumView();

    /**
     * 获取我的装扮的数量控件
     *
     * @return
     */
    TextView getOrnamentNumView();

    /**
     * 登录页面
     */
    void goLogin();
}
