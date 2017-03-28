package com.storyshu.storyshu.mvp.create;

import android.content.Context;
import android.util.Log;

import com.storyshu.storyshu.R;
import com.storyshu.storyshu.utils.time.TimeUtils;
import com.storyshu.storyshu.widget.dialog.LifeTimeDialog;

/**
 * 写故事的代理实现
 * Created by bear on 2017/3/17.
 */

public class CreateStoryPresenterImpl implements CreateStoryPresenter {
    private CreateStoryView mCreateStoryView; //视图接口
    private Context mContext;
    private int mLifeTimeHour = 24; //故事保留时间

    public CreateStoryPresenterImpl(CreateStoryView mCreateStoryView, Context mContext) {
        this.mCreateStoryView = mCreateStoryView;
        this.mContext = mContext;
    }

    @Override
    public void issueStory() {

    }

    @Override
    public void getLocationPoi() {

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
                mLifeTimeHour = hours;
                mCreateStoryView.getLifeTimeTv().setText(mContext.getString(R.string.life_time) +
                        TimeUtils.hour2lifeTime(mContext, mLifeTimeHour));
            }
        });
        lifeTimeDialog.show();
    }

}
