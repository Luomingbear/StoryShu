package com.storyshu.storyshu.model.location;

import android.content.Context;
import android.graphics.Point;
import android.util.Log;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.storyshu.storyshu.info.StoryInfo;
import com.storyshu.storyshu.utils.SysUtils;
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

    private int mZoomLevel = 18; //默认的地图缩放比例
    private MyCircleMarker mMyPositionMarker; //个人位置点图标
    private List<PersonMarker> mPersonMarkerList; //故事集图标列表
    private PersonMarker mSelectedMarker; //显示的选中的图标
    private int markerMinDistence = 5; //图标之间的最小距离，小于这个距离就只会显示一个！

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
        //获得最近一次的位置数据
        mLatLng = ISharePreference.getLatLngData(mContext);

        /**
         * 点击事件
         */
        mAMap.setOnMarkerClickListener(onMarkerClickListener);

        /**
         * ui
         */
        setUI();


        //初始化故事的图标集合
        mPersonMarkerList = new ArrayList<>();
    }

    /**
     * 设置地图上面的ui显示，仅仅是对原始的地图控件的ui进行改变
     */
    private void setUI() {
        if (mUiSettings == null)
            return;

        //不显示文字
        mAMap.showMapText(false);

        //限制显示的范围
//        LatLng southwestLatLng = new LatLng(mLatLng.latitude - 0.01, mLatLng.longitude - 0.01);
//        LatLng northeastLatLng = new LatLng(mLatLng.latitude + 0.01, mLatLng.longitude + 0.01);
//        LatLngBounds latLngBounds = new LatLngBounds(southwestLatLng, northeastLatLng);
//        mAMap.setMapStatusLimits(latLngBounds);

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
    public void showMyPositionIcon(LatLng latLng) {
        if (latLng == null)
            return;
//        Log.i(TAG, "showMyPositionIcon: !!!!!!!!!!!!!!!");
        if (mMyPositionMarker == null)
            mMyPositionMarker = new MyCircleMarker(mContext, mAMap, latLng);

        if (AMapUtils.calculateLineDistance(mMyPositionMarker.getLatLng(), mLatLng) > 200) {
            mAMap.clear();
            mMyPositionMarker = new MyCircleMarker(mContext, mAMap, latLng);
        } else
            mMyPositionMarker.animate(latLng);
    }

    /**
     * 显示故事图标
     */
    public void showStoryIcon(StoryInfo storyInfo) {
        Log.i(TAG, "showStoryIcon: 显示默认的图标");
        //如果选中的图标仍然显示着，就需要判断选中的图标是否是当前的这个图标，是就把他移除
        //不移除会出现图标的叠加现象
        if (mSelectedMarker != null) {
            if (mSelectedMarker.getLatLng().equals(storyInfo.getLatLng())) {
                mSelectedMarker.remove();
                mSelectedMarker = null;
            }
        }

        if (mPersonMarkerList != null && mPersonMarkerList.size() > 0) {
            //移除之前的小图标
            for (PersonMarker personMarker : mPersonMarkerList) {
                //两点之间的距离小于5米则不添加
                Log.i(TAG, "showStoryIcon: 距离：" + AMapUtils.calculateLineDistance(storyInfo.getLatLng(), personMarker.getLatLng()));
                if (AMapUtils.calculateLineDistance(storyInfo.getLatLng(), personMarker.getLatLng()) < markerMinDistence) {
                    return;
                }
            }
        }

        //显示图标
        PersonMarker person = new PersonMarker(mContext, mAMap, storyInfo);
        person.setAvatarAndShow();
        //添加到故事集列表
        mPersonMarkerList.add(person);
    }

    /**
     * 显示选中的故事图标
     */
    public void showSelectedStoryIcon(StoryInfo storyInfo) {
        Log.i(TAG, "showSelectedStoryIcon: 显示大图标");
        //移除之前选中的大图标
        if (mSelectedMarker != null) {
            //故事id不同则移除上一个
            if (!storyInfo.getStoryId().equals(mSelectedMarker.mMarker.getSnippet())) {
                mSelectedMarker.remove();
                mSelectedMarker = null;
            }
        }

        //移除当前选中的小图标
        List<PersonMarker> removeList = new ArrayList<>();
        for (PersonMarker personMarker : mPersonMarkerList) {
            if (AMapUtils.calculateLineDistance(personMarker.getLatLng(), storyInfo.getLatLng()) < markerMinDistence) {
                personMarker.remove();
                removeList.add(personMarker);
            }
        }
        for (PersonMarker p : removeList) {
            if (mPersonMarkerList.contains(p)) {
                mPersonMarkerList.remove(p);
            }
        }


        //添加新的图标
        if (mSelectedMarker == null) {
            PersonMarker personMarker = new PersonMarker(mContext, mAMap, storyInfo);
            personMarker.setAvatarAndShowSelected();
            mSelectedMarker = personMarker;
        }

        //如果新的位置在屏幕外，移动摄像机到新的位置
        Point p = mAMap.getProjection().toScreenLocation(storyInfo.getLatLng());
        if (p.x < 0 || p.x > SysUtils.getScreenWidth(mContext)) {
            move2Position(storyInfo.getLatLng());
        }
    }

    /**
     * 隐藏选中的图标
     */
    public void hideSelectedStoryIcon() {
        if (mSelectedMarker != null) {
            mSelectedMarker.remove();
            mSelectedMarker = null;
        }
    }

    /**
     * 显示所有图标
     */
    public void showStoriesIcons(ArrayList<StoryInfo> storyList) {
//        Log.d(TAG, "showStoriesIcons: 绘制图标！！！");
        for (StoryInfo storyInfo : storyList) {
            showStoryIcon(storyInfo);
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