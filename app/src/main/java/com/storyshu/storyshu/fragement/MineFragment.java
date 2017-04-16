package com.storyshu.storyshu.fragement;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.storyshu.storyshu.R;
import com.storyshu.storyshu.mvp.mine.MinePresenterIml;
import com.storyshu.storyshu.mvp.mine.MineView;
import com.storyshu.storyshu.utils.ToastUtil;
import com.storyshu.storyshu.widget.imageview.AvatarImageView;

/**
 * 我的
 * Created by bear on 2017/3/13.
 */

public class MineFragment extends IBaseStatusFragment implements MineView, View.OnClickListener {
    private MinePresenterIml mMinePresenterIml; //我的页面的逻辑

    @Override
    public int getLayoutRes() {
        return R.layout.mine_layout;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        //状态栏
        setStatusBackgroundColor(R.color.colorRed);
        //
        mRootView.findViewById(R.id.setting).setOnClickListener(this);

        mRootView.findViewById(R.id.my_story).setOnClickListener(this);

        mRootView.findViewById(R.id.my_footprint).setOnClickListener(this);

        mRootView.findViewById(R.id.my_rank).setOnClickListener(this);

        mRootView.findViewById(R.id.my_ticket).setOnClickListener(this);

        mRootView.findViewById(R.id.my_ornament).setOnClickListener(this);


        mMinePresenterIml = new MinePresenterIml(getContext(), this);
    }

    @Override
    public void initData() {
        mMinePresenterIml.initDate();
    }

    @Override
    public void initEvents() {
    }

    @Override
    public void showToast(String s) {
        ToastUtil.Show(getContext(), s);
    }

    @Override
    public void showToast(int stringRes) {
        ToastUtil.Show(getContext(), stringRes);
    }

    @Override
    public ImageView getBlurImageView() {
        return (ImageView) mRootView.findViewById(R.id.blur_image);
    }

    @Override
    public AvatarImageView getAvatarView() {
        return (AvatarImageView) mRootView.findViewById(R.id.avatar);
    }

    @Override
    public TextView getNicknameView() {
        return (TextView) mRootView.findViewById(R.id.nickname);
    }

    @Override
    public TextView getStoryNumView() {
        return (TextView) mRootView.findViewById(R.id.my_story_num);
    }

    @Override
    public TextView getFootprintNumView() {
        return (TextView) mRootView.findViewById(R.id.my_footprint_num);
    }

    @Override
    public TextView getRankingView() {
        return (TextView) mRootView.findViewById(R.id.my_rank_num);
    }

    @Override
    public TextView getTicketNumView() {
        return (TextView) mRootView.findViewById(R.id.my_ticket_num);
    }

    @Override
    public TextView getOrnamentNumView() {
        return (TextView) mRootView.findViewById(R.id.my_ornament_num);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting:
                mMinePresenterIml.clickSetting();
            case R.id.my_story:
                mMinePresenterIml.clickMyStory();
                break;
            case R.id.my_footprint:
                mMinePresenterIml.clickMyFootprint();
                break;
            case R.id.my_rank:
                mMinePresenterIml.clickMyRanking();
                break;
            case R.id.my_ticket:
                mMinePresenterIml.clickMyTicket();
                break;
            case R.id.my_ornament:
                mMinePresenterIml.clickMyOrnament();
                break;
        }
    }
}
