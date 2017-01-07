package com.storyshu.storyshu.widget.marker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.storyshu.storyshu.R;
import com.storyshu.storyshu.utils.ViewBitmapTool;

/**
 * 故事集图标
 * Created by bear on 2016/12/2.
 */

public class BookMarker extends IMarker {
    private static final String TAG = "BookMarker";
    private BookView mBookView;

    public BookMarker(Context mContext, AMap mAMap, LatLng mLatLng) {
        super(mContext, mAMap, mLatLng);

    }

    private ImageLoadingListener imageLoadingListener = new ImageLoadingListener() {
        @Override
        public void onLoadingStarted(String imageUri, View view) {
        }

        @Override
        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
            if (failReason.getType() == FailReason.FailType.UNKNOWN) {
                mBookView.init(imageUri);
                Bitmap bitmap = ViewBitmapTool.convertViewToBitmap(mBookView);

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(mLatLng);
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap));

                mMarker = mAMap.addMarker(markerOptions);
            }
            Log.e(TAG, "onLoadingFailed: 地图上个人头像的图标加载失败");
        }

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            mBookView.init(loadedImage);
            Bitmap bitmap = ViewBitmapTool.convertViewToBitmap(mBookView);

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(mLatLng);
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap));

            mMarker = mAMap.addMarker(markerOptions);
            Log.i(TAG, "onLoadingComplete: ");
        }

        @Override
        public void onLoadingCancelled(String imageUri, View view) {
            Log.i(TAG, "onLoadingCancelled: ");
        }
    };

    public void init(String title, String bgPath) {
        mBookView = new BookView(mContext);
        if (TextUtils.isEmpty(bgPath)) {
            mBookView.init(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.book));
            Bitmap bitmap = ViewBitmapTool.convertViewToBitmap(mBookView);

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(mLatLng);
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap));

            mMarker = mAMap.addMarker(markerOptions);
        } else
            ImageLoader.getInstance().loadImage(bgPath, imageLoadingListener);

    }

}
