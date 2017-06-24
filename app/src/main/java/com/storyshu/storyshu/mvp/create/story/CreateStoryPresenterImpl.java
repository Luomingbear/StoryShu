package com.storyshu.storyshu.mvp.create.story;

import android.content.Context;
import android.util.Log;

import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.storyshu.storyshu.R;
import com.storyshu.storyshu.bean.issueStory.IssueStoryBean;
import com.storyshu.storyshu.info.CardInfo;
import com.storyshu.storyshu.model.location.ILocationQueryTool;
import com.storyshu.storyshu.model.stories.StoryModel;
import com.storyshu.storyshu.mvp.base.IBasePresenter;
import com.storyshu.storyshu.utils.KeyBordUtil;
import com.storyshu.storyshu.utils.sharepreference.ISharePreference;
import com.storyshu.storyshu.utils.time.TimeUtils;
import com.storyshu.storyshu.widget.dialog.LifeTimeDialog;
import com.storyshu.storyshu.widget.dialog.LocationDialog;
import com.storyshu.storyshu.widget.dialog.PicturePreviewDialog;

import java.util.List;

/**
 * 写故事的代理实现
 * Created by bear on 2017/3/17.
 */

public class CreateStoryPresenterImpl extends IBasePresenter<CreateStoryView> implements CreateStoryPresenter {
    private static final String TAG = "CreateStoryPresenterImp";
    private String mTempContent; //故事的临时内容
    private int minStoryContent = 1; //故事内容最少的字符
    private int mLifeTimeMinute = 7 * 24 * 60; //故事保留时间,分钟
    private List<PoiItem> mLocationList; //位置列表
    private int radius = 50; //单位米
    private IssueStoryBean issueInfo = new IssueStoryBean();

    public CreateStoryPresenterImpl(Context mContext, CreateStoryView mvpView) {
        super(mContext, mvpView);
    }

    private StoryModel.OnStoryIssuseListener onStoryIssuseListener = new StoryModel.OnStoryIssuseListener() {
        @Override
        public void onSucceed() {
//            mMvpView.showToast(R.string.issue_succeed);
            //返回
//            mMvpView.backActivity();
        }

        @Override
        public void onFailed(String error) {
            mMvpView.showToast(error);
        }
    };

    @Override
    public void issueStory() {
        mTempContent = mMvpView.getStoryContent().replace(" ", "");
        if (mTempContent.length() < minStoryContent) {
            mMvpView.showToast(R.string.story_length_too_sort);
        } else {

            try {

                /**
                 * 设置发布的内容
                 */
                issueInfo.setUserId(ISharePreference.getUserId(mContext));
                issueInfo.setContent(mMvpView.getStoryContent());
                issueInfo.setStoryPictures(mMvpView.getStoryPic());
                issueInfo.setCityName(ISharePreference.getCityName(mContext));
                issueInfo.setLocationTitle(mMvpView.getLocationTv().getText().toString());

                LatLng latLng = ISharePreference.getLatLngData(mContext);
                issueInfo.setLatitude(latLng.latitude);
                issueInfo.setLongitude(latLng.longitude);

                issueInfo.setCreateTime(TimeUtils.getCurrentTime());
                issueInfo.setDestroyTime(TimeUtils.getDestroyTime(mLifeTimeMinute));
                issueInfo.setIsAnonymous(mMvpView.isAnonymous());
                issueInfo.setStoryType(CardInfo.STORY);

                /**
                 * 上传故事
                 */
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        StoryModel storyModel = new StoryModel(mContext);
                        storyModel.issueStory(issueInfo);
//                        storyModel.setOnStoryIssuseListener(onStoryIssuseListener);
                    }
                });

                thread.start();

                mMvpView.backActivity();

            } catch (Exception e) {
                e.printStackTrace();
            }

            //隐藏键盘
            KeyBordUtil.hideKeyboard(mContext, mMvpView.getLocationTv());
        }
    }

    @Override
    public void getLocationPoi() {
        ILocationQueryTool locationQueryTool = new ILocationQueryTool(mContext);
        locationQueryTool.setOnLocationQueryListener(new ILocationQueryTool.OnLocationQueryListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeAddress regeocodeAddress) {
                mLocationList = regeocodeAddress.getPois();
                mMvpView.getLocationTv().setText(mLocationList.get(0).getTitle());
                Log.i(TAG, "onRegeocodeSearched: 获取到了位置数据了！！！" + mLocationList.size());
            }

            @Override
            public void onGeocodeSearched(List<GeocodeAddress> geocodeAddressList) {
            }
        });
        locationQueryTool.startRegeocodeQuery(ISharePreference.getLatLngData(mContext), radius);
    }

    /**
     * 显示位置弹窗
     */
    @Override
    public void showLocationDialog() {
        LocationDialog locationDialog = new LocationDialog(mContext, R.style.TransparentDialogTheme);
        locationDialog.setDataAndShow(mLocationList, new LocationDialog.OnLocationChooseListener() {
            @Override
            public void onClick(PoiItem poiItem) {
                mMvpView.getLocationTv().setText(poiItem.getTitle());
            }
        });
    }

    @Override
    public void getPicList() {

    }

    @Override
    public void setLifeTime() {
        LifeTimeDialog lifeTimeDialog = new LifeTimeDialog(mContext, R.style.TransparentDialogTheme);
        lifeTimeDialog.setOnLifeSelectedListener(new LifeTimeDialog.OnLifeSelectedListener() {
            @Override
            public void onSelected(int minute) {
                Log.i("CreateStory", "onSelected: 故事：" + minute);
                mLifeTimeMinute = minute;
                mMvpView.getLifeTimeTv().setText(mContext.getString(R.string.life_time) +
                        TimeUtils.hour2lifeTime(mContext, mLifeTimeMinute));
            }
        });
        lifeTimeDialog.show();
    }

    @Override
    public void showPicturePreview() {
        PicturePreviewDialog previewDialog = new PicturePreviewDialog(mContext);
        previewDialog.setStoryListShow(mMvpView.getStoryPic());
    }

}
