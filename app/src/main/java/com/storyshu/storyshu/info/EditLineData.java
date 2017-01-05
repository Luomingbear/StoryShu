package com.storyshu.storyshu.info;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 正文里面的每一段的内容
 * Created by bear on 2016/12/29.
 */

public class EditLineData implements Parcelable {
    private String inputStr;
    private String imagePath;

    public EditLineData() {
    }

    public EditLineData(String inputStr, String iamgePath) {
        this.inputStr = inputStr;
        this.imagePath = iamgePath;
    }

    public String getInputStr() {
        return inputStr;
    }

    public void setInputStr(String inputStr) {
        this.inputStr = inputStr;
    }

    public String getIamgePath() {
        return imagePath;
    }

    public void setIamgePath(String iamgePath) {
        this.imagePath = iamgePath;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.inputStr);
        dest.writeString(this.imagePath);
    }

    protected EditLineData(Parcel in) {
        this.inputStr = in.readString();
        this.imagePath = in.readString();
    }

    public static final Parcelable.Creator<EditLineData> CREATOR = new Parcelable.Creator<EditLineData>() {
        @Override
        public EditLineData createFromParcel(Parcel source) {
            return new EditLineData(source);
        }

        @Override
        public EditLineData[] newArray(int size) {
            return new EditLineData[size];
        }
    };
}
