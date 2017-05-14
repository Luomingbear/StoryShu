package com.storyshu.storyshu.utils.net;


import android.database.Observable;

import com.storyshu.storyshu.bean.LauncherResponseBean;
import com.storyshu.storyshu.bean.TokenResponseBean;
import com.storyshu.storyshu.bean.checkForUpdate.VersionResponseBean;
import com.storyshu.storyshu.bean.getStory.LocationBean;
import com.storyshu.storyshu.bean.getStory.NearStoriesRsponseBean;
import com.storyshu.storyshu.bean.getStory.StoryIdBean;
import com.storyshu.storyshu.bean.getStory.StoryReponseBean;
import com.storyshu.storyshu.bean.issueStory.IssueStoryBean;
import com.storyshu.storyshu.bean.issueStory.IssuseResponseBean;
import com.storyshu.storyshu.bean.user.RegisterResponseBean;
import com.storyshu.storyshu.bean.user.UserIdBean;
import com.storyshu.storyshu.bean.user.UserLoginResponseBean;
import com.storyshu.storyshu.info.LoginInfo;
import com.storyshu.storyshu.info.RegisterUserInfo;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

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

    /**
     * 获取故事的详细信息
     *
     * @param storyIdBean 包含故事id的请求体
     * @return
     */
    @POST("getStoryInfo.php")
    Call<StoryReponseBean> getStoryInfo(@Body StoryIdBean storyIdBean);

    /**
     * 获取用户附近未过期的故事
     *
     * @param locationBean
     * @return
     */
    @POST("getNearStory.php")
    Call<NearStoriesRsponseBean> getNearStory(@Body LocationBean locationBean);

    /**
     * 获取更新信息
     *
     * @return
     */
    @GET("checkForUpdate.php")
    Call<VersionResponseBean> checkForUpdate();

    /**
     * 下载文件
     *
     * @param url 网络地址
     * @return
     */
    @Streaming
    @GET
    Call<ResponseBody> downloadFile(@Url String url);

    /**
     * 断点下载
     *
     * @param range
     * @param url
     * @return
     */
    @GET
    @Streaming
    Observable<Response<ResponseBody>> resumeDownload(@Header("Range") String range, @Url String url);
}
