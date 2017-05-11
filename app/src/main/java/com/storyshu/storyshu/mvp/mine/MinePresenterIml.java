package com.storyshu.storyshu.mvp.mine;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.storyshu.storyshu.info.BaseUserInfo;
import com.storyshu.storyshu.model.UserModel;
import com.storyshu.storyshu.mvp.base.IBasePresenter;
import com.storyshu.storyshu.utils.BlurTransformation;
import com.storyshu.storyshu.utils.ToastUtil;
import com.storyshu.storyshu.utils.sharepreference.ISharePreference;

/**
 * mvp
 * 我的页面的逻辑实现
 * Created by bear on 2017/4/16.
 */

public class MinePresenterIml extends IBasePresenter<MineView> implements MinePresenter {
    private BaseUserInfo mUserInfo; //用户信息

    public MinePresenterIml(Context mContext, MineView mvpView) {
        super(mContext, mvpView);
    }

    /**
     * 设置头像数据
     */
    private void setHead() {
        if (mUserInfo == null)
            return;

        //头像
        Glide.with(mContext).load(mUserInfo.getAvatar()).into(mMvpView.getAvatarView());

        //背景模糊
        Glide.with(mContext)
                .load(mUserInfo.getAvatar())
                .bitmapTransform(new BlurTransformation(mContext, 8, 4))
                .into(mMvpView.getBlurImageView());

        mMvpView.getNicknameView().setText(mUserInfo.getNickname());
    }

    /**
     * 初始化头像等数据
     */
    private void initHead() {
        UserModel userModel = new UserModel(mContext);
        userModel.getUserInfo(ISharePreference.getUserId(mContext));
        userModel.setOnUserInfoGetListener(new UserModel.OnUserInfoGetListener() {
            @Override
            public void onSucceed(BaseUserInfo userInfo) {
                mUserInfo = userInfo;
                setHead();
            }

            @Override
            public void onFailed(String error) {
                ToastUtil.Show(mContext, error);
            }
        });
    }

    /**
     * 初始化选项数据
     */
    private void initList() {

    }

    @Override
    public void initDate() {
        initHead();

        initList();
    }

    @Override
    public void clickSetting() {
        //测试！！！
        mMvpView.goLogin();
    }

    @Override
    public void clickMyStory() {

    }

    @Override
    public void clickMyFootprint() {

    }

    @Override
    public void clickMyRanking() {

    }

    @Override
    public void clickMyTicket() {

    }

    @Override
    public void clickMyOrnament() {

    }
}
