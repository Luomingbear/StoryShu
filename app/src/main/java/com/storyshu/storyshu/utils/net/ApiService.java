package com.storyshu.storyshu.utils.net;


import com.storyshu.storyshu.bean.LauncherResponseBean;
import com.storyshu.storyshu.bean.TokenResponseBean;
import com.storyshu.storyshu.bean.checkForUpdate.VersionResponseBean;
import com.storyshu.storyshu.bean.comment.CommentPostBean;
import com.storyshu.storyshu.bean.comment.CommentReponseBean;
import com.storyshu.storyshu.bean.comment.CommentSizeResponseBean;
import com.storyshu.storyshu.bean.getStory.LocationBean;
import com.storyshu.storyshu.bean.getStory.NearStoriesRsponseBean;
import com.storyshu.storyshu.bean.getStory.StoryIdBean;
import com.storyshu.storyshu.bean.getStory.StoryReponseBean;
import com.storyshu.storyshu.bean.issueStory.IssueStoryBean;
import com.storyshu.storyshu.bean.OnlyDataResponseBean;
import com.storyshu.storyshu.bean.like.LikePostBean;
import com.storyshu.storyshu.bean.user.RegisterResponseBean;
import com.storyshu.storyshu.bean.user.UserIdBean;
import com.storyshu.storyshu.bean.user.UserLoginResponseBean;
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
    Call<OnlyDataResponseBean> issueStory(@Body IssueStoryBean issueStoryBean);

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
     * 喜欢和不喜欢的接口
     *
     * @param likePostBean
     * @return
     */
    @POST("likeStory.php")
    Call<OnlyDataResponseBean> likeStory(@Body LikePostBean likePostBean);

    /**
     * 发表评论
     *
     * @param commentPostBean
     * @return
     */
    @POST("issueComment.php")
    Call<OnlyDataResponseBean> issueComment(@Body CommentPostBean commentPostBean);

    /**
     * 获取热门评论数据
     *
     * @param storyIdBean
     * @return
     */
    @POST("getHotComment.php")
    Call<CommentReponseBean> getHotComment(@Body StoryIdBean storyIdBean);

    /**
     * 获取最新的评论 返回10条数据
     *
     * @param storyIdBean
     * @return
     */
    @POST("getNestComment.php")
    Call<CommentReponseBean> getNestComment(@Body StoryIdBean storyIdBean);

    /**
     * 获取评论的数量
     *
     * @param storyIdBean
     * @return
     */
    @POST("getCommentSize.php")
    Call<CommentSizeResponseBean> getCommentSize(@Body StoryIdBean storyIdBean);
}
