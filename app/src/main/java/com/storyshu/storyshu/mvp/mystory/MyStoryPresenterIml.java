package com.storyshu.storyshu.mvp.mystory;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.storyshu.storyshu.adapter.MyStoryAdapter;
import com.storyshu.storyshu.bean.getStory.UserStoryPostBean;
import com.storyshu.storyshu.info.CardInfo;
import com.storyshu.storyshu.model.stories.StoryModel;
import com.storyshu.storyshu.mvp.base.IBasePresenter;
import com.storyshu.storyshu.utils.ListUtil;
import com.storyshu.storyshu.utils.sharepreference.ISharePreference;

import java.util.ArrayList;
import java.util.List;

/**
 * mvp
 * 我的说故事的代理实现
 * Created by bear on 2017/5/21.
 */

public class MyStoryPresenterIml extends IBasePresenter<MyStoryView> implements MyStoryPresenter {
    private static final String TAG = "MyStoryPresenterIml";
    private int page = 1;
    private MyStoryAdapter mMyStoryAdapter;
    private List<CardInfo> mCardList;

    private MyStoryAdapter.OnStoryCardClickListener onStoryCardClickListener = new MyStoryAdapter.OnStoryCardClickListener() {
        @Override
        public void onClick(int position) {
            mMvpView.intent2StoryRoom(mCardList.get(position));
        }
    };

    public MyStoryPresenterIml(Context mContext, MyStoryView mvpView) {
        super(mContext, mvpView);

        mCardList = new ArrayList<>();
        mMyStoryAdapter = new MyStoryAdapter(mContext, mCardList);
        mMyStoryAdapter.setOnCardClickedListener(onStoryCardClickListener);

        mMvpView.getMyStoryRV().setAdapter(mMyStoryAdapter);
        mMvpView.getMyStoryRV().setLayoutManager(new LinearLayoutManager(mContext));

    }


    @Override
    public void getStoryData() {
        StoryModel storyModel = new StoryModel(mContext);
        storyModel.getUserStory(new UserStoryPostBean(ISharePreference.getUserId(mContext), page));
        storyModel.setOnCardInfoGotListener(new StoryModel.OnCardInfoGotListener() {
            @Override
            public void onSucceed(List<CardInfo> cardInfoList) {
                // TODO: 2017/5/24 加载更多
                mCardList.clear();
                for (CardInfo cardInfo : cardInfoList)
                    mCardList.add(cardInfo);

                ListUtil.removeDuplicate(mCardList);
                mMyStoryAdapter.notifyDataSetChanged();

                mMvpView.getRefreshLayout().setRefreshing(false);
            }

            @Override
            public void onFailed(String error) {
                Log.e(TAG, "onFailed: " + error);
                mMvpView.showToast(error);
                mMvpView.getRefreshLayout().setRefreshing(false);
            }
        });
    }

    @Override
    public void refreshData() {

    }
}