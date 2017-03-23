package com.storyshu.storyshu.imagepicker;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by bear on 2017/3/22.
 */

public class PickerInfo implements Parcelable {

    public static final int MODE_IMAGE = 1;
    public static final int MODE_AVATAR = 2;

    private int maxCount = 1;
    private int pickMode = MODE_IMAGE;
    private int rowCount = 4;
    private boolean showCamera = false;
    private String avatarFilePath;
    private ArrayList<String> selected;

    public PickerInfo() {
    }

    public PickerInfo(int maxCount, int pickMode, int rowCount, boolean showCamera,
                      String avatarFilePath, ArrayList<String> selected) {
        this.maxCount = maxCount;
        this.pickMode = pickMode;
        this.rowCount = rowCount;
        this.showCamera = showCamera;
        this.avatarFilePath = avatarFilePath;
        this.selected = selected;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    public int getPickMode() {
        return pickMode;
    }

    public void setPickMode(int pickMode) {
        this.pickMode = pickMode;
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public boolean isShowCamera() {
        return showCamera;
    }

    public void setShowCamera(boolean showCamera) {
        this.showCamera = showCamera;
    }

    public String getAvatarFilePath() {
        return avatarFilePath;
    }

    public void setAvatarFilePath(String avatarFilePath) {
        this.avatarFilePath = avatarFilePath;
    }

    public ArrayList<String> getSelected() {
        return selected;
    }

    public void setSelected(ArrayList<String> selected) {
        this.selected = selected;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.maxCount);
        dest.writeInt(this.pickMode);
        dest.writeInt(this.rowCount);
        dest.writeByte(this.showCamera ? (byte) 1 : (byte) 0);
        dest.writeString(this.avatarFilePath);
        dest.writeStringList(this.selected);
    }

    protected PickerInfo(Parcel in) {
        this.maxCount = in.readInt();
        this.pickMode = in.readInt();
        this.rowCount = in.readInt();
        this.showCamera = in.readByte() != 0;
        this.avatarFilePath = in.readString();
        this.selected = in.createStringArrayList();
    }

    public static final Parcelable.Creator<PickerInfo> CREATOR = new Parcelable.Creator<PickerInfo>() {
        @Override
        public PickerInfo createFromParcel(Parcel source) {
            return new PickerInfo(source);
        }

        @Override
        public PickerInfo[] newArray(int size) {
            return new PickerInfo[size];
        }
    };
}
