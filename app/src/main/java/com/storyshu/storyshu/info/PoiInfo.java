package com.storyshu.storyshu.info;

import com.amap.api.services.core.LatLonPoint;

/**
 * 兴趣点类，剔除id相同的
 * Created by bear on 2016/12/3.
 */

public class PoiInfo implements Comparable<PoiInfo> {
    private String PoiName; //兴趣点名字
    private int PoiId; //兴趣点id
    private LatLonPoint PoiLatLonPoint; //兴趣点的坐标

    public PoiInfo() {
    }

    public PoiInfo(String poiName, int poiId, LatLonPoint poiLatLonPoint) {
        PoiName = poiName;
        PoiId = poiId;
        PoiLatLonPoint = poiLatLonPoint;
    }

    @Override
    public int compareTo(PoiInfo o) {
        if (o.PoiId < this.PoiId)
            return -1;
        else if (o.PoiId > this.PoiId)
            return 1;
        else
            return 0;
    }

    public String getPoiName() {
        return PoiName;
    }

    public void setPoiName(String poiName) {
        PoiName = poiName;
    }

    public int getPoiId() {
        return PoiId;
    }

    public void setPoiId(int poiId) {
        PoiId = poiId;
    }

    public LatLonPoint getPoiLatLonPoint() {
        return PoiLatLonPoint;
    }

    public void setPoiLatLonPoint(LatLonPoint poiLatLonPoint) {
        PoiLatLonPoint = poiLatLonPoint;
    }
}
