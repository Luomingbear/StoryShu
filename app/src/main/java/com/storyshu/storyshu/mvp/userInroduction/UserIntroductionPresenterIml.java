package com.storyshu.storyshu.mvp.userInroduction;

import android.content.Context;

import com.storyshu.storyshu.adapter.card.CardViewAdapter;
import com.storyshu.storyshu.info.CardInfo;
import com.storyshu.storyshu.mvp.base.IBasePresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * mvp
 * 用户简介页面
 * Created by bear on 2017/6/5.
 */

public class UserIntroductionPresenterIml extends IBasePresenter<UserIntroductionView> implements UserIntroductionPresenter {
    private List<CardInfo> mList; //动态数据
    private CardViewAdapter mCardViewAdapter;

    public UserIntroductionPresenterIml(Context mContext, UserIntroductionView mvpView) {
        super(mContext, mvpView);
        mList = new ArrayList<>();
        mCardViewAdapter = new CardViewAdapter(mList, mContext);

    }

    @Override
    public void initData() {
        for (int i = 0; i < 6; i++) {
            CardInfo cardInfo = new CardInfo();
            cardInfo.setLikeNum(22);
            mList.add(cardInfo);
        }

        mCardViewAdapter.notifyDataSetChanged();
    }
}
