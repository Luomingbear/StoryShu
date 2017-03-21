package com.storyshu.storyshu.fragement;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.storyshu.storyshu.R;
import com.storyshu.storyshu.info.AirPortPushInfo;
import com.storyshu.storyshu.mvp.airport.AirportPresenter;
import com.storyshu.storyshu.mvp.airport.AirportPresenterIml;
import com.storyshu.storyshu.mvp.airport.AirportView;

/**
 * 候机厅
 * Created by bear on 2017/3/13.
 */

public class AirportFragment extends IBaseStatusFragment implements AirportView {
    private View mSearchLayout; //搜索的布局，标题栏
    private RecyclerView mPushCardsRV; //推送故事的列表布局
    private AirportPresenter mAirportPresenterIml; //mvp模式的实现

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.airport_layout, container, false);
        mAirportPresenterIml = new AirportPresenterIml(getContext(), this);

        initView();
        initData();
        return mRootView;
    }

    @Override
    public RecyclerView getPushRv() {
        return mPushCardsRV;
    }

    @Override
    public void initView() {
        if (mRootView == null)
            return;

        //状态栏
        setStatusBackgroundColor(R.color.colorBlack);

        mSearchLayout = mRootView.findViewById(R.id.title_view);

        mPushCardsRV = (RecyclerView) mRootView.findViewById(R.id.airport_push_list);
    }

    @Override
    public void initData() {
        mAirportPresenterIml.getPushData();
    }

    @Override
    public void initEvents() {

    }

    @Override
    public AirPortPushInfo getPushCardInfo() {
        return null;
    }
}
