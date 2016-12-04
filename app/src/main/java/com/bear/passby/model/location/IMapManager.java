package com.bear.passby.model.location;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.bear.passby.widget.marker.BookMarker;
import com.bear.passby.widget.marker.PersonMarker;

import java.util.ArrayList;
import java.util.List;

/**
 * mapview的管理者
 * Created by bear on 2016/12/2.
 */

public class IMapManager implements ILocationSever.OnLocationChangeListener {
    private static final String TAG = "IMapManager";
    private Context mContext;
    private AMap mAMap; //地图对象
    private LatLng mLatLng; //当前位置经纬度
    private UiSettings mUiSettings; //地图ui设置
    private boolean isFirstZoom = true; //是否首次启动地图

    private int mScaleLevel = 19; //默认的地图缩放比例
    private PersonMarker mPersonMarker; //个人位置点图标
    private ILocationQueryTool mLocationQueryTool; //位置搜索工具
    private int mRadius = 10; //搜索半径
    private int maxDistance = 100; //最大距离 单位米
    private List<BookMarker> bookMarkerList; //故事集图标列表

    protected IMapManager() {
    }

    public IMapManager(Context context, MapView mMapView) {
        mContext = context;
        this.mAMap = mMapView.getMap();
        mUiSettings = mAMap.getUiSettings();
        mLocationQueryTool = new ILocationQueryTool(context);
    }

    /**
     * 设置接口回调
     *
     * @param locationSever
     */
    public void init(ILocationSever locationSever) {
        locationSever.setOnLocationChange(this);
        setUI();
    }

    /**
     * 设置地图上面的ui显示，仅仅是对原始的地图控件的ui进行改变
     */
    private void setUI() {
        if (mUiSettings == null)
            return;

        //隐藏缩放按钮
        mUiSettings.setZoomControlsEnabled(false);
        //旋转
        mUiSettings.setRotateGesturesEnabled(true);
        //
        mUiSettings.setIndoorSwitchEnabled(false);
    }


    private ILocationQueryTool.OnLocationQueryListener onLocationQueryListener = new ILocationQueryTool.OnLocationQueryListener() {
        @Override
        public void onRegeocodeSearched(RegeocodeAddress regeocodeAddress) {
//            List<AoiItem> aoilist = regeocodeAddress.getAois();
//            for (AoiItem aoiItem : aoilist) {
//                Log.i(TAG, "AoiItem: " + aoiItem.getAoiName());
//            }

            /**
             * 假的故事集数据
             */
            List<PoiItem> poiItemList = regeocodeAddress.getPois();
            bookMarkerList = new ArrayList<>();
            for (PoiItem poiItem : poiItemList) {
                LatLng latLng = new LatLng(poiItem.getLatLonPoint().getLatitude(), poiItem.getLatLonPoint().getLongitude());
                if (Math.abs(AMapUtils.calculateLineDistance(mLatLng, latLng)) < maxDistance) {
                    showBookIcon(poiItem.getLatLonPoint(), poiItem.getTitle());
                }
            }

            /**
             *  清除不在范围内的故事集图标
             */
            for (BookMarker bookMarker : bookMarkerList) {
                if (AMapUtils.calculateLineDistance(mLatLng, bookMarker.getmLatLng()) > maxDistance) {
                    bookMarker.destroy();
                    bookMarkerList.remove(bookMarker);
                }
            }
        }

        @Override
        public void onGeocodeSearched(List<GeocodeAddress> geocodeAddressList) {

        }
    };

    private AMap.OnMarkerClickListener onMarkerClickListener = new AMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            if (onMarkerClickedListener != null)
                onMarkerClickedListener.OnClick(marker);
            return false;
        }
    };


    /**
     * 取得位置信息的时候执行
     *
     * @param aMapLocation 位置数据
     */
    @Override
    public void locationChange(AMapLocation aMapLocation) {
        if (mAMap == null)
            return;

        /**
         * 显示地图
         */
        double lat = aMapLocation.getLatitude();
        double lng = aMapLocation.getLongitude();
        LatLng latLng = new LatLng(lat, lng);
        if (latLng.equals(mLatLng)) {
            return;
        }

        /**
         * 初始化搜索工具
         */
        mLocationQueryTool.init(aMapLocation, onLocationQueryListener).startRegeocodeQuery(mRadius);

        /**
         * 图标显示
         */
        mLatLng = latLng;
//        mAMap.clear(); //清除图标数据
        showPersonIcon();

        /**
         * 首次启动移动地图到当前位置，否则不主动移动
         */
        if (isFirstZoom) {
            move2Position(); //移动镜头到当前位置
            isFirstZoom = false;
        }

        /**
         * 点击事件
         */
        mAMap.setOnMarkerClickListener(onMarkerClickListener);

        /**
         * 保存位置信息
         */
        LocationSharedPreference.saveLatLng(mContext, mLatLng);
    }

    /**
     * 显示用户位置的图标
     */
    private void showPersonIcon() {
        if (mPersonMarker == null)
            mPersonMarker = new PersonMarker(mContext, mAMap, mLatLng);
        else
            mPersonMarker.move(mLatLng);
    }

    /**
     * 显示故事集图标
     */
    private void showBookIcon(LatLonPoint latLonPoint, String title) {
        BookMarker bookMarker = new BookMarker(mContext, mAMap,
                new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude()));
        bookMarker.init(title);
        //添加到故事集列表
        bookMarkerList.add(bookMarker);
    }

    /**
     * 显示所有图标
     */
    private void showBookIcons() {
        // TODO: 2016/12/3 获取服务器的数据显示图标
    }

    /**
     * 移动地图到当前位置
     */
    public void move2Position() {
        if (mAMap == null)
            return;
        if (mLatLng == null)
            mLatLng = LocationSharedPreference.getLatLngData(mContext);
        move2Position(mLatLng);
    }

    /**
     * 移动到制定位置
     *
     * @param latLng
     */
    public void move2Position(LatLng latLng) {
        if (mAMap == null)
            return;
        mAMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                new CameraPosition(latLng,
                        mScaleLevel, //新的缩放级别
                        0, //俯仰角0°~45°（垂直与地图时为0）
                        0)),  //偏航角 0~360° (正北方为0)
                750, //移动时间 豪秒
                new AMap.CancelableCallback() {
                    @Override
                    public void onFinish() {

                        //移动完成
                    }

                    @Override
                    public void onCancel() {

                        //移动取消
                    }
                });
    }

    private OnLocationGetListener onLocationGetListener;

    public void setOnLocationGetListener(OnLocationGetListener onLocationGetListener) {
        this.onLocationGetListener = onLocationGetListener;
    }

    /**
     * 定位获取结果反馈
     */
    public interface OnLocationGetListener {
        void onSucceed();

        void onFailed();
    }

    private OnMarkerClickedListener onMarkerClickedListener;

    public void setOnMarkerClickedListener(OnMarkerClickedListener onMarkerClickListener) {
        this.onMarkerClickedListener = onMarkerClickListener;
    }

    /**
     * 标记点点击回调
     */
    public interface OnMarkerClickedListener {
        void OnClick(Marker marker);
    }
}