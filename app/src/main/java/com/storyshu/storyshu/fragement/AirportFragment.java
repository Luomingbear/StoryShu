package com.storyshu.storyshu.fragement;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.storyshu.storyshu.R;
import com.storyshu.storyshu.activity.story.SearchLocationActivity;
import com.storyshu.storyshu.activity.story.StoryRoomActivity;
import com.storyshu.storyshu.bean.getStory.StoryBean;
import com.storyshu.storyshu.info.AirPortPushInfo;
import com.storyshu.storyshu.mvp.airport.AirportPresenterIml;
import com.storyshu.storyshu.mvp.airport.AirportView;
import com.storyshu.storyshu.utils.NameUtil;
import com.storyshu.storyshu.utils.ToastUtil;

/**
 * 候机厅
 * Created by bear on 2017/3/13.
 */

public class AirportFragment extends IBaseStatusFragment implements AirportView {
    private View mTitleLayout;  //标题栏
    private EditText mSearchEdit; //搜索的输入框
    private RecyclerView mPushCardsRV; //推送故事的列表布局
    private SwipeRefreshLayout mRefreshLayout; //下拉刷新控件
    private AirportPresenterIml mAirportPresenterIml; //mvp模式的实现

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
        //
        mTitleLayout = mRootView.findViewById(R.id.title_layout);

        mRootView.findViewById(R.id.search_edit).setVisibility(View.GONE);
        mRootView.findViewById(R.id.search_text).setVisibility(View.VISIBLE);

        mRefreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.refresh_layout);
        mRefreshLayout.setColorSchemeResources(R.color.colorRedLight, R.color.colorRed);

        mPushCardsRV = (RecyclerView) mRootView.findViewById(R.id.airport_push_list);

        mAirportPresenterIml = new AirportPresenterIml(getContext(), this);
    }

    @Override
    public void onDestroy() {
        mAirportPresenterIml.distach();
        super.onDestroy();
    }

    @Override
    public SwipeRefreshLayout getRefreshLayout() {
        return mRefreshLayout;
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
        //点击标题栏则跳转到搜索页面
        mTitleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent2Search();
            }
        });

        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mAirportPresenterIml.getPushData();
            }
        });
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

    @Override
    public void intent2Search() {
        intentTo(SearchLocationActivity.class);
    }

    @Override
    public void intent2StoryRoom(StoryBean storyBean) {
        intentWithParcelable(StoryRoomActivity.class, NameUtil.STORY_INFO, storyBean);
    }
}
