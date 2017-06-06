package com.storyshu.storyshu.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.storyshu.storyshu.R;
import com.storyshu.storyshu.activity.base.IBaseActivity;
import com.storyshu.storyshu.mvp.userInroduction.UserIntroductionPresenterIml;
import com.storyshu.storyshu.mvp.userInroduction.UserIntroductionView;
import com.storyshu.storyshu.utils.StatusBarUtils;
import com.storyshu.storyshu.utils.ToastUtil;
import com.storyshu.storyshu.widget.title.TitleView;
import com.yarolegovich.discretescrollview.DiscreteScrollView;

/**
 * 用户的简介界面
 * Created by bear on 2017/6/5.
 */

public class UserIntroductionActivity extends IBaseActivity implements UserIntroductionView {
    private TitleView mTitleView;
    private DiscreteScrollView mRecyclerView;
    private UserIntroductionPresenterIml mPresenterIml;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_introduction_layout);

        initView();

        mPresenterIml = new UserIntroductionPresenterIml(this, this);

        initEvent();
        initData();
    }

    private void initTitle() {
        mTitleView.setOnTitleClickListener(new TitleView.OnTitleClickListener() {
            @Override
            public void onLeftClick() {
                onBackPressed();
            }

            @Override
            public void onCenterClick() {

            }

            @Override
            public void onCenterDoubleClick() {

            }

            @Override
            public void onRightClick() {

            }
        });
    }

    private void initView() {
        StatusBarUtils.setColor(UserIntroductionActivity.this, R.color.colorRed);

        mTitleView = (TitleView) findViewById(R.id.title_view);
        //
        mRecyclerView = (DiscreteScrollView) findViewById(R.id.card_list);
    }

    private void initEvent() {
        initTitle();
    }

    private void initData() {
        mPresenterIml.initData();
    }

    @Override
    public void showToast(String s) {
        ToastUtil.Show(this, s);
    }

    @Override
    public void showToast(int stringRes) {
        ToastUtil.Show(this, stringRes);
    }

    @Override
    public DiscreteScrollView getRecyclerView() {
        return mRecyclerView;
    }
}
