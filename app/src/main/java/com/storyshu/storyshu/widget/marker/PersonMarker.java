package com.storyshu.storyshu.widget.marker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.animation.LinearInterpolator;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.animation.Animation;
import com.amap.api.maps.model.animation.TranslateAnimation;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.storyshu.storyshu.R;
import com.storyshu.storyshu.info.StoryInfo;
import com.storyshu.storyshu.utils.ViewBitmapTool;

/**
 * 个人的标志
 * Created by bear on 2016/12/2.
 */

public class PersonMarker extends IMarker {
    private static final String TAG = "PersonMarker";
    private PersonView mPersonView; //
    private StoryInfo mStoryInfo; //故事的数据

    public PersonMarker(Context mContext, AMap mAMap, StoryInfo storyInfo) {
        super(mContext, mAMap, storyInfo.getLatLng());
        this.mStoryInfo = storyInfo;
    }

    /**
     * 显示普通的图标
     *
     * @param avatarPath
     */
    public void setAvatarAndShow(String avatarPath) {
        mPersonView = new PersonView(mContext);

        if (TextUtils.isEmpty(avatarPath)) {
            mPersonView.init(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.avatar_wolverine));
            Bitmap bitmap = ViewBitmapTool.convertLayoutToBitmap(mPersonView);

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(mLatLng);
            markerOptions.title(mStoryInfo.getUserInfo().getUserId());
            markerOptions.snippet(mStoryInfo.getStoryId());
            markerOptions.infoWindowEnable(false); //不显示标题窗口
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap));

            mMarker = mAMap.addMarker(markerOptions);

        } else {
            SimpleTarget target = new SimpleTarget<GlideBitmapDrawable>() {
                @Override
                public void onResourceReady(GlideBitmapDrawable resource, GlideAnimation<? super GlideBitmapDrawable> glideAnimation) {
                    mPersonView.init(resource);
                    Bitmap bitmap = ViewBitmapTool.convertLayoutToBitmap(mPersonView);

                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(mLatLng);
                    markerOptions.title(mStoryInfo.getUserInfo().getUserId());
                    markerOptions.snippet(mStoryInfo.getStoryId());
                    markerOptions.infoWindowEnable(false);
                    markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap));

                    mMarker = mAMap.addMarker(markerOptions);

                }
            };
            Glide.with(mContext.getApplicationContext())
                    .load(avatarPath)
                    .into(target);
        }
    }

    /**
     * 显示选中的图标
     *
     * @param avatarPath
     */
    public void setAvatarAndShowSelected(String avatarPath) {
        mPersonView = new PersonView(mContext);
        mPersonView.setSelectedMode();

        if (TextUtils.isEmpty(avatarPath)) {
            mPersonView.init(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.avatar_wolverine));
            Bitmap bitmap = ViewBitmapTool.convertLayoutToBitmap(mPersonView);

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(mLatLng);
            markerOptions.title(mStoryInfo.getUserInfo().getUserId());
            markerOptions.snippet(mStoryInfo.getStoryId());
            markerOptions.infoWindowEnable(false);
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap));

            mMarker = mAMap.addMarker(markerOptions);

        } else {
            SimpleTarget target = new SimpleTarget<GlideBitmapDrawable>() {
                @Override
                public void onResourceReady(GlideBitmapDrawable resource, GlideAnimation<? super GlideBitmapDrawable> glideAnimation) {
                    mPersonView.init(resource);
                    Bitmap bitmap = ViewBitmapTool.convertLayoutToBitmap(mPersonView);

                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(mLatLng);
                    markerOptions.title(mStoryInfo.getUserInfo().getUserId());
                    markerOptions.snippet(mStoryInfo.getStoryId());
                    markerOptions.infoWindowEnable(false);
                    markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap));

                    mMarker = mAMap.addMarker(markerOptions);
                }
            };
            Glide.with(mContext.getApplicationContext())
                    .load(avatarPath)
                    .into(target);
        }
    }

    /**
     * 动画地移动marker
     *
     * @param latLng
     */
    public void animate(LatLng latLng) {
        Animation animation = new TranslateAnimation(latLng);
        animation.setInterpolator(new LinearInterpolator());
        animation.setDuration(1500);

        mMarker.setAnimation(animation);
        mMarker.startAnimation();
    }

    /**
     * 移动marker
     *
     * @param latLng
     */
    public void move(LatLng latLng) {
        Animation animation = new TranslateAnimation(latLng);
        animation.setInterpolator(new LinearInterpolator());

        mMarker.setAnimation(animation);
        mMarker.startAnimation();
    }
}
