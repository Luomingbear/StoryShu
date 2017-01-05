package com.storyshu.storyshu.info;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * 故事的正文信息类
 * Created by bear on 2016/12/29.
 */

public class StroryContentInfo implements Parcelable {
    private List<EditLineData> lineDatas;

    public StroryContentInfo() {
    }

    public StroryContentInfo(List<EditLineData> lineDatas) {
        this.lineDatas = lineDatas;
    }

    public List<EditLineData> getLineDatas() {
        return lineDatas;
    }

    public void setLineDatas(List<EditLineData> lineDatas) {
        this.lineDatas = lineDatas;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.lineDatas);
    }

    protected StroryContentInfo(Parcel in) {
        this.lineDatas = new ArrayList<EditLineData>();
        in.readList(this.lineDatas, EditLineData.class.getClassLoader());
    }

    public static final Parcelable.Creator<StroryContentInfo> CREATOR = new Parcelable.Creator<StroryContentInfo>() {
        @Override
        public StroryContentInfo createFromParcel(Parcel source) {
            return new StroryContentInfo(source);
        }

        @Override
        public StroryContentInfo[] newArray(int size) {
            return new StroryContentInfo[size];
        }
    };
}
