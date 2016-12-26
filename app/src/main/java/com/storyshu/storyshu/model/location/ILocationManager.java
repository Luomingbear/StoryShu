package com.storyshu.storyshu.model.location;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.storyshu.storyshu.utils.sharepreference.ISharePreference;

import java.util.List;


/**
 * 位置服务的管家,单例
 * Created by bear on 2016/12/2.
 */

public class ILocationManager implements IMapManager.OnMarkerClickedListener, ILocationSever.OnLocationChangeListener {
    private static final String TAG = "ILocationManager";

    private volatile static ILocationManager instance;

    private Context mAppContext; //应用上下文

    private MapView mMapView; //地图视图

    private ILocationSever mLocationSever; //定位服务

    private IMapManager mMapManager; //地图视图管家，定位的数据在地图上更新

    private ILocationQueryTool mLocationQueryTool; //位置搜索工具

    private int mRadius = 10; //搜索半径
    private int maxDistance = 50; //最大距离 单位米

    private LatLng oldLatlng; //上一次的位置数据
    private List<PoiItem> aoiList; //当前位置的兴趣点列表

    /**
     * 获取单例的位置管家，定位和地图都通过这个完成
     *
     * @return
     */
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
        this.mLocationQueryTool = new ILocationQueryTool(appContext);
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
        //
        mLocationSever.start();
        mLocationSever.setOnLocationChange(this);
        //
        mMapManager.init();
        mMapManager.setOnMarkerClickedListener(this);

        //移动地图到上一次的地点
        mMapManager.move2Position(ISharePreference.getLatLngData(mAppContext));

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
    }

    public void move2CurrentPosition() {
        if (mMapManager == null)
            return;

        mMapManager.move2Position();
    }

    /**
     * 获取当前地点的兴趣的列表
     */
    public List<PoiItem> getPoiList() {
        if (aoiList == null)
            return null;
        return aoiList;
    }


    @Override
    public void OnClick(Marker marker) {
        if (onLocationMarkerClickListener != null)
            onLocationMarkerClickListener.OnClick(marker);
    }

    /**
     * 位置逆编码
     */
    private ILocationQueryTool.OnLocationQueryListener onLocationQueryListener = new ILocationQueryTool.OnLocationQueryListener() {
        @Override
        public void onRegeocodeSearched(RegeocodeAddress regeocodeAddress) {
            aoiList = regeocodeAddress.getPois();
        }

        @Override
        public void onGeocodeSearched(List<GeocodeAddress> geocodeAddressList) {

        }
    };

    /**
     * 定位服务获取到位置时执行
     *
     * @param aMapLocation 位置数据
     */
    @Override
    public void onLocationChange(AMapLocation aMapLocation) {
        /**
         * 设置地图的显示
         */
        LatLng latLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
        //
        moveMap(latLng);
        //
        saveLatlngPreference(latLng);
        /**
         * 设置用户的图标显示
         */
        mMapManager.showPersonIcon(latLng);

        /**
         * 搜索工具,寻找当前位置的兴趣点
         */
        mLocationQueryTool.init(aMapLocation, onLocationQueryListener).startRegeocodeQuery(mRadius);
    }

    /**
     * 移动地图到当前的位置
     */
    private void moveMap(LatLng latLng) {
        if (latLng.equals(oldLatlng))
            return;
        mMapManager.move2Position(latLng);
        oldLatlng = latLng;
    }

    /**
     * 保存最近的一次定位数据
     *
     * @param latLng
     */
    private void saveLatlngPreference(LatLng latLng) {
        if (!ISharePreference.getLatLngData(mAppContext).equals(latLng))
            ISharePreference.saveLatLng(mAppContext, latLng);
    }

    private OnLocationMarkerClickListener onLocationMarkerClickListener;

    public ILocationManager setOnLocationMarkerClickListener(OnLocationMarkerClickListener
                                                                     onLocationMarkerClickListener) {
        this.onLocationMarkerClickListener = onLocationMarkerClickListener;
        return this;
    }

    /**
     * 标记点点击回调函数
     */
    public interface OnLocationMarkerClickListener {
        void OnClick(Marker marker);
    }

    private OnLocationGotListener onLOcationGotListener;

//    public ILocationManager setOnLocationGotListener(OnLocationGotListener onLOcationGotListener) {
//        this.onLOcationGotListener = onLOcationGotListener;
//        return this;
//    }

    public interface OnLocationGotListener {
        void onLocationGot(String locationName);
    }
}