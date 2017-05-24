package com.storyshu.storyshu.fragement;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;

import com.storyshu.storyshu.R;
import com.storyshu.storyshu.activity.story.StoryRoomActivity;
import com.storyshu.storyshu.bean.getStory.StoryIdBean;
import com.storyshu.storyshu.mvp.message.MessagePresenterIml;
import com.storyshu.storyshu.mvp.message.MessageView;
import com.storyshu.storyshu.utils.NameUtil;
import com.storyshu.storyshu.utils.ToastUtil;
import com.storyshu.storyshu.widget.IExpandableListView;

/**
 * 消息；
 * Created by bear on 2017/3/13.
 */

public class MessageFragment extends IBaseStatusFragment implements MessageView {
    private IExpandableListView mLikeListView; //我收到的赞的折叠列表
    private IExpandableListView mCommentListView; //我收到的评论的折叠列表
    private IExpandableListView mSystemListView; //我收到的系统消息的折叠列表
    private MessagePresenterIml mMessagePresenter; //mvp的代理人
    private SwipeRefreshLayout mRefreshLayout; //下拉刷新

    @Override
    public int getLayoutRes() {
        return R.layout.message_layout;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        if (mRootView == null)
            return;

        //状态栏
        setStatusBackgroundColor(R.color.colorRed);

        mRefreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.refresh_layout);

        //折叠列表
        mLikeListView = (IExpandableListView) mRootView.findViewById(R.id.expand_like_list_view);
        mCommentListView = (IExpandableListView) mRootView.findViewById(R.id.expand_comment_list_view);
        mSystemListView = (IExpandableListView) mRootView.findViewById(R.id.expand_system_list_view);

        mMessagePresenter = new MessagePresenterIml(getContext(), this);
    }

    @Override
    public void initData() {
        mMessagePresenter.getMessageData();
    }

    @Override
    public void initEvents() {
        mRefreshLayout.setColorSchemeResources(R.color.colorRedLight, R.color.colorRed);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mMessagePresenter.getMessageData();
            }
        });
    }

    @Override
    public void onDestroy() {
        mMessagePresenter.distach();
        super.onDestroy();
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
    public SwipeRefreshLayout getRefreshLayout() {
        return mRefreshLayout;
    }

    @Override
    public IExpandableListView getLikeMessageList() {
        return mLikeListView;
    }

    @Override
    public IExpandableListView getCommentMessageList() {
        return mCommentListView;
    }

    @Override
    public IExpandableListView getSystemMessageList() {
        return mSystemListView;
    }

    @Override
    public void intent2StoryRoom(StoryIdBean storyIdBean) {
        intentWithParcelable(StoryRoomActivity.class, NameUtil.STORY_ID_BEAN, storyIdBean);
    }
}
