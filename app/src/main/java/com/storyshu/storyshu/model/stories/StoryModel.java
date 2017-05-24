package com.storyshu.storyshu.model.stories;

import android.content.Context;
import android.util.Log;

import com.amap.api.maps.model.LatLng;
import com.storyshu.storyshu.R;
import com.storyshu.storyshu.bean.OnlyDataResponseBean;
import com.storyshu.storyshu.bean.getStory.LocationBean;
import com.storyshu.storyshu.bean.getStory.NearStoriesRsponseBean;
import com.storyshu.storyshu.bean.getStory.StoryBean;
import com.storyshu.storyshu.bean.getStory.StoryIdBean;
import com.storyshu.storyshu.bean.getStory.StoryReponseBean;
import com.storyshu.storyshu.bean.getStory.UserStoryPostBean;
import com.storyshu.storyshu.bean.getStory.UserStoryResponseBean;
import com.storyshu.storyshu.bean.issueStory.IssueStoryBean;
import com.storyshu.storyshu.info.CardInfo;
import com.storyshu.storyshu.tool.observable.EventObservable;
import com.storyshu.storyshu.utils.ToastUtil;
import com.storyshu.storyshu.utils.net.CodeUtil;
import com.storyshu.storyshu.utils.net.QiniuUploadManager;
import com.storyshu.storyshu.utils.net.RetrofitManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * mvp模式
 * 故事数据的获取
 * Created by bear on 2017/3/26.
 */

public class StoryModel {
    private static final String TAG = "StoryModel";
    private Context mContext;
    private OnStoryGetListener onStoryModelListener;
    private OnStoryIssuseListener onStoryIssuseListener;
    private OnCardInfoGotListener onCardInfoGotListener;
    private int mUserId; //用户

    /**
     * 设置获取故事的监听
     *
     * @param onStoryModelListener
     */
    public void setOnStoryModelListener(OnStoryGetListener onStoryModelListener) {
        this.onStoryModelListener = onStoryModelListener;
    }

    public void setOnStoryIssuseListener(OnStoryIssuseListener onStoryIssuseListener) {
        this.onStoryIssuseListener = onStoryIssuseListener;
    }

    public void setOnCardInfoGotListener(OnCardInfoGotListener onCardInfoGotListener) {
        this.onCardInfoGotListener = onCardInfoGotListener;
    }

    /**
     * 获取故事的接口
     */
    public interface OnStoryGetListener {
        void onStoriesGot(List<StoryBean> storyList);

        void onFailed(String error);
    }

    public interface OnCardInfoGotListener {
        void onSucceed(List<CardInfo> cardInfoList);

        void onFailed(String error);
    }

    /**
     * 发布故事的接口
     */
    public interface OnStoryIssuseListener {
        void onSucceed();

        void onFailed(String error);
    }

    public StoryModel(Context mContext) {
        this.mContext = mContext.getApplicationContext();
    }

    /**
     * 获取用户附近未过期的故事
     *
     * @param userId
     * @param latLng
     * @param scale  地图缩放级别
     */
    public void getNearStories(int userId, LatLng latLng, int scale) {
        mUserId = userId;

        LocationBean locationBean = new LocationBean();
        locationBean.setLatitude(latLng.latitude);
        locationBean.setLongitude(latLng.longitude);
        locationBean.setUserId(userId);
        locationBean.setScale(scale);

        Call<NearStoriesRsponseBean> nearStoriesCall = RetrofitManager.getInstance().getService().getNearStory(locationBean);
        nearStoriesCall.enqueue(new Callback<NearStoriesRsponseBean>() {
            @Override
            public void onResponse(Call<NearStoriesRsponseBean> call, Response<NearStoriesRsponseBean> response) {
                Log.i(TAG, "onResponse: " + response);

                if (response.body().getCode() == CodeUtil.Succeed) {
                    if (onCardInfoGotListener != null)
                        onCardInfoGotListener.onSucceed(response.body().getData());
                } else {
                    if (onCardInfoGotListener != null)
                        onCardInfoGotListener.onFailed(response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<NearStoriesRsponseBean> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                if (onCardInfoGotListener != null)
                    onCardInfoGotListener.onFailed(t.getMessage());
            }
        });


    }


    private IssueStoryBean storyBean; //发布的故事的内容

    /**
     * 发布故事
     *
     * @param issueStoryBean
     */
    public void issueStory(final IssueStoryBean issueStoryBean) {
        storyBean = issueStoryBean;

        if (issueStoryBean.getStoryPictures().size() > 0) {
            QiniuUploadManager qiniuUploadService = new QiniuUploadManager(mContext);
            qiniuUploadService.uploadFileList(issueStoryBean.getStoryPictures());
            //上传完成的监听完成
            qiniuUploadService.setQiniuUploadInterface(new QiniuUploadManager.QiniuUploadInterface() {
                @Override
                public void onSucceed(List<String> pathList) {
                    storyBean.setStoryPictures(pathList);
                    startIssue(storyBean);
                }

                @Override
                public void onFailed(List<String> errorPathList) {
                    Log.e(TAG, "onFailed: " + errorPathList);
                }
            });

            //上传进度的监听
            qiniuUploadService.setQiniuUploadProgressListener(new QiniuUploadManager.QiniuUploadProgressListener() {
                @Override
                public void onProgress(int progress) {
                    Log.i(TAG, "onProgress: 上传进度：" + progress);
                    EventObservable.getInstance().notifyObservers(R.id.line_progress_bar, progress);
                }
            });
        } else {
            startIssue(issueStoryBean);
        }

    }

    /**
     * 开始上传
     *
     * @param storyBean
     */
    private void startIssue(IssueStoryBean storyBean) {
        Call<OnlyDataResponseBean> call = RetrofitManager.getInstance().getService().issueStory(storyBean);
        call.enqueue(new Callback<OnlyDataResponseBean>() {
            @Override
            public void onResponse(Call<OnlyDataResponseBean> call, Response<OnlyDataResponseBean> response) {
                Log.i(TAG, "onResponse: " + response.body().getMessage()
                        + "\n内容：" + response.body().getData());
                if (response.body().getCode() == CodeUtil.Succeed) {
                    ToastUtil.Show(mContext, response.body().getMessage());
                    EventObservable.getInstance().notifyObservers(R.id.line_progress_bar, 100);
                    if (onStoryIssuseListener != null)
                        onStoryIssuseListener.onSucceed();
                } else {
                    if (onStoryIssuseListener != null)
                        onStoryIssuseListener.onFailed(response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<OnlyDataResponseBean> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t);
                if (onStoryIssuseListener != null)
                    onStoryIssuseListener.onFailed(mContext.getString(R.string.issue_failed));
            }
        });
    }

    /**
     * 获取用户的详细信息
     * 不包括评论数据
     *
     * @param storyId 故事id
     */
    public void getStoryInfo(String storyId) {

        Call<StoryReponseBean> call = RetrofitManager.getInstance().getService().
                getStoryInfo(new StoryIdBean(storyId));
        call.enqueue(new Callback<StoryReponseBean>() {
            @Override
            public void onResponse(Call<StoryReponseBean> call, Response<StoryReponseBean> response) {
                Log.i(TAG, "onResponse: " + response.body().getMessage());
                if (response.body().getCode() == CodeUtil.Succeed) {
                    ArrayList<StoryBean> list = new ArrayList<>();
                    list.add(response.body().getData());
                    if (onStoryModelListener != null)
                        onStoryModelListener.onStoriesGot(list);
                } else {
                    if (onStoryModelListener != null)
                        onStoryModelListener.onFailed(response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<StoryReponseBean> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                if (onStoryModelListener != null)
                    onStoryModelListener.onFailed(t.getMessage());
            }
        });
    }

    /**
     * 获取用户发布的所有的故事，每次返回10条数据
     *
     * @param userStoryPostBean
     */
    public void getUserStory(UserStoryPostBean userStoryPostBean) {
        Call<UserStoryResponseBean> call = RetrofitManager.getInstance().getService().getUserStory(userStoryPostBean);
        call.enqueue(new Callback<UserStoryResponseBean>() {
            @Override
            public void onResponse(Call<UserStoryResponseBean> call, Response<UserStoryResponseBean> response) {
                Log.i(TAG, "onResponse: " + response.body().getData());
                if (response.body().getCode() == CodeUtil.Succeed) {
                    if (onCardInfoGotListener != null)
                        onCardInfoGotListener.onSucceed(response.body().getData());
                } else {

                    if (onCardInfoGotListener != null)
                        onCardInfoGotListener.onFailed(response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<UserStoryResponseBean> call, Throwable t) {

                if (onCardInfoGotListener != null)
                    onCardInfoGotListener.onFailed(t.getMessage());
            }
        });
    }
}
