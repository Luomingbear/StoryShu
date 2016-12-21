package com.storyshu.storyshu.model.location;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.amap.api.maps.model.LatLng;

/**
 * 保存最近的一次定位数据到本地
 * Created by bear on 2016/12/2.
 */

public class LocationSharedPreference {
    /**
     * 存储的文件名
     */
    public static final String LOCATION_DATA = "locationData";
    public static final String LAT = "lat";
    public static final String LNG = "lng";


    /**
     * 保存最近的一次经纬度
     */
    public static void saveLatLng(Context context, LatLng latLng) {

        SharedPreferences sp = context.getSharedPreferences(LOCATION_DATA,
                Activity.MODE_PRIVATE);
        // 获取Editor对象
        SharedPreferences.Editor editor = sp.edit();

        editor.putFloat(LAT, (float) latLng.latitude);
        editor.putFloat(LNG, (float) latLng.longitude);
        editor.apply();
    }

    /**
     * 获取上次保存的位置信息
     *
     * @return 经纬度
     */
    public static LatLng getLatLngData(Context context) {

        SharedPreferences sp = context.getSharedPreferences(LOCATION_DATA,
                Activity.MODE_PRIVATE);
        LatLng latLng = new LatLng(sp.getFloat(LAT, 1),
                sp.getFloat(LNG, 1));

        return latLng;
    }
}
