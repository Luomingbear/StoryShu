package com.storyshu.storyshu.mvp.message;

import android.content.Context;
import android.view.View;
import android.widget.ExpandableListView;

import com.storyshu.storyshu.adapter.MessageExpandableAdapter;
import com.storyshu.storyshu.adapter.SystemMessageAdapter;
import com.storyshu.storyshu.bean.getStory.StoryIdBean;
import com.storyshu.storyshu.bean.read.ReadCommentPostBean;
import com.storyshu.storyshu.bean.read.ReadStoryLikeBean;
import com.storyshu.storyshu.bean.read.ReadStoryLikePostBean;
import com.storyshu.storyshu.info.StoryMessageInfo;
import com.storyshu.storyshu.info.SystemMessageInfo;
import com.storyshu.storyshu.model.MessageModel;
import com.storyshu.storyshu.mvp.base.IBasePresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * mvp模式
 * 消息页面的代理实现
 * Created by bear on 2017/3/21.
 */

public class MessagePresenterIml extends IBasePresenter<MessageView> implements MessagePresenter {
    private static final String TAG = "MessagePresenterIml";
    private MessageModel mMessageModel; //信息更新的model
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
        mLikeList = new ArrayList<>();
        mCommentList = new ArrayList<>();
        mSystemMessageList = new ArrayList<>();

        mLikeExpandableAdapter = new MessageExpandableAdapter(mContext, mLikeList);
        mMvpView.getLikeMessageList().setAdapter(mLikeExpandableAdapter);
        mMvpView.getLikeMessageList().setVisibility(View.GONE);

        mCommentExpandableAdapter = new MessageExpandableAdapter(mContext, mCommentList);
        mMvpView.getCommentMessageList().setAdapter(mCommentExpandableAdapter);
        mMvpView.getCommentMessageList().setVisibility(View.GONE);

        mSystemExpandableAdapter = new SystemMessageAdapter(mContext, mSystemMessageList);
        mMvpView.getSystemMessageList().setAdapter(mSystemExpandableAdapter);
        mMvpView.getSystemMessageList().setVisibility(View.GONE);

        //点击选项
        setClickEvents();
    }

    private MessageModel.OnMessageModelListener messageModelListener = new MessageModel.OnMessageModelListener() {
        @Override
        public void onLikeDataGot(ArrayList<StoryMessageInfo> messageInfoList) {

            mLikeList.clear();
            if (messageInfoList != null && messageInfoList.size() > 0) {
                for (StoryMessageInfo storyMessageInfo : messageInfoList) {
                    mLikeList.add(storyMessageInfo);
                }
            }

            if (mLikeList.size() == 0)
                mMvpView.getLikeMessageList().setVisibility(View.GONE);
            else {
                mMvpView.getLikeMessageList().setVisibility(View.VISIBLE);
                mLikeExpandableAdapter.notifyDataSetChanged();
            }


        }

        @Override
        public void onCommentDataGot(ArrayList<StoryMessageInfo> messageList) {

            mCommentList.clear();
            if (messageList != null && messageList.size() > 0) {
                for (StoryMessageInfo storyMessageInfo : messageList) {
                    mCommentList.add(storyMessageInfo);
                }
            }

            if (mCommentList.size() == 0)
                mMvpView.getCommentMessageList().setVisibility(View.GONE);
            else {
                mMvpView.getCommentMessageList().setVisibility(View.VISIBLE);
                mCommentExpandableAdapter.notifyDataSetChanged();
            }

        }

        @Override
        public void onSystemDataGot(ArrayList<SystemMessageInfo> messageList) {
            mSystemMessageList = messageList;

            if (mSystemMessageList.size() == 0)
                mMvpView.getSystemMessageList().setVisibility(View.GONE);
            else {
                mMvpView.getSystemMessageList().setVisibility(View.VISIBLE);
                //显示
                showMessageList();
            }

            mMvpView.getRefreshLayout().setRefreshing(false);
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

    /**
     * 标记我收到的赞为已读
     */
    private void readStoryLike() {
        MessageModel messageModel = new MessageModel(mContext);
        List<ReadStoryLikeBean> readStoryLikeBeanList = new ArrayList<>();
        for (StoryMessageInfo storyMessageInfo : mLikeList) {
            readStoryLikeBeanList.add(new ReadStoryLikeBean(storyMessageInfo.getUserInfo().getUserId(),
                    storyMessageInfo.getStoryId()));
        }
        messageModel.updateStoryLikeRead(new ReadStoryLikePostBean(readStoryLikeBeanList));
    }

    /**
     * 标记我收到的评论为已读
     */
    private void readComment() {
        MessageModel messageModel = new MessageModel(mContext);
        List<String> list = new ArrayList<>();
        for (StoryMessageInfo storyMessageInfo : mCommentList) {
            list.add(storyMessageInfo.getCommentId());
        }
        messageModel.updateStoryCommentRead(new ReadCommentPostBean(list));
    }

    /**
     * 初始化点击事件
     */
    private void setClickEvents() {
        //点赞列表
        mMvpView.getLikeMessageList().setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if (mLikeList.size() == 0)
                    return false;

                mLikeList.get(0).setUnReadNum(0);
                mLikeExpandableAdapter.notifyDataSetChanged();
                //
                readStoryLike();
                return false;
            }
        });
        mMvpView.getLikeMessageList().setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                mMvpView.intent2StoryRoom(new StoryIdBean(mLikeList.get(childPosition).getStoryId()));
                return false;
            }
        });

        //评论列表
        mMvpView.getCommentMessageList().setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if (mCommentList.size() == 0)
                    return false;

                mCommentList.get(0).setUnReadNum(0);
                mCommentExpandableAdapter.notifyDataSetChanged();
                //
                readComment();
                return false;
            }
        });
        mMvpView.getCommentMessageList().setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                mMvpView.intent2StoryRoom(new StoryIdBean(mCommentList.get(childPosition).getStoryId()));
                return false;
            }
        });

        //系统信息列表
        mMvpView.getSystemMessageList().setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
//                    mMvpView.intent2StoryRoom(new StoryIdBean(mSystemMessageList.get(childPosition).getStoryId()));
                return false;
            }
        });
    }

    @Override
    public void showMessageList() {

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
