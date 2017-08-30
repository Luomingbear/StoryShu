package com.storyshu.storyshu.model.discuss;

import android.content.Context;
import android.text.TextUtils;

import com.storyshu.storyshu.R;
import com.storyshu.storyshu.utils.ToastUtil;

/**
 * 讨论的数据管理
 * Created by bear on 2017/5/25.
 */

public class DiscussModel {

    private Context mContext;
    private String mRoomId; //聊天室id

    private OnDiscussMessageListener onDisscusMessageListener;

    public interface OnDiscussMessageListener {
        /**
         * 接受到的消息
         *
         * @param received
         */
        void onReceived(String received);
    }

    public void setOnDisscusMessageListener(OnDiscussMessageListener onDisscusMessageListener) {
        this.onDisscusMessageListener = onDisscusMessageListener;
    }

    public DiscussModel(Context mContext, String roomId) {
        this.mContext = mContext;
        this.mRoomId = roomId;
    }

    /**
     * 发送信息到服务器
     *
     * @param msg
     */
    public void sendMessage(String msg) {
        if (TextUtils.isEmpty(mRoomId)) {
            ToastUtil.Show(mContext, R.string.send_message_failed);
            return;
        }


    }
}
