package com.storyshu.storyshu.info;

import android.os.Parcel;
import android.os.Parcelable;

import com.amap.api.maps.model.LatLng;

import java.util.List;

/**
 * 故事卡片信息类
 * Created by bear on 2016/12/4.
 */

public class CardInfo extends StoryInfo implements Parcelable {

    private int likeNum = 0; //点赞数量
    private int opposeNum = 0; //反对数量

    public CardInfo() {

    }

    public CardInfo(int likeNum, int opposeNum) {
        this.likeNum = likeNum;
        this.opposeNum = opposeNum;
    }

    public CardInfo(String storyId, String cover, List<String> storyPic, String content, BaseUserInfo userInfo, String createDate, String destroyTime, String content1, LatLng latLng, String location, boolean isAnonymous, int likeNum, int opposeNum) {
        super(storyId, cover, storyPic, content, userInfo, createDate, destroyTime, content1, latLng, location, isAnonymous);
        this.likeNum = likeNum;
        this.opposeNum = opposeNum;
    }

    public int getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(int likeNum) {
        this.likeNum = likeNum;
    }

    public int getOpposeNum() {
        return opposeNum;
    }

    public void setOpposeNum(int opposeNum) {
        this.opposeNum = opposeNum;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.likeNum);
        dest.writeInt(this.opposeNum);
    }

    protected CardInfo(Parcel in) {
        super(in);
        this.likeNum = in.readInt();
        this.opposeNum = in.readInt();
    }

}
