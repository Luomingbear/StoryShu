package com.storyshu.storyshu.mvp.create.article;

import android.content.Context;
import android.view.View;

import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.storyshu.storyshu.R;
import com.storyshu.storyshu.bean.issueStory.IssueLongStoryBean;
import com.storyshu.storyshu.info.CardInfo;
import com.storyshu.storyshu.model.location.ILocationQueryTool;
import com.storyshu.storyshu.model.stories.StoryModel;
import com.storyshu.storyshu.mvp.base.IBasePresenter;
import com.storyshu.storyshu.utils.sharepreference.ISharePreference;
import com.storyshu.storyshu.utils.time.TimeUtils;
import com.storyshu.storyshu.widget.text.RichTextEditor;

import java.util.List;

/**
 * mvp
 * 创建文章的逻辑实现
 * Created by bear on 2017/6/7.
 */

public class CreateArticlePresenterIml extends IBasePresenter<CreateArticleView> implements CreateArticlePresenter {
    private static final String TAG = "CreateLongStoryPresente";
    private IssueLongStoryBean longStoryBean;

    public CreateArticlePresenterIml(Context mContext, CreateArticleView mvpView) {
        super(mContext, mvpView);
        longStoryBean = new IssueLongStoryBean();
    }


    @Override
    public void initTitleEdit() {
        mMvpView.getTitleEdit().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (v.equals(mMvpView.getTitleEdit())) {
                    mMvpView.getBottomLayout().setVisibility(View.GONE);
                }

            }
        });
    }

    @Override
    public void initRichTextEditor() {
        //获取到焦点就显示工具栏
        mMvpView.getRichTextEditor().setOnTextFocusListener(new RichTextEditor.OnTextFocusListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mMvpView.getBottomLayout().setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void queryLocation() {
        ILocationQueryTool locationQueryTool = new ILocationQueryTool(mContext);
        locationQueryTool.setOnLocationQueryListener(new ILocationQueryTool.OnLocationQueryListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeAddress regeocodeAddress) {
                if (regeocodeAddress.getPois().size() > 0) {
                    longStoryBean.setLocationTitle(regeocodeAddress.getPois().get(0).getTitle());
                    /**
                     * 上传故事
                     */
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            startIssue();
                        }
                    });

                    thread.start();

                    mMvpView.backActivity();
                }
            }

            @Override
            public void onGeocodeSearched(List<GeocodeAddress> geocodeAddressList) {

            }
        });
        locationQueryTool.startRegeocodeQuery(ISharePreference.getLatLngData(mContext), 100);
    }

    @Override
    public void issueLongStory() {
        longStoryBean.setUserId(ISharePreference.getUserId(mContext));
        longStoryBean.setTitle(mMvpView.getTitleEdit().getText().toString());
        longStoryBean.setContent(mMvpView.getRichTextEditor().getEditData());
        longStoryBean.setCreateTime(TimeUtils.getCurrentTime());
        //默认保存7天
        longStoryBean.setDestroyTime(TimeUtils.getDestroyTime(7 * 24 * 60));

        longStoryBean.setLatLng(ISharePreference.getLatLngData(mContext));
        longStoryBean.setCityName(ISharePreference.getCityName(mContext));
        longStoryBean.setStoryType(CardInfo.ARTICLE);
        queryLocation();
    }

    /**
     * 开始发布
     */
    private void startIssue() {
        StoryModel storyModel = new StoryModel(mContext);
        storyModel.issueLongStory(longStoryBean);
        storyModel.setOnStoryIssuseListener(new StoryModel.OnStoryIssuseListener() {
            @Override
            public void onSucceed() {
                mMvpView.showToast(R.string.issue_succeed);
            }

            @Override
            public void onFailed(String error) {
                mMvpView.showToast(error);
            }
        });
    }
}
