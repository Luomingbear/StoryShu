package com.storyshu.storyshu.utils.net;

import com.alibaba.fastjson.support.retrofit.Retrofit2ConverterFactory;
import com.storyshu.storyshu.bean.ApiService;

import retrofit2.Retrofit;

/**
 * 网络请求的manager
 * 单例模式
 * Created by bear on 2017/5/10.
 */

public class RetrofitManager {
    private static RetrofitManager instance;
    private Retrofit mRetrofit;
    private ApiService mApiService; //请求的接口

    public static RetrofitManager getInstance() {
        if (instance == null) {
            synchronized (RetrofitManager.class) {
                if (instance == null)
                    instance = new RetrofitManager();
            }
        }
        return instance;
    }

    protected RetrofitManager() {
        /**
         * 构建retrofit
         */
        mRetrofit = new Retrofit.Builder()
                .baseUrl(UrlUtil.BASE_API_URL)
                .addConverterFactory(new Retrofit2ConverterFactory())
                .build();

        /**
         * 设置请求
         */
        mApiService = mRetrofit.create(ApiService.class);
    }

    /**
     * 获取服务
     *
     * @return
     */
    public ApiService getService() {
        return mApiService;
    }

}
