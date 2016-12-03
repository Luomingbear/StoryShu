package com.bear.passby.widget.marker;

import android.content.Context;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.model.LatLng;

/**
 * 地图上的标记图标的基础类
 * Created by bear on 2016/12/2.
 */

public class IMarker {
    public Context mContext;
    public AMap mAMap; //地图对象
    public LatLng mLatLng; //经纬度

    protected IMarker() {
    }

    public IMarker(Context mContext, AMap mAMap, LatLng mLatLng) {
        this.mContext = mContext;
        this.mAMap = mAMap;
        this.mLatLng = mLatLng;
    }
}
