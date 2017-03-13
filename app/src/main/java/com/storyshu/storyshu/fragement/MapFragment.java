package com.storyshu.storyshu.fragement;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amap.api.maps.MapView;
import com.amap.api.maps.model.Marker;
import com.storyshu.storyshu.R;
import com.storyshu.storyshu.model.location.ILocationManager;
import com.storyshu.storyshu.utils.ToastUtil;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 故事地图的fragment
 * Created by bear on 2017/3/11.
 */

public class MapFragment extends Fragment implements ILocationManager.OnLocationMarkerClickListener {
    private static final String TAG = "MapFragment";
    private View mViewRoot; //总布局
    private MapView mMapView; //地图

    protected static final int LOCATION_PERMISSION = 0; //定位权限
    protected static final int PERMISSION_INTENT = 0; //跳转到权限管理
    private AlertDialog.Builder dialogBuilder; //显示设置权限的窗
    private String mPermission;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewRoot = inflater.inflate(R.layout.activity_story_map_layout, container, false);
        initView(savedInstanceState);

        return mViewRoot;
    }

    private void initView(Bundle savedInstanceState) {
        if (mViewRoot == null)
            return;

        //地图
        mMapView = (MapView) mViewRoot.findViewById(R.id.story_map_map_view);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，实现地图生命周期管理
        mMapView.onCreate(savedInstanceState);

        //初始化地图管家
        ILocationManager.getInstance().init(getContext(), mMapView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    /**
     * 获取位置信息
     */
    public void getLocation() {
        //显示地图上的图标，延时执行，避免卡顿
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                ILocationManager.getInstance().setOnLocationMarkerClickListener(MapFragment.this).start();
            }
        }, 400);
    }


    /**
     * 移动地图到当前的位置
     */
    private void move2Position() {
        ILocationManager.getInstance().move2CurrentPosition();
    }

    @Override
    public void OnMarkerClick(Marker marker) {
        ToastUtil.Show(getContext(), R.string.skip);
    }
}
