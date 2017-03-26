package com.storyshu.storyshu.model.location;

import android.content.Context;
import android.util.Log;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.services.core.LatLonPoint;
import com.storyshu.storyshu.info.StoryInfo;
import com.storyshu.storyshu.utils.sharepreference.ISharePreference;
import com.storyshu.storyshu.widget.marker.MyCircleMarker;
import com.storyshu.storyshu.widget.marker.PersonMarker;

import java.util.ArrayList;
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

    private int mZoomLevel = 17; //默认的地图缩放比例
    private MyCircleMarker mMyPositionMarker; //个人位置点图标
    private List<PersonMarker> bookMarkerList; //故事集图标列表

    protected IMapManager() {
    }

    public IMapManager(Context context, MapView mMapView) {
        mContext = context.getApplicationContext();
        this.mAMap = mMapView.getMap();
        mUiSettings = mAMap.getUiSettings();
    }

    /**
     * 设置夜间模式
     */
    public void setNightMode() {
        mAMap.setMapType(AMap.MAP_TYPE_NIGHT);
    }

    /**
     * 设置日间模式
     */
    public void setDayMode() {
        mAMap.setMapType(AMap.MAP_TYPE_NORMAL);
    }

    /**
     * 初始化地图
     */
    public void init() {
        if (mAMap == null)
            return;

//        if (ISharePreference.isNightMode(mContext))
//            setNightMode();
//        else setDayMode();

        /**
         * 显示地图
         */
        LatLng latLng = ISharePreference.getLatLngData(mContext);
        if (latLng == null || latLng.equals(mLatLng)) {
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

        //不显示文字
        mAMap.showMapText(false);

        //隐藏缩放按钮
        mUiSettings.setZoomControlsEnabled(false);
        //旋转
        mUiSettings.setRotateGesturesEnabled(false);
        //缩放
//        mUiSettings.setZoomGesturesEnabled(false);
        //倾斜
        mUiSettings.setTiltGesturesEnabled(false);
        //移动
//        mUiSettings.setScrollGesturesEnabled(false);
        //
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
     * 显示用户自己位置的图标
     */
    public void showMyPositionIcon(LatLonPoint personLatLng) {
        if (personLatLng == null)
            return;
        Log.i(TAG, "showMyPositionIcon: !!!!!!!!!!!!!!!");
        if (mMyPositionMarker == null)
            mMyPositionMarker = new MyCircleMarker(mContext, mAMap,
                    new LatLng(personLatLng.getLatitude(), personLatLng.getLongitude()));

        if (AMapUtils.calculateLineDistance(mMyPositionMarker.getLatLng(), mLatLng) > 200) {
            mAMap.clear();
            mMyPositionMarker = new MyCircleMarker(mContext, mAMap,
                    new LatLng(personLatLng.getLatitude(), personLatLng.getLongitude()));
        } else
            mMyPositionMarker.animate(new LatLng(personLatLng.getLatitude(),
                    personLatLng.getLongitude()));
    }

    /**
     * 显示故事图标
     */
    public void showStoryIcon(LatLonPoint latLonPoint, String content, String avatar) {
        PersonMarker personMarker = new PersonMarker(mContext, mAMap,
                new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude()));
        personMarker.setAvatarAndShow(avatar);
        //添加到故事集列表
//        bookMarkerList.add(bookMarker);
    }

    /**
     * 显示所有图标
     */
    public void showStoriesIcons(ArrayList<StoryInfo> storyList) {
        for (StoryInfo storyInfo : storyList) {
            showStoryIcon(storyInfo.getLatLng(), storyInfo.getContent(),
                    storyInfo.getUserInfo().getAvatar());
        }
    }


    /**
     * 移动地图到当前位置
     */
    public void animate2Position() {
        if (mAMap == null)
            return;
        if (mLatLng == null)
            mLatLng = ISharePreference.getLatLngData(mContext);
        animate2Position(mLatLng);
    }

    /**
     * 移动到制定位置
     *
     * @param latLng
     */
    public void animate2Position(LatLng latLng) {
        if (mAMap == null)
            return;
        mLatLng = latLng;
        mAMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                new CameraPosition(latLng,
                        mZoomLevel, //新的缩放级别
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

    /**
     * 移动摄像机到当前的位置
     */
    public void move2Position() {
        move2Position(mLatLng);
    }

    /**
     * 马上移动摄像机到指定位置
     *
     * @param latLng
     */
    public void move2Position(LatLng latLng) {
        if (mAMap == null)
            return;
        mLatLng = latLng;
        mAMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(
                mLatLng,
                mZoomLevel,
                0,
                0
        )));
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