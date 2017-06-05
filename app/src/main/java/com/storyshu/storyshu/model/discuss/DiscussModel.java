package com.storyshu.storyshu.model.discuss;

import android.content.Context;

import com.storyshu.storyshu.utils.net.UrlUtil;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * 讨论的数据管理
 * Created by bear on 2017/5/25.
 */

public class DiscussModel {

    private Context mContext;
    private Socket mSocket; //进行socket通信
    private int port = 2293;

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

    public DiscussModel(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 发送信息到服务器
     *
     * @param msg
     */
    public void sendMessage(String msg) {
        try {
            mSocket = new Socket(UrlUtil.BASE_HOST, port);

            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(mSocket.getOutputStream())), true);
            //上传数据
            out.println(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
