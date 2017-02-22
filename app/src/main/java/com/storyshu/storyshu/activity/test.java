package com.storyshu.storyshu.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.storyshu.storyshu.R;
import com.storyshu.storyshu.model.location.ILocationQueryTool;
import com.storyshu.storyshu.utils.sharepreference.ISharePreference;
import com.storyshu.storyshu.widget.LocationSelector;

import java.util.ArrayList;
import java.util.List;

/**
 * Test
 * Created by bear on 2016/12/6.
 */

public class Test extends FragmentActivity implements View.OnClickListener {
    private static final String TAG = "Test";
    private LocationSelector locationSelector;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {

        locationSelector = (LocationSelector) findViewById(R.id.select_location);

        final ILocationQueryTool locationQueryTool = new ILocationQueryTool();
        locationQueryTool.startRegeocodeQuery(this, ISharePreference.getLatLngData(this), 20);
        locationQueryTool.setOnLocationQueryListener(new ILocationQueryTool.OnLocationQueryListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeAddress regeocodeAddress) {
                List<PoiItem> list = new ArrayList<PoiItem>();
                list = regeocodeAddress.getPois();
                locationSelector.setLocationList(list);
                Log.i(TAG, "onRegeocodeSearched: list size:" + list.size());
                Log.i(TAG, "onRegeocodeSearched: list:" + list);
            }

            @Override
            public void onGeocodeSearched(List<GeocodeAddress> geocodeAddressList) {

            }
        });

//        findViewById(R.id.add_image).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                richTextEditor.insertImage("/storage/emulated/0/tencent/MicroMsg/WeiXin/mmexport1482231789918.jpg");
//            }
//        });
    }

    private void removeDuplicate(List<PoiItem> list) {
        if (list == null)
            return;
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = list.size() - 1; j > i; j--) {
                if (TextUtils.isEmpty(list.get(j).getTitle())) {
                    list.remove(j);
                    continue;
                }

                if (list.get(j).getTitle().equals(list.get(i).getTitle())) {
                    list.remove(j);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
    }
}
