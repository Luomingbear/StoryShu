package com.storyshu.storyshu.model.stories;

import android.content.Context;

import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.storyshu.storyshu.info.StoryInfo;
import com.storyshu.storyshu.info.UserInfo;
import com.storyshu.storyshu.utils.sharepreference.ISharePreference;
import com.storyshu.storyshu.utils.time.TimeUtils;

import java.util.ArrayList;

/**
 * mvp模式
 * 故事数据的获取
 * Created by bear on 2017/3/26.
 */

public class StoryModel {
    private Context mContext;
    private OnStoryModelListener onStoryModelListener;
    private UserInfo mUser; //用户

    /**
     * 设置获取故事的监听
     *
     * @return
     */
    public OnStoryModelListener getOnStoryModelListener() {
        return onStoryModelListener;
    }

    public interface OnStoryModelListener {
        void onStoriesGot(ArrayList<StoryInfo> storyList);
    }

    public StoryModel(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 请求服务器
     */
    private void startGetStories() {
        // TODO: 2017/3/26 请求服务器的数据
        ArrayList<StoryInfo> list = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            StoryInfo cardInfo = new StoryInfo();

            UserInfo userInfo = new UserInfo();
            userInfo.setAvatar("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1490018447650&di=022e0edec69bb0710e7855ccd12da777&imgtype=0&src=http%3A%2F%2Ftupian.enterdesk.com%2F2014%2Fxll%2F11%2F15%2F1%2Ftouxiang15.jpg");
            userInfo.setUserId(i + 1);
            userInfo.setNickname("三月" + i);
            cardInfo.setUserInfo(userInfo);

            cardInfo.setCover("http://img.hb.aicdn.com/76804c96de76c13e841d06858f70117a9eb39fbb5b16e7-u3HahK_fw658");
            cardInfo.setLocation("浙江传媒学院产业园");
            cardInfo.setContent("彼时是星期天，阳光明媚而干燥，简直不像是在香港。车路过香港公园，又路过山顶缆车正下方的佑宁堂。时值11点的礼拜散场，很多人站在街道两边挥手搭车。司机忽然说：“这是个好教堂。");
            cardInfo.setCreateDate(TimeUtils.getCurrentTime());
            cardInfo.setLifeTime(24);
            cardInfo.setLikeNum(2 * i + i);
            cardInfo.setOpposeNum(i + 1);

            //随机坐标
            double lat = ISharePreference.getLatLngPointData(mContext).getLatitude();
            double lng = ISharePreference.getLatLngPointData(mContext).getLongitude();
            lat = lat + Math.random() * 0.001 * (Math.random() > 0.5 ? 1 : -1);
            lng = lng + Math.random() * 0.001 * (Math.random() > 0.5 ? 1 : -1);

            cardInfo.setLatLng(new LatLng(lat, lng));
            list.add(cardInfo);
        }
        list.get(1).getUserInfo().setAvatar("http://img.hb.aicdn.com/29959d9544d6d3d2ca70551a32c1cbfa0f22879a72a0-IrG5Sr_fw658");
        list.get(2).getUserInfo().setAvatar("http://img.hb.aicdn.com/39be071869229a1aaf5a20903e9cbba55df30c281b5c2-RhMK1Y_fw658");
        list.get(3).getUserInfo().setAvatar("http://img.hb.aicdn.com/24b6add9f88748e7208f89d3af85b665cd02e0571325f-XIlpwp_fw658");
        list.get(4).getUserInfo().setAvatar("http://img.hb.aicdn.com/98ec385d465ce72a2e7336892f2a5065a1b342cfe96e-0vRdLP_fw658");
        list.get(5).getUserInfo().setAvatar("http://img.hb.aicdn.com/c7146db3871385a2d13cafb9fd9295260b8145ad3e2d-emRGXC_fw658");

        list.get(1).setCover("http://img.hb.aicdn.com/61742b3ce0bb3346db287b9c475d6c238013ad853387b-Hureyd_fw658");
        list.get(2).setCover("");
        list.get(3).setCover("http://img.hb.aicdn.com/c3e6fc7c483c1b111190e49b404de20cd165b82c38cca-Ap7tnp_fw658");
        list.get(4).setCover("");
        list.get(5).setCover("http://img.hb.aicdn.com/2b66da89a997889d0b27bf836a2cb168bd0323c9570f8-VYtaMq_fw658");

        if (onStoryModelListener != null)
            onStoryModelListener.onStoriesGot(list);
    }

    /**
     * 获取用户附近的故事
     *
     * @param userInfo
     * @param latLonPoint
     */
    public void getNearStories(UserInfo userInfo, LatLonPoint latLonPoint, OnStoryModelListener onStoryModelListener) {
        this.onStoryModelListener = onStoryModelListener;
        startGetStories();
    }
}
