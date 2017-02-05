package com.storyshu.storyshu.model.location;

import android.content.Context;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
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
    private boolean isMapLoaded = false; //地图加载完毕

    private ILocationQueryTool mLocationQueryTool; //位置搜索工具

    private AMapLocation mAmapLocation; //位置数据
    private LatLng mLatLng = new LatLng(0, 0); //位置数据
    private int mRadius = 5; //搜索半径
    private List<PoiItem> poiItemList; //搜索的结果

    private LatLng oldLatlng; //上一次的位置数据

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
     * 地图加载完毕的接口回调
     */
    private AMap.OnMapLoadedListener onMapLoadedListener = new AMap.OnMapLoadedListener() {
        @Override
        public void onMapLoaded() {
            animate2CurrentPosition();
            //显示故事集
//            Log.i(TAG, "onMapLoaded: 显示故事集");
//            StoryDateBaseHelper storyDateBaseHelper = new StoryDateBaseHelper(mAppContext);
//            List<StoryInfo> storyList = storyDateBaseHelper.getLocalStory();
//            Log.d(TAG, "onMapLoaded: 故事集的大小：" + storyList.size());
//            for (StoryInfo storyInfo : storyList) {
//                mMapManager.showBookIcon(storyInfo.getLatLng(), storyInfo.getTitle(), storyInfo.getDetailPic());
//            }
            isMapLoaded = true;
        }
    };

    /**
     * 开始定位
     */
    public void start() {
        if (mAppContext == null || mMapView == null) {
            Log.e(TAG, "start: 定位启动失败！");
            return;
        }
        if (mLocationSever == null)
            mLocationSever = new ILocationSever(mAppContext);
        if (mMapManager == null) {
            mMapManager = new IMapManager(mAppContext, mMapView);
        }
        mMapView.getMap().setOnMapLoadedListener(onMapLoadedListener);
        //
        mLocationSever.start();
        mLocationSever.setOnLocationChange(this);
        //
        mMapManager.init();
        mMapManager.setOnMarkerClickedListener(this);

        //移动地图到上一次的地点
        mMapManager.animate2Position(ISharePreference.getLatLngData(mAppContext));
        //显示用户图标
        mMapManager.showPersonIcon(ISharePreference.getLatLngData(mAppContext));
    }

    /**
     * 结束定位
     */
    public void stop() {
        //清除定位服务
        if (mLocationSever != null) {
            mLocationSever.stop();
        }
    }

    /**
     * 销毁定位
     */
    public void destroy() {
        mMapView.getMap().clear();
        //清除定位服务
        if (mLocationSever != null) {
            mLocationSever.destroy();
            mLocationSever = null;
        }

        if (mMapManager != null)
            mMapManager = null;
    }

    /**
     * 动画地移动摄像机到当前的位置
     */
    public void animate2CurrentPosition() {
        if (mMapManager == null)
            return;

        mMapManager.animate2Position();
    }
    /**
     * 移动摄像机到当前的位置
     */
    public void move2CurrentPosition() {
        if (mMapManager == null)
            return;

        mMapManager.animate2Position();
    }

    /**
     * 获取当前地点的兴趣的列表
     */
    public List<PoiItem> getPoiList() {
        Log.i(TAG, "getPoiList: " + poiItemList);
        List<PoiItem> list = poiItemList;
        removeDuplicate(list);
        return list;
    }

    /**
     * 移除重复的元素
     *
     * @param list 需要重新排列的元素
     */
    private void removeDuplicate(List<PoiItem> list) {
        if (list == null)
            return;
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = list.size() - 1; j > i; j--) {
                if (list.get(j).getTitle().equals(list.get(i).getTitle())) {
                    list.remove(j);
                }
            }
        }
    }


    @Override
    public void OnClick(Marker marker) {
        if (onLocationMarkerClickListener != null)
            onLocationMarkerClickListener.OnMarkerClick(marker);
    }

    /**
     * 位置逆编码
     */
    private ILocationQueryTool.OnLocationQueryListener onLocationQueryListener = new ILocationQueryTool.OnLocationQueryListener() {
        @Override
        public void onRegeocodeSearched(RegeocodeAddress regeocodeAddress) {
            poiItemList = regeocodeAddress.getPois();
            Log.d(TAG, "onRegeocodeSearched: poiList Size:" + poiItemList.size());
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
        LatLng latLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());


        /**
         * 搜索工具,寻找当前位置的兴趣点
         */
//        if (poiItemList == null || poiItemList.size() == 0)
//            mLocationQueryTool.init(mAmapLocation, onLocationQueryListener).startRegeocodeQuery(mRadius);


        if (mLatLng.equals(latLng) && isMapLoaded)
            return;
        mLatLng = latLng;

        //

        /**
         * 移动地图的显示
         */
        moveMap(latLng);

        /**
         * 设置用户的图标显示
         */
        mMapManager.showPersonIcon(latLng);

        //更新位置信息
        mAmapLocation = aMapLocation;

        //保存位置信息到本地
        saveLatlngPreference(latLng);

    }

    /**
     * 移动地图到当前的位置
     */
    private void moveMap(LatLng latLng) {
        mMapManager.animate2Position(latLng);
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
        void OnMarkerClick(Marker marker);
    }
}