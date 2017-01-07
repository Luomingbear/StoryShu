package com.storyshu.storyshu.model.location;

import android.content.Context;
import android.util.Log;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.services.core.LatLonPoint;
import com.storyshu.storyshu.utils.sharepreference.ISharePreference;
import com.storyshu.storyshu.widget.marker.BookMarker;
import com.storyshu.storyshu.widget.marker.PersonMarker;

import java.util.List;

/**
 * mapview的管理者
 * Created by bear on 2016/12/2.
 */

public class IMapManager {
    private static final String TAG = "IMapManager";
    private Context mContext;
    private AMap mAMap; //地图对象
    private LatLng mLatLng; //当前位置经纬度
    private UiSettings mUiSettings; //地图ui设置
    private boolean isFirstZoom = true; //是否首次启动地图

    private int mScaleLevel = 17; //默认的地图缩放比例
    private PersonMarker mPersonMarker; //个人位置点图标
    private List<BookMarker> bookMarkerList; //故事集图标列表

    protected IMapManager() {
    }

    public IMapManager(Context context, MapView mMapView) {
        mContext = context;
        this.mAMap = mMapView.getMap();
        mUiSettings = mAMap.getUiSettings();
    }

    /**
     * 初始化地图
     */
    public void init() {
        if (mAMap == null)
            return;

        /**
         * 显示地图
         */
        LatLng latLng = ISharePreference.getLatLngData(mContext);
        if (latLng.equals(mLatLng) || latLng == null) {
            return;
        }

        /**
         * 点击事件
         */
        mAMap.setOnMarkerClickListener(onMarkerClickListener);

        /**
         * ui
         */
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
        //缩放
        mUiSettings.setZoomGesturesEnabled(false);
        //倾斜
        mUiSettings.setTiltGesturesEnabled(false);
        //移动
//        mUiSettings.setScrollGesturesEnabled(false);
        //

        mUiSettings.setIndoorSwitchEnabled(false);
    }


    private AMap.OnMarkerClickListener onMarkerClickListener = new AMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            if (onMarkerClickedListener != null)
                onMarkerClickedListener.OnClick(marker);
            return false;
        }
    };

    /**
     * 显示位置的图标
     */
    public void showPersonIcon(LatLng personLatLng) {
        if (personLatLng == null)
            return;
        Log.i(TAG, "showPersonIcon: !!!!!!!!!!!!!!!");
        if (mPersonMarker == null)
            mPersonMarker = new PersonMarker(mContext, mAMap, personLatLng);
        else
            mPersonMarker.move(personLatLng);
    }

    /**
     * 显示故事集图标
     */
    public void showBookIcon(LatLonPoint latLonPoint, String title,String bgPath) {
        BookMarker bookMarker = new BookMarker(mContext, mAMap,
                new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude()));
        bookMarker.init(title,bgPath);
        //添加到故事集列表
        bookMarkerList.add(bookMarker);
    }

    /**
     * 显示所有图标
     */
    public void showBookIcons() {
        // TODO: 2016/12/3 获取服务器的数据显示图标
    }


    /**
     * 移动地图到当前位置
     */
    public void move2Position() {
        if (mAMap == null)
            return;
        if (mLatLng == null)
            mLatLng = ISharePreference.getLatLngData(mContext);
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
        mLatLng = latLng;
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

    private OnMarkerClickedListener onMarkerClickedListener;

    public void setOnMarkerClickedListener(OnMarkerClickedListener onMarkerClickListener) {
        this.onMarkerClickedListener = onMarkerClickListener;
    }

    /**
     * 标记点 点击回调
     */
    public interface OnMarkerClickedListener {
        void OnClick(Marker marker);
    }
}