package com.storyshu.storyshu.tool;

import android.content.Context;
import android.util.Log;

import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTTransmitMessage;
import com.storyshu.storyshu.bean.ClientIdBean;
import com.storyshu.storyshu.bean.OnlyDataResponseBean;
import com.storyshu.storyshu.utils.net.RetrofitManager;
import com.storyshu.storyshu.utils.sharepreference.ISharePreference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 继承 GTIntentService 接收来自个推的消息, 所有消息在线程中回调, 如果注册了该服务, 则务必要在 AndroidManifest中声明, 否则无法接受消息<br>
 * onReceiveMessageData 处理透传消息<br>
 * onReceiveClientId 接收 cid <br>
 * onReceiveOnlineState cid 离线上线通知 <br>
 * onReceiveCommandResult 各种事件处理回执 <br>
 * Created by bear on 2017/7/8.
 */

public class PushIntentService extends GTIntentService {
    public PushIntentService() {
    }

    @Override
    public void onReceiveServicePid(Context context, int pid) {
    }

    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage msg) {
    }

    @Override
    public void onReceiveClientId(Context context, String clientid) {
        Log.e(TAG, "onReceiveClientId -> " + "clientid = " + clientid);
        //上传用户的clientId
        Call<OnlyDataResponseBean> call = RetrofitManager.getInstance().getService().uploadClientId(
                new ClientIdBean(clientid, ISharePreference.getUserId(getApplicationContext())));
        call.enqueue(new Callback<OnlyDataResponseBean>() {
            @Override
            public void onResponse(Call<OnlyDataResponseBean> call, Response<OnlyDataResponseBean> response) {

            }

            @Override
            public void onFailure(Call<OnlyDataResponseBean> call, Throwable t) {

            }
        });

    }

    @Override
    public void onReceiveOnlineState(Context context, boolean online) {
    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage cmdMessage) {
    }

}
