package com.bear.passby.widget.marker;

import android.content.Context;
import android.graphics.Bitmap;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;
import com.bear.passby.utils.ViewBitmapTool;

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
        bookView.setTitleString(title);
        Bitmap bitmap = ViewBitmapTool.convertViewToBitmap(bookView);


        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(mLatLng);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
//        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
//                .decodeResource(mContext.getResources(),
//                        R.drawable.person_location)));

        //add
        mAMap.addMarker(markerOptions);
    }
}
