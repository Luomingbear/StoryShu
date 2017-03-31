package com.storyshu.storyshu.model.location;

import android.content.Context;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.storyshu.storyshu.info.StoryInfo;
import com.storyshu.storyshu.utils.sharepreference.ISharePreference;

import java.util.ArrayList;


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

    private LatLng mLatLng = new LatLng(0, 0); //位置数据

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
        if (this.mAppContext == null)
            this.mAppContext = appContext.getApplicationContext();
        if (this.mMapView == null)
            this.mMapView = mapView;
        if (this.mLocationQueryTool == null)
            this.mLocationQueryTool = new ILocationQueryTool(appContext);

        if (mMapManager == null) {
            mMapManager = new IMapManager(mAppContext, mMapView);
            mMapManager.init();
            mMapManager.setOnMarkerClickedListener(this);
        }

        TransparentInfoWindowAdapter adapter = new TransparentInfoWindowAdapter(mAppContext);
        mMapView.getMap().setInfoWindowAdapter(adapter);

        move2CurrentPosition();

        return this;
    }

    /**
     * 是否初始化了
     *
     * @return
     */
    public boolean isInit() {
        if (this.mAppContext == null)
            return false;
        if (this.mMapView == null)
            return false;
        if (this.mLocationQueryTool == null)
            return false;
        return true;
    }


    /**
     * 开始定位
     */
    public void start() {
        Log.i(TAG, "start: 定位管家开始服务！！！");
        if (mAppContext == null || mMapView == null) {
            Log.e(TAG, "start: 定位启动失败！");
            return;
        }
        if (mLocationSever == null)
            mLocationSever = new ILocationSever(mAppContext);

        //先停止上一次的定位
        mLocationSever.stop();

        //定位开始
        mLocationSever.start();
        mLocationSever.setOnLocationChange(this);
        //
        animate2CurrentPosition();

        //显示用户图标
        mMapManager.showMyPositionIcon(ISharePreference.getLatLngData(mAppContext));
    }

    /**
     * 结束定位
     */
    public void pause() {
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

        mMapManager.move2Position();
    }

    /**
     * 显示故事集图标
     *
     * @param storyList
     */
    public void showStoriesIcons(ArrayList<StoryInfo> storyList) {
        if (mMapManager == null || storyList == null)
            return;
        //显示故事集图标
        mMapManager.showStoriesIcons(storyList);
    }

    /**
     * 显示选中的图标
     *
     * @param storyInfo
     */
    public void showSelectedIcon(StoryInfo storyInfo) {
        if (mMapManager == null || storyInfo == null)
            return;
        //显示故事集图标
        mMapManager.showSelectedStoryIcon(storyInfo);
    }

    /**
     * 隐藏选中的图标
     */
    public void hideSelectedIcon() {
        if (mMapManager == null)
            return;
        //显示故事集图标
        mMapManager.hideSelectedStoryIcon();
    }

    /**
     * 显示普通的图标
     *
     * @param storyInfo
     */
    public void showDefIcon(StoryInfo storyInfo) {
        if (mMapManager == null || storyInfo == null)
            return;
        //显示故事集图标
        mMapManager.showStoryIcon(storyInfo);
    }

    @Override
    public void OnClick(Marker marker) {
        if (onLocationMarkerClickListener != null)
            onLocationMarkerClickListener.OnMarkerClick(marker);
    }

    /**
     * 定位服务获取到位置时执行
     *
     * @param aMapLocation 位置数据
     */
    @Override
    public void onLocationChange(AMapLocation aMapLocation) {
        LatLng latLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());

        //如果与上一次的坐标一致则不做任何操作
        if (latLng.longitude == mLatLng.longitude &&
                latLng.latitude == mLatLng.latitude)
            return;
        mLatLng = latLng;

        /**
         * 移动地图的显示
         */
        moveMap(latLng);

        /**
         * 设置用户的图标显示
         */
        mMapManager.showMyPositionIcon(latLng);

        //保存位置信息到本地
        saveLatlngPreference(latLng);
    }

    /**
     * 移动地图到当前的位置
     */
    private void moveMap(LatLng latLng) {
        mMapManager.animate2Position(latLng);
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