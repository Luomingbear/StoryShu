package com.storyshu.storyshu.activity.story;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.storyshu.storyshu.R;
import com.storyshu.storyshu.activity.base.IBaseActivity;
import com.storyshu.storyshu.info.CardInfo;
import com.storyshu.storyshu.mvp.mystory.MyStoryPresenterIml;
import com.storyshu.storyshu.mvp.mystory.MyStoryView;
import com.storyshu.storyshu.utils.NameUtil;
import com.storyshu.storyshu.utils.StatusBarUtils;
import com.storyshu.storyshu.utils.ToastUtil;
import com.storyshu.storyshu.widget.title.TitleView;

/**
 * 我的故事页面
 * Created by bear on 2017/5/21.
 */

public class MyStoryActivity extends IBaseActivity implements MyStoryView {
    private MyStoryPresenterIml mMyStoryPresenterIml;
    private TitleView mTitleView;
    private SwipeRefreshLayout mRefreshLayout; //刷新控件

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my_story);

        initView();

        mMyStoryPresenterIml = new MyStoryPresenterIml(this, this);

        initEvent();
    }


    private void initView() {
        StatusBarUtils.setColor(this, R.color.colorRed);

        mTitleView = (TitleView) findViewById(R.id.title_view);

        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        mRefreshLayout.setColorSchemeResources(R.color.colorRedLight, R.color.colorRed);
    }

    private void initEvent() {
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

        //获取故事数据
        mMyStoryPresenterIml.getStoryData();

        //
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mMyStoryPresenterIml.getStoryData();
            }
        });

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
    public RecyclerView getMyStoryRV() {
        return (RecyclerView) findViewById(R.id.my_story_rv);
    }

    @Override
    public SwipeRefreshLayout getRefreshLayout() {
        return mRefreshLayout;
    }

    @Override
    public void intent2StoryRoom(CardInfo cardInfo) {
        intentWithParcelable(StoryRoomActivity.class, NameUtil.CARD_INFO, cardInfo);
    }
}
