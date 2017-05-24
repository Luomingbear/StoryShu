package com.storyshu.storyshu.model.stories;

import android.content.Context;

import com.storyshu.storyshu.bean.RecommendPostBean;
import com.storyshu.storyshu.bean.getStory.NearStoriesRsponseBean;
import com.storyshu.storyshu.info.AirPortPushInfo;
import com.storyshu.storyshu.info.CardInfo;
import com.storyshu.storyshu.utils.net.CodeUtil;
import com.storyshu.storyshu.utils.net.RetrofitManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 获取推送的故事集的数据操作类
 * 根据用户的id和为位置推送
 * Created by bear on 2017/3/20.
 */

public class PushStoryModel {
    private ArrayList<AirPortPushInfo> mPushList; //推送的列表
    private Context mAppContext; //上下文
    private OnPushStoryModelListener onPushStoryModelListener;

    /**
     * 设置推送列表获取监听函数
     *
     * @param onPushStoryModelListener
     */
    public void setOnPushStoryModelListener(OnPushStoryModelListener onPushStoryModelListener) {
        this.onPushStoryModelListener = onPushStoryModelListener;
    }

    /**
     * 获取故事集的接口
     */
    public interface OnPushStoryModelListener {
        /**
         * 当故事集获取成功之后返回
         *
         * @param pushList AirPortPushInfo的列表
         */
        void onDataGotSucceed(ArrayList<AirPortPushInfo> pushList);

        /**
         * 数据获取失败
         */
        void onDataGotFailed(String error);
    }

    public PushStoryModel(Context context) {
        mAppContext = context.getApplicationContext();
    }

    /**
     * 根据用户的id和位置进行推荐
     *
     * @param recommendPostBean
     */
    public void startGetPushList(RecommendPostBean recommendPostBean) {

        Call<NearStoriesRsponseBean> call = RetrofitManager.getInstance().getService().getRecommendStory(recommendPostBean);
        call.enqueue(new Callback<NearStoriesRsponseBean>() {
            @Override
            public void onResponse(Call<NearStoriesRsponseBean> call, Response<NearStoriesRsponseBean> response) {
                if (response.body().getCode() == CodeUtil.Succeed) {
                    mPushList = new ArrayList<>();
                    if (response.body().getData() != null && response.body().getData().size() > 0) {
                        for (CardInfo storyBean : response.body().getData())
                            mPushList.add(new AirPortPushInfo(storyBean, AirPortPushInfo.TYPE_STORY, ""));
                    }

                    if (onPushStoryModelListener != null)
                        onPushStoryModelListener.onDataGotSucceed(mPushList);
                } else {
                    if (onPushStoryModelListener != null)
                        onPushStoryModelListener.onDataGotFailed(response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<NearStoriesRsponseBean> call, Throwable t) {
                if (onPushStoryModelListener != null)
                    onPushStoryModelListener.onDataGotFailed(t.getMessage());
            }
        });
    }
}
