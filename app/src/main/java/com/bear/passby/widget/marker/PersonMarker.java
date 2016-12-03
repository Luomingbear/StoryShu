package com.bear.passby.widget.marker;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.bear.passby.utils.ViewBitmapTool;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * 个人的标志
 * Created by bear on 2016/12/2.
 */

public class PersonMarker extends IMarker {
    private PersonView mPersonView; //

    public PersonMarker(Context mContext, AMap mAMap, LatLng mLatLng) {
        super(mContext, mAMap, mLatLng);
        init();
    }

    private ImageLoadingListener imageLoadingListener = new ImageLoadingListener() {
        @Override
        public void onLoadingStarted(String imageUri, View view) {

        }

        @Override
        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

        }

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            mPersonView.init(loadedImage);
            Bitmap bitmap = ViewBitmapTool.convertViewToBitmap(mPersonView);

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(mLatLng);
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
//        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
//                .decodeResource(mContext.getResources(),
//                        R.drawable.person_location)));

            //add

            mAMap.addMarker(markerOptions);
        }

        @Override
        public void onLoadingCancelled(String imageUri, View view) {

        }
    };

    private void init() {
        mPersonView = new PersonView(mContext);
        String url = "http://img3.imgtn.bdimg.com/it/u=1059202420,708287351&fm=23&gp=0.jpg";
        ImageLoader.getInstance().loadImage(url, imageLoadingListener);

    }

    private void remove() {
    }
}
