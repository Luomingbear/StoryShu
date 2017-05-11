package com.storyshu.storyshu.utils.net;

import android.text.TextUtils;
import android.util.Log;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.storyshu.storyshu.bean.TokenResponseBean;
import com.storyshu.storyshu.utils.FileUtil;
import com.storyshu.storyshu.utils.PasswordUtil;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 上传文件到七牛云
 * Created by bear on 2017/5/10.
 */

public class QiniuUploadManager {
    private static final String TAG = "QiniuUploadManager";
    private UploadManager mUploadManager; //上传的管理
    private QiniuUploadInterface mQiniuUploadInterface; //上传完毕的监听
    private String mToken; //上传图片使用的token
    private String mFilePath; //上传的地址

    public interface QiniuUploadInterface {
        void onSucceed(String fileNetPath);

        void onFailed();
    }

    public void setQiniuUploadInterface(QiniuUploadInterface mQiniuUploadInterface) {
        this.mQiniuUploadInterface = mQiniuUploadInterface;
    }

    public QiniuUploadManager() {
        Configuration mConfiguration = new Configuration.Builder().build();

        mUploadManager = new UploadManager(mConfiguration);
    }

    /**
     * 上传文件到七牛云
     *
     * @param filePath 文件的绝对路径
     */
    public void uploadFile(String filePath) {
        if (TextUtils.isEmpty(filePath))
            return;
        mFilePath = filePath;
        if (TextUtils.isEmpty(mToken)) {
            getTokenOnNet();
        } else {
            uploadFileOnNet();
        }

    }

    /**
     * 请求token
     */
    public void getTokenOnNet() {
        Call<TokenResponseBean> tokenCall = RetrofitManager.getInstance().getService().getToken();
        tokenCall.enqueue(new Callback<TokenResponseBean>() {
            @Override
            public void onResponse(Call<TokenResponseBean> call, Response<TokenResponseBean> response) {
                mToken = response.body().getData();
                uploadFileOnNet();
            }

            @Override
            public void onFailure(Call<TokenResponseBean> call, Throwable t) {

            }
        });
    }

    /**
     * 开始上传图片
     */
    private void uploadFileOnNet() {
        String key = PasswordUtil.getSampleEncodePassword(System.currentTimeMillis() + "." + FileUtil.getFileName(mFilePath))
                + FileUtil.getExtensionName(mFilePath);
        mUploadManager.put(mFilePath, key, mToken,
                new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject res) {
                        //res包含hash、key等信息，具体字段取决于上传策略的设置
                        if (info.isOK()) {
                            Log.i("qiniu", "Upload Succeed");
                            if (mQiniuUploadInterface != null) {
                                mQiniuUploadInterface.onSucceed(UrlUtil.BASE_QINIU_URL + key);
                            }

                        } else {
                            Log.i("qiniu", "Upload Fail");
                            //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
                            if (mQiniuUploadInterface != null) {
                                mQiniuUploadInterface.onFailed();
                            }
                        }
                    }
                }, null);
    }
}
