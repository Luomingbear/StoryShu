package com.storyshu.storyshu.bean;

import com.storyshu.storyshu.info.LoginInfo;
import com.storyshu.storyshu.info.RegisterUserInfo;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * 接口方法
 * Created by bear on 2017/5/8.
 */

public interface ApiService {
    /**
     * 获取启动页的数据
     *
     * @return LauncherResponseBean
     */
    @GET("getLauncher.php")
    Call<LauncherResponseBean> getLauncher();

    /**
     * 注册账号
     *
     * @param userInfo 账户信息
     * @return RegisterResponseBean
     */
    @POST("registerUser.php")
    Call<RegisterResponseBean> register(@Body RegisterUserInfo userInfo);


    /**
     * 获取上传图片需要的token
     *
     * @return token
     */
    @GET("getToken.php")
    Call<TokenResponseBean> getToken();

    /**
     * 登录账号
     *
     * @param loginInfo
     * @return
     */
    @POST("loginUser.php")
    Call<UserLoginResponseBean> login(@Body LoginInfo loginInfo);

    /**
     * 获取用户的基本信息
     *
     * @param userIdBean
     * @return
     */
    @POST("getUserInfo.php")
    Call<UserLoginResponseBean> getUserInfo(@Body UserIdBean userIdBean);

    /**
     * 新建故事
     *
     * @param issueStoryBean
     * @return
     */
    @POST("issueStory.php")
    Call<IssuseResponseBean> issueStory(@Body IssueStoryBean issueStoryBean);
}
