package com.storyshu.storyshu.mvp.airport;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

import com.storyshu.storyshu.adapter.AirportAdapter;
import com.storyshu.storyshu.bean.RecommendPostBean;
import com.storyshu.storyshu.bean.getStory.StoryBean;
import com.storyshu.storyshu.info.AirPortPushInfo;
import com.storyshu.storyshu.model.stories.PushStoryModel;
import com.storyshu.storyshu.mvp.base.IBasePresenter;
import com.storyshu.storyshu.utils.sharepreference.ISharePreference;

import java.util.ArrayList;

/**
 * mvp模式
 * 候机厅功能的实现
 * Created by bear on 2017/3/20.
 */

public class AirportPresenterIml extends IBasePresenter<AirportView> implements AirportPresenter {
    private static final String TAG = "AirportPresenterIml";
    private ArrayList<AirPortPushInfo> mPushList; //推送的故事列表
    private AirportAdapter mAirportAdapter; //推送的故事的适配器

    public AirportPresenterIml(Context mContext, AirportView mvpView) {
        super(mContext, mvpView);
        //设置adapter
        mPushList = new ArrayList<>();
        mAirportAdapter = new AirportAdapter(mContext, mPushList);
        //显示数据
        mMvpView.getPushRv().setAdapter(mAirportAdapter);
        mAirportAdapter.setAirportCardClickListener(onAirportCardClickListener);
        mMvpView.getPushRv().setLayoutManager(new LinearLayoutManager(mContext));
    }

    private AirportAdapter.OnAirportCardClickListener onAirportCardClickListener = new AirportAdapter.OnAirportCardClickListener() {
        @Override
        public void onClick(int position) {
            intent2StoryRoom(mPushList.get(position));
        }
    };

    /**
     * 获取推荐信息的接口
     */
    private PushStoryModel.OnPushStoryModelListener pushStoryModelListener = new PushStoryModel.OnPushStoryModelListener() {
        @Override
        public void onDataGotSucceed(ArrayList<AirPortPushInfo> pushList) {
            mPushList.clear();
            for (AirPortPushInfo airPortPushInfo : pushList)
                mPushList.add(airPortPushInfo);
            showPushList();

            mMvpView.getRefreshLayout().setRefreshing(false);
        }

        @Override
        public void onDataGotFailed(String error) {

        }
    };

    @Override
    public void getPushData() {
        PushStoryModel pushStoryModel = new PushStoryModel(mContext);
        pushStoryModel.startGetPushList(new RecommendPostBean(ISharePreference.getUserId(mContext),
                ISharePreference.getCityName(mContext)));
        pushStoryModel.setOnPushStoryModelListener(pushStoryModelListener);
    }

    /**
     * 显示推送数据
     */
    public void showPushList() {
        if (mMvpView.getPushRv() != null) {
            //设置布局类型
            mAirportAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void intent2StoryRoom(StoryBean storyBean) {
        mMvpView.intent2StoryRoom(storyBean);
    }

    @Override
    public void intent2AdActivity() {

    }

    @Override
    public boolean clickLike() {
        return false;
    }

    @Override
    public boolean clickOppose() {
        return false;
    }
}
