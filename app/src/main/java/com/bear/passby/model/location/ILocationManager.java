package com.bear.passby.model.location;

import android.content.Context;

import com.amap.api.maps2d.MapView;

/**
 * 位置服务的管家,单例
 * Created by bear on 2016/12/2.
 */

public class ILocationManager {
    private volatile static ILocationManager instance;

    private Context mAppContext; //应用上下文

    private MapView mMapView; //地图视图

    private ILocationSever mLocationSever; //定位服务

    private IMapManager mMapManager; //地图视图管家，定位的数据在地图上更新

    public static ILocationManager getInstance() {
        if (instance == null) {
            synchronized (ILocationManager.class) {
                if (instance == null) {
                    instance = new ILocationManager();
                }
            }
        }
        return instance;
    }

    protected ILocationManager() {
    }

    /**
     * 初始化
     *
     * @param appContext appContext
     */
    public ILocationManager init(Context appContext, MapView mapView) {
        this.mAppContext = appContext;
        this.mMapView = mapView;
        return this;
    }

    /**
     * 开始定位
     */
    public void start() {
        if (mAppContext == null || mMapView == null)
            return;
        if (mLocationSever == null)
            mLocationSever = new ILocationSever(mAppContext);
        if (mMapManager == null) {
            mMapManager = new IMapManager(mAppContext, mMapView);
        }
        mLocationSever.start();
        mMapManager.init(mLocationSever);

        //移动地图到上一次的地点
        mMapManager.move2Position(LocationSharedPreference.getLatLngData(mAppContext));


    }

    /**
     * 结束定位
     */
    public void stop() {
        //清除定位服务
        if (mLocationSever != null) {
            mLocationSever.destroy();
            mLocationSever = null;
        }

        if (mMapManager != null)
            mMapManager = null;
        // TODO: 2016/12/2 销毁定位服务 ，清除缓冲
    }

    public void move2CurrentPosition() {
        if (mMapManager == null)
            return;

        mMapManager.move2Position();
    }
}