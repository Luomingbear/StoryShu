package com.storyshu.storyshu.model;

import android.content.Context;

import com.storyshu.storyshu.info.StoryMessageInfo;
import com.storyshu.storyshu.info.SystemMessageInfo;
import com.storyshu.storyshu.info.UserInfo;
import com.storyshu.storyshu.utils.time.TimeUtils;

import java.util.ArrayList;
import java.util.Date;

/**
 * "与我有关的信息"的数据操作
 * Created by bear on 2017/3/21.
 */

public class MessageModel {
    private Context mContext;
    private OnMessageModelListener onMessageModelListener;

    public void setOnMessageModelListener(OnMessageModelListener onMessageModelListener) {
        this.onMessageModelListener = onMessageModelListener;
    }

    /**
     * 获取与我有关信息的回调函数
     */
    public interface OnMessageModelListener {
        void onLikeDataGot(ArrayList<StoryMessageInfo> messageList);

        void onCommentDataGot(ArrayList<StoryMessageInfo> messageList);

        void onSystemDataGot(ArrayList<SystemMessageInfo> messageList);
    }

    public MessageModel(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 获取点赞的列表数据
     */
    private void getLikeList() {
        ArrayList<StoryMessageInfo> likeList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            StoryMessageInfo info = new StoryMessageInfo();
            UserInfo userInfo = new UserInfo();
            userInfo.setNickname("西瓜su" + i);
            userInfo.setUserId(i + 1);
            userInfo.setAvatar("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1490098140866&di=aeb868c2a86cbb95f356868dcb0e8c2d&imgtype=0&src=http%3A%2F%2Fup.qqjia.com%2Fz%2F24%2Ftu29448_13.jpg");
            info.setUserInfo(userInfo);
            info.setCreateTime(TimeUtils.convert2TimeText(new Date(System.currentTimeMillis())));
            info.setStoryContent("每个人的生命里都有想要逃离的日常，唯有碍于良善才能带我们找到真正的温暖");
            info.setMessageType(StoryMessageInfo.MessageType.LIKE);
            info.setCover("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1490099987302&di=fbc6bb7d2af5aa5ed8456f4abbe1be06&imgtype=0&src=http%3A%2F%2Fc15.eoemarket.net%2Fapp0%2F652%2F652549%2Fscreen%2F3260012.png");
            likeList.add(info);
        }

        likeList.get(2).setCover("");
        likeList.get(3).setCover("");
        if (onMessageModelListener != null)
            onMessageModelListener.onLikeDataGot(likeList);
    }

    /**
     * 获取评论数据
     */
    private void getCommentList() {
        ArrayList<StoryMessageInfo> commentList = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            StoryMessageInfo info = new StoryMessageInfo();
            UserInfo userInfo = new UserInfo();
            userInfo.setNickname("西瓜su" + i);
            userInfo.setUserId(i + 1);
            userInfo.setAvatar("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1490098140866&di=aeb868c2a86cbb95f356868dcb0e8c2d&imgtype=0&src=http%3A%2F%2Fup.qqjia.com%2Fz%2F24%2Ftu29448_13.jpg");
            info.setUserInfo(userInfo);
            info.setCreateTime(TimeUtils.getCurrentTime());
            info.setStoryContent("每个人的生命里都有想要逃离的日常，唯有碍于良善才能带我们找到真正的温暖");
            info.setMessageType(StoryMessageInfo.MessageType.COMMENT);
            info.setComment("喜欢你的故事，喜欢");
            info.setCover("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1490099987302&di=fbc6bb7d2af5aa5ed8456f4abbe1be06&imgtype=0&src=http%3A%2F%2Fc15.eoemarket.net%2Fapp0%2F652%2F652549%2Fscreen%2F3260012.png");
            commentList.add(info);
        }

        commentList.get(1).setCover("");
        if (onMessageModelListener != null)
            onMessageModelListener.onCommentDataGot(commentList);
    }

    /**
     * 获取系统消息列表
     */
    private void getSystemList() {
        ArrayList<SystemMessageInfo> systemList = new ArrayList<>();

        for (int i = 0; i < 1; i++) {
            SystemMessageInfo info = new SystemMessageInfo();
            info.setCreateTime(TimeUtils.getCurrentTime());
            info.setDescribe("不亦说乎网络科技有限公司在纳斯达克上市了，为了报答每一位用户，送每人一辆跑车！");
            info.setUrl("www.storyshu.com");
            systemList.add(info);
        }
        if (onMessageModelListener != null)
            onMessageModelListener.onSystemDataGot(systemList);
    }

    /**
     * 更新"我的信息"
     */
    public void updateMessageData(OnMessageModelListener onMessageModelListener) {
        this.onMessageModelListener = onMessageModelListener;
        getLikeList();
        getCommentList();
        getSystemList();
    }
}
