package com.storyshu.storyshu.model;

/**
 * 只返回一个值的接口
 * Created by bear on 2017/7/8.
 */

public interface OnOnlyDataResponseListener {
    void onSucceed(Object obj);

    void onFalied(String error);
}
