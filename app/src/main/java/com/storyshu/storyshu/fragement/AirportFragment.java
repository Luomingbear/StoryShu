package com.storyshu.storyshu.fragement;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.storyshu.storyshu.R;
import com.storyshu.storyshu.info.AirPortPushInfo;
import com.storyshu.storyshu.mvp.airport.AirportPresenter;
import com.storyshu.storyshu.mvp.airport.AirportPresenterIml;
import com.storyshu.storyshu.mvp.airport.AirportView;
import com.storyshu.storyshu.utils.ToastUtil;

/**
 * 候机厅
 * Created by bear on 2017/3/13.
 */

public class AirportFragment extends IBaseStatusFragment implements AirportView {
    private View mSearchLayout; //搜索的布局，标题栏
    private RecyclerView mPushCardsRV; //推送故事的列表布局
    private AirportPresenter mAirportPresenterIml; //mvp模式的实现

    @Override
    public int getLayoutRes() {
        return R.layout.airport_layout;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        if (mRootView == null)
            return;

        //状态栏
        setStatusBackgroundColor(R.color.colorBlack);

        mSearchLayout = mRootView.findViewById(R.id.title_view);

        mPushCardsRV = (RecyclerView) mRootView.findViewById(R.id.airport_push_list);

        mAirportPresenterIml = new AirportPresenterIml(getContext(), this);
    }

    @Override
    public RecyclerView getPushRv() {
        return mPushCardsRV;
    }


    @Override
    public void initData() {
        mAirportPresenterIml.getPushData();
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
    public AirPortPushInfo getPushCardInfo() {
        return null;
    }
}
