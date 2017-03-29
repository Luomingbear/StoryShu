package com.storyshu.storyshu.mvp.create;

import android.content.Context;
import android.util.Log;

import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.storyshu.storyshu.R;
import com.storyshu.storyshu.data.DateBaseHelperIml;
import com.storyshu.storyshu.info.StoryInfo;
import com.storyshu.storyshu.model.location.ILocationQueryTool;
import com.storyshu.storyshu.utils.sharepreference.ISharePreference;
import com.storyshu.storyshu.utils.time.TimeUtils;
import com.storyshu.storyshu.widget.dialog.LifeTimeDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * 写故事的代理实现
 * Created by bear on 2017/3/17.
 */

public class CreateStoryPresenterImpl implements CreateStoryPresenter {
    private static final String TAG = "CreateStoryPresenterImp";
    private CreateStoryView mCreateStoryView; //视图接口
    private Context mContext;
    private String mTempContent; //故事的临时内容
    private int minStoryContent = 1; //故事内容最少的字符
    private int mLifeTimeMinute = 24 * 60; //故事保留时间,分钟
    private List<PoiItem> mLocationList; //位置列表

    public CreateStoryPresenterImpl(CreateStoryView mCreateStoryView, Context mContext) {
        this.mCreateStoryView = mCreateStoryView;
        this.mContext = mContext;
        mLocationList = new ArrayList<>();
    }

    @Override
    public void issueStory() {
        mTempContent = mCreateStoryView.getStoryContent().replace(" ", "");
        if (mTempContent.length() < minStoryContent) {
            mCreateStoryView.showToast(mContext.getString(R.string.story_length_too_sort, minStoryContent));
        } else {
            // TODO: 2017/3/29 上传发布故事
            StoryInfo storyInfo = new StoryInfo();
            storyInfo.setUserInfo(ISharePreference.getUserData(mContext));
            storyInfo.setContent(mCreateStoryView.getStoryContent());
            storyInfo.setCover(mCreateStoryView.getStoryPic().get(0));
            storyInfo.setStoryPic(mCreateStoryView.getStoryPic());
            storyInfo.setLocation(mCreateStoryView.getLocationTv().getText().toString());
            storyInfo.setLatLng(ISharePreference.getLatLngData(mContext));
            storyInfo.setCreateDate(TimeUtils.getCurrentTime());
            storyInfo.setLifeTime(mLifeTimeMinute);
            storyInfo.setAnonymous(false);

            DateBaseHelperIml dateBaseHelperIml = new DateBaseHelperIml(mContext);
            storyInfo.setStoryId(dateBaseHelperIml.getLocalStory().size());
            dateBaseHelperIml.insertStoryData(storyInfo);

            mCreateStoryView.toMainActivity();
        }
    }

    @Override
    public void getLocationPoi() {
        ILocationQueryTool locationQueryTool = new ILocationQueryTool(mContext);
        locationQueryTool.setOnLocationQueryListener(new ILocationQueryTool.OnLocationQueryListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeAddress regeocodeAddress) {
                mLocationList = regeocodeAddress.getPois();
                mCreateStoryView.getLocationTv().setText(mLocationList.get(0).getTitle());
            }

            @Override
            public void onGeocodeSearched(List<GeocodeAddress> geocodeAddressList) {
            }
        });
        locationQueryTool.startRegeocodeQuery(ISharePreference.getLatLngData(mContext), 10);
    }

    @Override
    public void getPicList() {

    }

    @Override
    public void setLifeTime() {
        LifeTimeDialog lifeTimeDialog = new LifeTimeDialog(mContext, R.style.TransparentDialogTheme);
        lifeTimeDialog.setOnLifeSelectedListener(new LifeTimeDialog.OnLifeSelectedListener() {
            @Override
            public void onSelected(int hours) {
                Log.i("CreateStory", "onSelected: 故事：" + hours);
                mLifeTimeMinute = hours;
                mCreateStoryView.getLifeTimeTv().setText(mContext.getString(R.string.life_time) +
                        TimeUtils.hour2lifeTime(mContext, mLifeTimeMinute));
            }
        });
        lifeTimeDialog.show();
    }

}
