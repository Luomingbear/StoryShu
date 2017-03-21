package com.storyshu.storyshu.fragement;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.storyshu.storyshu.R;
import com.storyshu.storyshu.mvp.message.MessagePresenterIml;
import com.storyshu.storyshu.mvp.message.MessageView;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mRootView = LayoutInflater.from(getContext()).inflate(R.layout.message_layout,
                container, false);
        initView();
        mMessagePresenter = new MessagePresenterIml(getContext(), MessageFragment.this);
        initData();

        return mRootView;
    }

    @Override
    public void initView() {
        if (mRootView == null)
            return;

        //状态栏
        setStatusBackgroundColor(R.color.colorGoldLight);

        //折叠列表
        mLikeListView = (IExpandableListView) mRootView.findViewById(R.id.expand_like_list_view);
        mCommentListView = (IExpandableListView) mRootView.findViewById(R.id.expand_comment_list_view);
        mSystemListView = (IExpandableListView) mRootView.findViewById(R.id.expand_system_list_view);
    }

    @Override
    public void initData() {
        mMessagePresenter.getMessageData();
    }

    @Override
    public void initEvents() {

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
}
