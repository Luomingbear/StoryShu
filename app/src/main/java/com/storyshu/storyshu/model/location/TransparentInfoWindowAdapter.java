package com.storyshu.storyshu.model.location;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.Marker;

/**
 * 头像的infoWindow
 * Created by bear on 2017/3/30.
 */

public class TransparentInfoWindowAdapter implements AMap.InfoWindowAdapter {
    private Context context;

    public TransparentInfoWindowAdapter(Context context) {
        this.context = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return renderView();
    }

    @Override
    public View getInfoContents(Marker marker) {

        return null;
    }

    private View renderView() {
        View view = new View(context);
        view.setBackgroundColor(Color.TRANSPARENT);
        return view;
    }
}
