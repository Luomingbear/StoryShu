package com.storyshu.storyshu.mvp.message;

import android.content.Context;
import android.view.View;
import android.widget.ExpandableListView;

import com.storyshu.storyshu.adapter.MessageExpandableAdapter;
import com.storyshu.storyshu.adapter.SystemMessageAdapter;
import com.storyshu.storyshu.bean.StoryIdBean;
import com.storyshu.storyshu.info.StoryMessageInfo;
import com.storyshu.storyshu.info.SystemMessageInfo;
import com.storyshu.storyshu.model.MessageModel;
import com.storyshu.storyshu.mvp.base.IBasePresenter;

import java.util.ArrayList;

/**
 * mvp模式
 * 消息页面的代理实现
 * Created by bear on 2017/3/21.
 */

public class MessagePresenterIml extends IBasePresenter<MessageView> implements MessagePresenter {
    private MessageModel mMessageModel; //信息更新的model
    private ArrayList<StoryMessageInfo.MessageType> mGroupList; //组别列表
    private ArrayList<StoryMessageInfo> mLikeList; //喜欢我的列表
    private ArrayList<StoryMessageInfo> mCommentList; //评论我的列表
    private ArrayList<SystemMessageInfo> mSystemMessageList; //系统消息的列表
    private MessageExpandableAdapter mLikeExpandableAdapter; //点赞显示适配器
    private MessageExpandableAdapter mCommentExpandableAdapter; //评论显示适配器
    private SystemMessageAdapter mSystemExpandableAdapter; //系统信息显示适配器

    public MessagePresenterIml(Context mContext, MessageView mvpView) {
        super(mContext, mvpView);
        init();
    }

    private void init() {
        mGroupList = new ArrayList<>();
    }

    private MessageModel.OnMessageModelListener messageModelListener = new MessageModel.OnMessageModelListener() {
        @Override
        public void onLikeDataGot(ArrayList<StoryMessageInfo> messageInfoList) {
            mLikeList = messageInfoList;
            mGroupList.add(StoryMessageInfo.MessageType.LIKE);
        }

        @Override
        public void onCommentDataGot(ArrayList<StoryMessageInfo> messageList) {
            mCommentList = messageList;
            mGroupList.add(StoryMessageInfo.MessageType.COMMENT);
        }

        @Override
        public void onSystemDataGot(ArrayList<SystemMessageInfo> messageList) {
            mSystemMessageList = messageList;
            showMessageList();
        }
    };

    /**
     * 获取消息数据
     */
    @Override
    public void getMessageData() {
        //获取消息列表
        mMessageModel = new MessageModel(mContext);
        mMessageModel.updateMessageData(messageModelListener);
    }

    private void setClickEvents() {
        //点赞列表
        mMvpView.getLikeMessageList().setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                mMvpView.intent2StoryRoom(new StoryIdBean(mLikeList.get(childPosition).getStoryId()));
                return false;
            }
        });

        //评论列表
        mMvpView.getCommentMessageList().setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                mMvpView.intent2StoryRoom(new StoryIdBean(mLikeList.get(childPosition).getStoryId()));

                return false;
            }
        });

        //系统信息列表
        mMvpView.getSystemMessageList().setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                mMvpView.intent2StoryRoom(new StoryIdBean(mLikeList.get(childPosition).getStoryId()));

                return false;
            }
        });
    }

    @Override
    public void showMessageList() {
        mLikeExpandableAdapter = new MessageExpandableAdapter(mContext, mLikeList);
        mMvpView.getLikeMessageList().setAdapter(mLikeExpandableAdapter);

        mCommentExpandableAdapter = new MessageExpandableAdapter(mContext, mCommentList);
        mMvpView.getCommentMessageList().setAdapter(mCommentExpandableAdapter);

        mSystemExpandableAdapter = new SystemMessageAdapter(mContext, mSystemMessageList);
        mMvpView.getSystemMessageList().setAdapter(mSystemExpandableAdapter);

        setClickEvents();
    }

    @Override
    public void unFoldList(ListType listType) {

    }

    @Override
    public void FoldList(ListType listType) {

    }

    @Override
    public void toStoryRoom() {

    }

    @Override
    public void toComputerMessage() {

    }
}
