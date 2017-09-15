package com.storyshu.storyshu.fragement;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.storyshu.storyshu.R;
import com.storyshu.storyshu.activity.DiscussActivity;
import com.storyshu.storyshu.bean.getStory.StoryIdBean;
import com.storyshu.storyshu.info.MessageTypeInfo;
import com.storyshu.storyshu.mvp.message.MessagePresenterIml;
import com.storyshu.storyshu.mvp.message.MessageView;
import com.storyshu.storyshu.mvp.message_me.MessageMeActivity;
import com.storyshu.storyshu.utils.NameUtil;
import com.storyshu.storyshu.utils.ToastUtil;
import com.storyshu.storyshu.widget.IExpandableListView;

import org.jetbrains.annotations.NotNull;

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


        mMessagePresenter = new MessagePresenterIml(getContext(), this);
    }

    @Override
    public void initData() {
        mMessagePresenter.getMessageData();
    }

    @Override
    public void initEvents() {
        mMessagePresenter.initRefreshLayout();

        mMessagePresenter.addEMMessageListener();
    }

    @Override
    public void onDestroy() {
        mMessagePresenter.distach();
        mMessagePresenter.removeEMMessageListener();
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
        return (SwipeRefreshLayout) mRootView.findViewById(R.id.refresh_layout);
    }

    @NotNull
    @Override
    public RecyclerView getMessageRecyclerView() {
        return (RecyclerView) mRootView.findViewById(R.id.message_rv);

    }

    @Override
    public void notifyDataSetChanged() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getMessageRecyclerView().getAdapter().notifyDataSetChanged();
            }
        });
    }

    @Override
    public void intent2MessageMe(int type) {
        intentWithParcelable(MessageMeActivity.class, NameUtil.MESSAGE_TYPE_INFO, new MessageTypeInfo(type));
    }

    @Override
    public void intent2DiscussRoom(StoryIdBean storyIdBean) {
        intentWithParcelable(DiscussActivity.class, NameUtil.STORY_ID_BEAN, storyIdBean);
    }
}
