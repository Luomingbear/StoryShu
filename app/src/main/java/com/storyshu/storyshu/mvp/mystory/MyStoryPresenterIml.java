package com.storyshu.storyshu.mvp.mystory;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.storyshu.storyshu.R;
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
    private int position = 0; //当前的位置


    public MyStoryPresenterIml(Context mContext, MyStoryView mvpView) {
        super(mContext, mvpView);

        mCardList = new ArrayList<>();
        mMyStoryAdapter = new MyStoryAdapter(mCardList);
        mMyStoryAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener<CardInfo>() {
            @Override
            public void onItemClick(BaseQuickAdapter<CardInfo, ? extends BaseViewHolder> adapter, View view, int position) {
                mMvpView.intent2StoryRoom(mCardList.get(position));

            }
        });


        final View foot = LayoutInflater.from(mContext).inflate(R.layout.see_more, null, false);
        foot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCardList.size() % 10 == 0) {
                    page++;
                    position = mCardList.size() - 1;
                    getStoryData();
                } else {
                    TextView seeMore = (TextView) foot.findViewById(R.id.see_more_tv);
                    seeMore.setText(R.string.no_more);
                }
            }
        });
        mMyStoryAdapter.setFooterView(foot);

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
                mCardList.addAll(cardInfoList);

                ListUtil.removeDuplicate(mCardList);
                mMyStoryAdapter.notifyDataSetChanged();

                mMvpView.getMyStoryRV().smoothScrollToPosition(position);
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
        mMvpView.getRefreshLayout().setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mCardList.clear();
                page = 1;
                getStoryData();
            }
        });
    }
}