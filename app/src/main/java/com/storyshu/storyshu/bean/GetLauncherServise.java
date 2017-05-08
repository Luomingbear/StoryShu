package com.storyshu.storyshu.bean;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * 获取启动页的数据
 * Created by bear on 2017/5/8.
 */

public interface GetLauncherServise {
    @GET("getLauncher.php")
    Call<LauncherBean> getLauncher();
}
