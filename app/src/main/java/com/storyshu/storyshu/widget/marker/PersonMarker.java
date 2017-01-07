package com.storyshu.storyshu.widget.marker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.animation.Animation;
import com.amap.api.maps.model.animation.TranslateAnimation;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.storyshu.storyshu.R;
import com.storyshu.storyshu.utils.ViewBitmapTool;
import com.storyshu.storyshu.utils.sharepreference.ISharePreference;

/**
 * 个人的标志
 * Created by bear on 2016/12/2.
 */

public class PersonMarker extends IMarker {
    private static final String TAG = "PersonMarker";
    private PersonView mPersonView; //
    public static String PersonId = "personMarker";

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
            if (failReason.getType() == FailReason.FailType.UNKNOWN) {
                mPersonView.init(imageUri);
                Bitmap bitmap = ViewBitmapTool.convertViewToBitmap(mPersonView);

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(mLatLng);
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap));

                mMarker = mAMap.addMarker(markerOptions);
            }
            Log.e(TAG, "onLoadingFailed: 地图上个人头像的图标加载失败");
        }

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            mPersonView.init(loadedImage);
            Bitmap bitmap = ViewBitmapTool.convertViewToBitmap(mPersonView);

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

    private void init() {
        mPersonView = new PersonView(mContext);
        String url = ISharePreference.getUserData(mContext).getAvatar();
        if (TextUtils.isEmpty(url)) {
            mPersonView.init(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.avatar_superman));
            Bitmap bitmap = ViewBitmapTool.convertViewToBitmap(mPersonView);

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(mLatLng);
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap));

            mMarker = mAMap.addMarker(markerOptions);
        } else
            ImageLoader.getInstance().loadImage(url, imageLoadingListener);

    }

    /**
     * 移动marker
     *
     * @param latLng
     */
    public void move(LatLng latLng) {
        Animation animation = new TranslateAnimation(latLng);
        animation.setInterpolator(new LinearInterpolator());
        animation.setDuration(2000);

        mMarker.setAnimation(animation);
        mMarker.startAnimation();
    }
}
