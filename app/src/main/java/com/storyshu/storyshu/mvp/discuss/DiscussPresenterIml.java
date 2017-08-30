package com.storyshu.storyshu.mvp.discuss;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.storyshu.storyshu.R;
import com.storyshu.storyshu.adapter.DiscussAdapter;
import com.storyshu.storyshu.bean.getStory.JoinChatRoomBean;
import com.storyshu.storyshu.model.stories.StoryModel;
import com.storyshu.storyshu.mvp.base.IBasePresenter;
import com.storyshu.storyshu.utils.KeyBordUtil;
import com.storyshu.storyshu.utils.ToastUtil;
import com.storyshu.storyshu.utils.sharepreference.ISharePreference;
import com.storyshu.storyshu.widget.dialog.LoadingDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * mvp
 * 讨论的控制
 * Created by bear on 2017/5/24.
 */

public class DiscussPresenterIml extends IBasePresenter<DiscussView> implements DiscussPresenter {
    private static final String TAG = "DiscussPresenterIml";
    private List<EMMessage> mDiscussList; //讨论列表
    private String mRoomID; //聊天室id
    private DiscussAdapter mDiscussAdapter;
    private LoadingDialog mLoadingDialog;

    public DiscussPresenterIml(Context mContext, DiscussView mvpView) {
        super(mContext, mvpView);
        mDiscussList = new ArrayList<>();

        mDiscussAdapter = new DiscussAdapter(mContext, mDiscussList);
        mMvpView.getDiscussRv().setAdapter(mDiscussAdapter);
        mMvpView.getDiscussRv().setLayoutManager(new LinearLayoutManager(mContext));

        /**
         * 设置滑动列表就隐藏键盘
         */
        mMvpView.getDiscussRv().addOnItemTouchListener(onItemTouchListener);
    }

    private RecyclerView.OnItemTouchListener onItemTouchListener = new RecyclerView.OnItemTouchListener() {
        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            switch (e.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    break;

                case MotionEvent.ACTION_MOVE:
                    break;

                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    KeyBordUtil.hideKeyboard(mContext, mMvpView.getEditText());
                    break;
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    };

    @Override
    public void initDiscussData() {
        //显示loading
        mLoadingDialog = new LoadingDialog(mContext, R.style.TransparentDialogTheme);
        mLoadingDialog.show();

        //获取聊天室id
        StoryModel storyModel = new StoryModel(mContext);
        storyModel.getStoryRoomId(new JoinChatRoomBean(mMvpView.getStoryId(), ISharePreference.getUserId(mContext)),
                new StoryModel.OnStoryRoomIDListener() {
                    @Override
                    public void onSucceed(String roomID) {
                        mRoomID = roomID;
                        mLoadingDialog.dismiss();
                        mMvpView.getRefreshLayout().setRefreshing(false);
                    }

                    @Override
                    public void onFailed(String error) {
                        ToastUtil.Show(mContext, R.string.join_chatroom_failed);
                        mLoadingDialog.dismiss();
                        mMvpView.getRefreshLayout().setRefreshing(false);
                    }
                });

    }

    /**
     * 接收消息接口
     */
    private EMMessageListener mMessageListener = new EMMessageListener() {
        @Override
        public void onMessageReceived(List<EMMessage> list) {
            //收到消息
            Log.i(TAG, "onMessageReceived: ");
            mDiscussList.addAll(list);
            mMvpView.notifyData();


        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> list) {
            Log.i(TAG, "onCmdMessageReceived: ");
        }

        @Override
        public void onMessageRead(List<EMMessage> list) {
            Log.i(TAG, "onMessageRead: ");
        }

        @Override
        public void onMessageDelivered(List<EMMessage> list) {
            Log.i(TAG, "onMessageDelivered: ");
        }

        @Override
        public void onMessageRecalled(List<EMMessage> list) {
            Log.i(TAG, "onMessageRecalled: ");
        }

        @Override
        public void onMessageChanged(EMMessage emMessage, Object o) {
            Log.i(TAG, "onMessageChanged: ");
        }
    };

    /**
     * 添加消息监听
     */
    public void addEMMListener() {
        EMClient.getInstance().chatManager().addMessageListener(mMessageListener);
    }

    /**
     * 移除消息监听
     */
    public void removeEMMListener() {
        EMClient.getInstance().chatManager().removeMessageListener(mMessageListener);
    }


    public void initRefreshLayout() {
        mMvpView.getRefreshLayout().setColorSchemeResources(R.color.colorRed);
        mMvpView.getRefreshLayout().setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initDiscussData();
            }
        });

    }

    @Override
    public void sendMessage() {
        if (mMvpView.getEditText().getText().length() == 0) {
            mMvpView.showToast(R.string.comment_issue_empty);
            return;
        }

        EMMessage message = EMMessage.createTxtSendMessage(mMvpView.getEditText().getText().toString(), mRoomID);
        message.setChatType(EMMessage.ChatType.ChatRoom);

        //发送
        EMClient.getInstance().chatManager().sendMessage(message);

        mDiscussList.add(message);
        mDiscussAdapter.notifyDataSetChanged();

        //滚动到最新的数据
        mMvpView.getDiscussRv().scrollToPosition(mDiscussList.size() - 1);

        mMvpView.getEditText().setText("");

    }

}
