package com.storyshu.storyshu.imagepicker;

import android.content.Intent;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.storyshu.storyshu.imagepicker.activity.PhotoPickerActivity;
import com.storyshu.storyshu.imagepicker.util.SystemUtil;
import com.storyshu.storyshu.R;

import java.util.ArrayList;


/**
 * Created by Martin on 2017/1/17.
 */
public class SImagePicker {
    public static String SImagePicker = "SImagePicker";
    private static PickerInfo mPickerInfo;

    private static PickerConfig pickerConfig;

    private Fragment fragment;
    private AppCompatActivity activity;

    private
    @StringRes
    int pickRes = R.string.select;

    private SImagePicker(Fragment fragment) {
        this.fragment = fragment;
    }

    private SImagePicker(AppCompatActivity activity) {
        this.activity = activity;
    }

    public static SImagePicker from(Fragment fragment) {
        return new SImagePicker(fragment);
    }

    public static SImagePicker from(AppCompatActivity activity) {
        return new SImagePicker(activity);
    }

    public static void init(PickerConfig config) {
        pickerConfig = config;
        SystemUtil.init(config.getAppContext());
        mPickerInfo = new PickerInfo();
    }

    public static PickerConfig getPickerConfig() {
        if (pickerConfig == null) {
            throw new IllegalArgumentException("you must call init() first");
        }
        return pickerConfig;
    }

    public SImagePicker maxCount(int maxCount) {
        mPickerInfo.setMaxCount(maxCount);
        return this;
    }

    public SImagePicker rowCount(int rowCount) {
        mPickerInfo.setRowCount(rowCount);
        return this;
    }

    public SImagePicker setSelected(ArrayList<String> selected) {
        mPickerInfo.setSelected(selected);
        return this;
    }

    public SImagePicker pickMode(int mode) {
        mPickerInfo.setPickMode(mode);
        return this;
    }

    public SImagePicker cropFilePath(String filePath) {
        mPickerInfo.setAvatarFilePath(filePath);
        return this;
    }

    public SImagePicker showCamera(boolean showCamera) {
        mPickerInfo.setShowCamera(showCamera);
        return this;
    }

    public SImagePicker pickText(@StringRes int pick) {
        this.pickRes = pick;
        return this;
    }

    public void forResult(int requestCode) {
        if (pickerConfig == null) {
            throw new IllegalArgumentException("you must call init() first");
        }
        Intent intent = new Intent();
        intent.putExtra(SImagePicker, mPickerInfo);
        if (activity != null) {
            intent.setClass(activity, PhotoPickerActivity.class);
            activity.startActivityForResult(intent, requestCode);
        } else if (fragment != null) {
            intent.setClass(fragment.getActivity(), PhotoPickerActivity.class);
            fragment.startActivityForResult(intent, requestCode);
        } else {
            throw new IllegalArgumentException("you must call from() first");
        }
    }


}
