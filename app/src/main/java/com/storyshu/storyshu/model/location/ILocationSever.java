package com.storyshu.storyshu.model.location;

import android.content.Context;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.storyshu.storyshu.R;
import com.storyshu.storyshu.tool.observable.EventObservable;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 具体实现定位服务的类
 * Created by bear on 2016/11/23.
 */

public class ILocationSever {
    private static final String TAG = "ILocationSever";
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    private int mIntervalTime = 2000; //定位间隔时间毫秒
    private Timer mTimer; //用于重新定位服务

    public ILocationSever(Context applicationContext) {
        //初始化定位
        mLocationClient = new AMapLocationClient(applicationContext);
    }

    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    EventObservable.getInstance().notifyObservers(R.id.title_view, aMapLocation.getAoiName());

                    //可在其中解析amapLocation获取相应内容。
                    if (onLocationChange != null)
                        onLocationChange.onLocationChange(aMapLocation);
                    Log.i(TAG, "onLocationChanged: " + aMapLocation);
                    mTimer.cancel();
                    mTimer = null;

                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo());

                    if (mTimer == null) {
                        mTimer = new Timer();
                        mTimer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                start();
                            }
                        }, 1000, 1000);
                    }
                }
            }
        }
    };

    /**
     * 初始化
     * 设置定位间隔时间精确度等
     */
    private void init() {
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();

        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);

        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);

        //设置定位模式为AMapLocationMode.Battery_Saving，低功耗模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);

        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
        mLocationOption.setInterval(mIntervalTime);

        /**
         * 获取最近3s内的最高精度定位
         */
//        //获取一次定位结果：
//        //该方法默认为false。
//        mLocationOption.setOnceLocation(false);
//
//        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
//        mLocationOption.setInterval(1000);
//        mLocationOption.setGpsFirst(true);
//        mLocationOption.setOnceLocationLatest(true);

    }


    /**
     * 开始定位
     */
    public void start() {
        init();
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
//        mLocationClient.startAssistantLocation();
    }

    /**
     * 停止定位
     */
    public void stop() {
        mLocationClient.stopLocation();
    }

    /**
     * 结束定位
     */
    public void destroy() {
        if (mLocationClient == null)
            return;
        mLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
        mLocationClient.onDestroy();
        mLocationClient.stopLocation();
    }


    private OnLocationChangeListener onLocationChange;

    public void setOnLocationChange(OnLocationChangeListener onLocationChange) {
        this.onLocationChange = onLocationChange;
    }

    public interface OnLocationChangeListener {
        /**
         * 当位置改变和第一次获取到位置的时候返回
         *
         * @param aMapLocation 位置数据
         */
        void onLocationChange(AMapLocation aMapLocation);
    }
}
