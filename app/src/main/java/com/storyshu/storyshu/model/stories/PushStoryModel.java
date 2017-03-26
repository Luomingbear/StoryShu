package com.storyshu.storyshu.model.stories;

import android.content.Context;

import com.amap.api.services.core.LatLonPoint;
import com.storyshu.storyshu.info.AirPortPushInfo;
import com.storyshu.storyshu.info.UserInfo;
import com.storyshu.storyshu.utils.time.TimeUtils;

import java.util.ArrayList;
import java.util.Date;

/**
 * 获取推送的故事集的数据操作类
 * 根据用户的id和为位置推送
 * Created by bear on 2017/3/20.
 */

public class PushStoryModel {
    private ArrayList<AirPortPushInfo> mPushList; //推送的列表
    private Context mAppContext; //上下文
    private OnPushStoryModelListener onPushStoryModelListener;

    /**
     * 设置推送列表获取监听函数
     *
     * @param onPushStoryModelListener
     */
    public void setOnPushStoryModelListener(OnPushStoryModelListener onPushStoryModelListener) {
        this.onPushStoryModelListener = onPushStoryModelListener;
    }

    /**
     * 获取故事集的接口
     */
    public interface OnPushStoryModelListener {
        /**
         * 当故事集获取成功之后返回
         *
         * @param pushList AirPortPushInfo的列表
         */
        void onDataGotSucceed(ArrayList<AirPortPushInfo> pushList);

        /**
         * 数据获取失败
         */
        void onDataGotFailed();
    }

    public PushStoryModel(Context context) {
        mAppContext = context.getApplicationContext();
    }

    /**
     * 根据用户的id和位置进行推荐
     *
     * @param userId      用户id
     * @param latLonPoint 位置坐标
     */
    public void startGetPushList(int userId, LatLonPoint latLonPoint, OnPushStoryModelListener onPushStoryModelListener) {
        this.onPushStoryModelListener = onPushStoryModelListener;
        // TODO: 2017/3/20 请求服务器数据获得故事集的数据
        getDemoList();
    }

    /**
     * test数据
     */
    private void getDemoList() {
        mPushList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            AirPortPushInfo info = new AirPortPushInfo();
            info.setPushType(AirPortPushInfo.TYPE_STORY);

            UserInfo userInfo = new UserInfo();
            userInfo.setAvatar("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1490018447650&di=022e0edec69bb0710e7855ccd12da777&imgtype=0&src=http%3A%2F%2Ftupian.enterdesk.com%2F2014%2Fxll%2F11%2F15%2F1%2Ftouxiang15.jpg");
            userInfo.setNickname("阿狸");
            userInfo.setUserId(i + 1);
            info.setUserInfo(userInfo);

            info.setCover("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1490026292493&di=947955e35862963dbb6d3ef4fe8b9839&imgtype=0&src=http%3A%2F%2Fimglf2.ph.126.net%2FsUm-Vev0kLAlrjUI7YdATQ%3D%3D%2F3356589097375979828.jpg");
            info.setCreateDate(TimeUtils.convert2TimeText(new Date(System.currentTimeMillis())));
            info.setContent("彼时是星期天，阳光明媚而干燥，简直不像是在香港。车路过香港公园，又路过山顶缆车正下方的佑宁堂。时值11点的礼拜散场，很多人站在街道两边挥手搭车。司机忽然说：“这是个好教堂。”");
            info.setLocation("浙江传媒学院产业园");
            mPushList.add(info);
        }

        mPushList.get(1).setPushType(AirPortPushInfo.TYPE_AD);

        if (onPushStoryModelListener != null)
            onPushStoryModelListener.onDataGotSucceed(mPushList);
    }
}
