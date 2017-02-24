package com.storyshu.storyshu.model.location;

import android.content.Context;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;

import java.util.List;

/**
 * 通过定位的数据获取逆编码信息
 * Created by bear on 2016/12/3.
 */

public class ILocationQueryTool {
    private static final String TAG = "ILocationQueryTool";
    private Context mContext;
    private AMapLocation mAmapLocation; //位置信息类
    private GeocodeSearch mGeocodeSearch; //搜索实例

    protected ILocationQueryTool() {
    }

    public ILocationQueryTool(Context context) {
        this.mContext = context;
    }

    public ILocationQueryTool init(AMapLocation aMapLocation, OnLocationQueryListener onLocationQueryListener) {
        if (mContext == null)
            return this;
        this.mGeocodeSearch = new GeocodeSearch(mContext);
        this.mAmapLocation = aMapLocation;
        this.onLocationQueryListener = onLocationQueryListener;
        return this;
    }

    /**
     * 搜索回调函数
     */
    private GeocodeSearch.OnGeocodeSearchListener onGeocodeSearchListener = new GeocodeSearch.OnGeocodeSearchListener() {
        @Override
        public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
            if (i == 1000) {
                if (regeocodeResult != null && regeocodeResult.getRegeocodeAddress() != null
                        && regeocodeResult.getRegeocodeAddress().getFormatAddress() != null) {
                    RegeocodeAddress r = regeocodeResult.getRegeocodeAddress();
                    Log.d(TAG, "onRegeocodeSearched: 搜索完毕！");
                    if (onLocationQueryListener != null)
                        onLocationQueryListener.onRegeocodeSearched(r);

                } else {
                    Log.i(TAG, "onRegeocodeSearched: regeocodeResult:" + regeocodeResult);
                }
            } else {
                Log.e(TAG, "onGeocodeSearched: " + i);
            }
        }

        @Override
        public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
            if (i == 1000) {
                if (geocodeResult != null && geocodeResult.getGeocodeAddressList() != null
                        && geocodeResult.getGeocodeAddressList().size() > 0) {
                    List<GeocodeAddress> addressList = geocodeResult.getGeocodeAddressList();
                    if (onLocationQueryListener != null)
                        onLocationQueryListener.onGeocodeSearched(addressList);
                } else {
                    Log.i(TAG, "onGeocodeSearched: ");
                }
            } else {
                Log.e(TAG, "onGeocodeSearched: rCode:" + i);
            }
        }
    };

    /**
     * 开始指定坐标的搜索，需要先执行init函数
     *
     * @param radius 搜索的半径 单位米
     */
    public void startRegeocodeQuery(int radius) {
        if (mGeocodeSearch == null || mAmapLocation == null)
            return;
        mGeocodeSearch.setOnGeocodeSearchListener(onGeocodeSearchListener);
        LatLonPoint latLonPoint = new LatLonPoint(mAmapLocation.getLatitude(), mAmapLocation.getLongitude());
        //最小的搜索范围 10米
        if (radius < 10)
            radius = 10;
        RegeocodeQuery geocodeQuery = new RegeocodeQuery(latLonPoint, radius, GeocodeSearch.AMAP);
        mGeocodeSearch.getFromLocationAsyn(geocodeQuery);
    }

    /**
     * 开始指定坐标的搜索
     *
     * @param latLng 搜索的坐标 单位米
     * @param radius 搜索的半径 单位米
     */
    public void startRegeocodeQuery(LatLng latLng, int radius) {
        if (mContext == null)
            return;
        mGeocodeSearch = new GeocodeSearch(mContext);

        LatLonPoint latLonPoint = new LatLonPoint(latLng.latitude, latLng.longitude);
        mGeocodeSearch.setOnGeocodeSearchListener(onGeocodeSearchListener);
        //最小的搜索范围 10米
        if (radius < 10)
            radius = 10;
        RegeocodeQuery geocodeQuery = new RegeocodeQuery(latLonPoint, radius, GeocodeSearch.AMAP);
        mGeocodeSearch.getFromLocationAsyn(geocodeQuery);
    }

    /***
     * 开始模糊搜索
     */
    public void startGeocodeQuery() {
        if (mGeocodeSearch == null || mAmapLocation == null)
            return;

        mGeocodeSearch.setOnGeocodeSearchListener(onGeocodeSearchListener);
        GeocodeQuery geocodeQuery = new GeocodeQuery(mAmapLocation.getLocationDetail(), mAmapLocation.getCityCode());
        mGeocodeSearch.getFromLocationNameAsyn(geocodeQuery);
    }


    private OnLocationQueryListener onLocationQueryListener;

    public void setOnLocationQueryListener(OnLocationQueryListener onLocationQueryListener) {
        this.onLocationQueryListener = onLocationQueryListener;
    }

    public interface OnLocationQueryListener {
        /***
         * 搜索经纬度得到的值
         */
        void onRegeocodeSearched(RegeocodeAddress regeocodeAddress);

        /**
         * 通过粗略位置获取的值
         */
        void onGeocodeSearched(List<GeocodeAddress> geocodeAddressList);
    }
}
