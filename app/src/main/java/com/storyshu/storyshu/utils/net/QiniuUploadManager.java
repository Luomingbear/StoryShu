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

import java.util.ArrayList;
import java.util.List;

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
    private List<String> mPathList; //待上传的文件的地址
    private List<String> mCloudPathList; //图片保存的地址
    private List<String> mErrorPathList; //图片上传失败的


    public interface QiniuUploadInterface {
        void onSucceed(List<String> pathList);

        void onFailed(List<String> errorPathList);
    }

    public void setQiniuUploadInterface(QiniuUploadInterface mQiniuUploadInterface) {
        this.mQiniuUploadInterface = mQiniuUploadInterface;
    }

    public QiniuUploadManager() {
        Configuration mConfiguration = new Configuration.Builder().build();

        mUploadManager = new UploadManager(mConfiguration);
        mPathList = new ArrayList<>();
        mCloudPathList = new ArrayList<>();
    }

    /**
     * 上传文件到七牛云
     *
     * @param filePath 文件的绝对路径
     */
    public void uploadFile(String filePath) {
        if (TextUtils.isEmpty(filePath))
            return;

        mPathList.add(filePath);
        uploadFileList(mPathList);
    }

    /**
     * 上传图片列表
     *
     * @param pathList
     */
    public void uploadFileList(List<String> pathList) {
        if (pathList == null || pathList.size() == 0)
            return;

        mPathList = pathList;

        if (TextUtils.isEmpty(mToken))
            getTokenOnNet();
        else
            uploadFileOnNet(mPathList.get(0));
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
                uploadFileOnNet(mPathList.get(0));
            }

            @Override
            public void onFailure(Call<TokenResponseBean> call, Throwable t) {
                mErrorPathList = mPathList;
                if (mQiniuUploadInterface != null)
                    mQiniuUploadInterface.onFailed(mErrorPathList);
            }
        });
    }

    /**
     * 上传图片
     *
     * @param path
     */
    private void uploadFileOnNet(final String path) {

        String key = PasswordUtil.getSampleEncodePassword(System.currentTimeMillis() + FileUtil.getFileName(path))
                + "." + FileUtil.getExtensionName(path);

        mUploadManager.put(path, key, mToken,
                new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject res) {
                        mPathList.remove(0);

                        //res包含hash、key等信息，具体字段取决于上传策略的设置
                        if (info.isOK()) {
                            Log.i("qiniu", "Upload Succeed");

                            mCloudPathList.add(UrlUtil.BASE_QINIU_URL + key);

                            if (mPathList.size() > 0) {
                                uploadFileOnNet(mPathList.get(0));
                            } else if (mQiniuUploadInterface != null) {
                                mQiniuUploadInterface.onSucceed(mCloudPathList);
                            }

                        } else {
                            Log.i("qiniu", "Upload Fail");
                            mErrorPathList.add(path);

                            //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
                            if (mPathList.size() > 0) {
                                uploadFileOnNet(mPathList.get(0));
                            } else if (mQiniuUploadInterface != null) {
                                mQiniuUploadInterface.onFailed(mErrorPathList);
                            }
                        }

                    }
                }, null);
    }
}
