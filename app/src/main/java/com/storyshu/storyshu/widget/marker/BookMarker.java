package com.storyshu.storyshu.widget.marker;

import android.content.Context;
import android.graphics.Bitmap;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.storyshu.storyshu.utils.ViewBitmapTool;

/**
 * 故事集图标
 * Created by bear on 2016/12/2.
 */

public class BookMarker extends IMarker {
    public BookMarker(Context mContext, AMap mAMap, LatLng mLatLng) {
        super(mContext, mAMap, mLatLng);

    }

    public void init(String title) {
        BookView bookView = new BookView(mContext);
//        bookView.setTitleString(title);
        Bitmap bitmap = ViewBitmapTool.convertViewToBitmap(bookView);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(mLatLng);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
//        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
//                .decodeResource(mContext.getResources(),
//                        R.drawable.person_location)));

        //add

        mMarker = mAMap.addMarker(markerOptions);
    }

}
