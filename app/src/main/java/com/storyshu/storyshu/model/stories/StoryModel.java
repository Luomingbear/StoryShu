package com.storyshu.storyshu.model.stories;

import android.content.Context;

import com.amap.api.maps.model.LatLng;
import com.storyshu.storyshu.info.StoryInfo;

import java.util.ArrayList;

/**
 * mvp模式
 * 故事数据的获取
 * Created by bear on 2017/3/26.
 */

public class StoryModel {
    private Context mContext;
    private OnStoryModelListener onStoryModelListener;
    private int mUserId; //用户

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
    private void startGetStories(LatLng latLng) {
//        TODO:2017 / 3 / 26 请求服务器的数据

//
//        //假数据
        ArrayList<StoryInfo> list = new ArrayList<>();
//        for (int i = 0; i < 6; i++) {
//            StoryInfo cardInfo = new StoryInfo();
//
//            BaseUserInfo userInfo = new BaseUserInfo();
//            userInfo.setAvatar("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1490018447650&di=022e0edec69bb0710e7855ccd12da777&imgtype=0&src=http%3A%2F%2Ftupian.enterdesk.com%2F2014%2Fxll%2F11%2F15%2F1%2Ftouxiang15.jpg");
//            userInfo.setUserId(i + 11);
//            userInfo.setNickname("三月" + i);
//            cardInfo.setUserInfo(userInfo);
//            cardInfo.setStoryId(userInfo.getUserId() + TimeUtils.getTimeId());
//
//            cardInfo.setLocation("浙江传媒学院");
//            cardInfo.setContent("彼时是星期天，阳光明媚而干燥，简直不像是在香港。车路过香港公园，又路过山顶缆车正下方的佑宁堂。时值11点的礼拜散场，很多人站在街道两边挥手搭车。司机忽然说：“这是个好教堂。");
//            cardInfo.setCreateDate(TimeUtils.getCurrentTime());
//            cardInfo.setDestroyTime(TimeUtils.getDestoryTime((int) (60 * Math.random()) * 24));
//            cardInfo.setLikeNum(2 * i + i);
//            cardInfo.setOpposeNum(i + 1);
//
//            LatLng lnglog = ISharePreference.getLatLngData(mContext);
//            double lat = lnglog.latitude + Math.random() * 0.001 * (Math.random() > 0.5 ? 1 : -1);
//            double lng = lnglog.longitude + Math.random() * 0.001 * (Math.random() > 0.5 ? 1 : -1);
//
//            cardInfo.setLatLng(new LatLng(lat, lng));
//            list.add(cardInfo);
//        }
//        list.get(0).getUserInfo().setNickname("六月");
//        list.get(1).getUserInfo().setNickname("烟花笑");
//        list.get(2).getUserInfo().setNickname("Dilemma");
//        list.get(3).getUserInfo().setNickname("空心");
//        list.get(4).getUserInfo().setNickname("青梅染雪");
//        list.get(5).getUserInfo().setNickname("Estrus");
//
//        list.get(1).getUserInfo().setAvatar("http://img.hb.aicdn.com/29959d9544d6d3d2ca70551a32c1cbfa0f22879a72a0-IrG5Sr_fw658");
//        list.get(2).getUserInfo().setAvatar("http://img.hb.aicdn.com/39be071869229a1aaf5a20903e9cbba55df30c281b5c2-RhMK1Y_fw658");
//        list.get(3).getUserInfo().setAvatar("http://img.hb.aicdn.com/24b6add9f88748e7208f89d3af85b665cd02e0571325f-XIlpwp_fw658");
//        list.get(4).getUserInfo().setAvatar("http://img.hb.aicdn.com/98ec385d465ce72a2e7336892f2a5065a1b342cfe96e-0vRdLP_fw658");
//        list.get(5).getUserInfo().setAvatar("http://img.hb.aicdn.com/9c892392e2dc75598e8c3063d78ee04e5d6956b72181f-PrdDAH_fw658");
//
//        List<String> strings = new ArrayList<String>();
//        strings.add("https://img1.doubanio.com/view/note/large/public/p41953749.jpg");
//        list.get(1).setStoryPic(strings);
//        list.get(2).setCover("");
//        strings.clear();
//        strings.add("http://img.hb.aicdn.com/c3e6fc7c483c1b111190e49b404de20cd165b82c38cca-Ap7tnp_fw658");
//        list.get(3).setStoryPic(strings);
//        strings.clear();
//        strings.add("https://tse4-mm.cn.bing.net/th?id=OIP.78Yog3FoOJio-Y4a50F16QEsEs&w=300&h=300&p=0&pid=1.1");
//        list.get(4).setStoryPic(strings);
//        strings.clear();
//        strings.add("http://img.hb.aicdn.com/2b66da89a997889d0b27bf836a2cb168bd0323c9570f8-VYtaMq_fw658");
//        list.get(5).setStoryPic(strings);
//
//        list.get(1).setContent("自己又遇见一只毛色从未见过的田园猫——银灰色被毛、浅条纹，非常好看。对于我这样拍猫成痴的，能遇见不同花色，或者说独特花色的猫猫，这是最开心的事情。");
//        list.get(2).setContent("大一的时候，他和她在网上认识。他们一见如故，无话不聊。他喜欢逛她的空间，看她的动态。他喜欢她，但他不能说。因为毕业后注定要是北方工作。而她在沿海。一晃四年大学毕业。他短信她，只是轻轻的说了一声：我要去北方工作了。手机震，她回了：爱情是可以一起坐火车的。");
//        list.get(3).setContent("我很好，不吵不闹不炫耀，不要委屈不要嘲笑，也不需要别人知道。");
//        list.get(4).setContent("谁说系列电影不能长寿？故事，永远都是影片的核心，讲好了故事，电影就能绽放它本该有的光彩！");
//        list.get(5).setContent("因为一首歌怀念一座城。曾经的天府广场武侯祠锦里杜甫草堂青羊宫春熙路宽窄巷子昭觉寺文殊院都成了回忆。");
//
//        DateBaseHelperIml dateBaseHelperIml = new DateBaseHelperIml(mContext);
//        for (StoryInfo storyInfo : dateBaseHelperIml.getLifeStory(ISharePreference.getUserId(mContext))) {
//            list.add(storyInfo);
//        }

        if (onStoryModelListener != null)
            onStoryModelListener.onStoriesGot(list);
    }

    /**
     * 获取用户附近的故事
     *
     * @param userId
     * @param latLng
     */
    public void getNearStories(int userId, LatLng latLng, OnStoryModelListener onStoryModelListener) {
        mUserId = userId;
        this.onStoryModelListener = onStoryModelListener;
        startGetStories(latLng);
    }
}
