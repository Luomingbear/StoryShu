package com.storyshu.storyshu.mvp.main;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import com.storyshu.storyshu.R;
import com.storyshu.storyshu.bean.checkForUpdate.AppUpdateBean;
import com.storyshu.storyshu.bean.checkForUpdate.VersionResponseBean;
import com.storyshu.storyshu.model.FileModel;
import com.storyshu.storyshu.model.MessageModel;
import com.storyshu.storyshu.model.OnOnlyDataResponseListener;
import com.storyshu.storyshu.mvp.base.IBasePresenter;
import com.storyshu.storyshu.utils.AutoInstall;
import com.storyshu.storyshu.utils.NameUtil;
import com.storyshu.storyshu.utils.VersionUtil;
import com.storyshu.storyshu.utils.net.RetrofitManager;
import com.storyshu.storyshu.utils.sharepreference.ISharePreference;
import com.storyshu.storyshu.widget.dialog.CustomDialog;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 主页的逻辑实现
 * Created by bear on 2017/5/13.
 */

public class MainPresenterIml extends IBasePresenter<MainView> implements MainPresenter {
    private AppUpdateBean appUpdateBean; //版本的信息
    String outPath;    //保存路径


    public MainPresenterIml(Context mContext, MainView mvpView) {
        super(mContext, mvpView);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 1:
                    CustomDialog versionDialog = new CustomDialog.Builder(mContext)
                            .title(appUpdateBean.getTitle())
                            .description(appUpdateBean.getDescription().replace("\\n", "\n"))
                            .leftString(R.string.cancel)
                            .rightString(R.string.update)
                            .onDialogClickListener(new CustomDialog.OnDialogClickListener() {
                                @Override
                                public void onLeftClick() {
//                                    versionDialog.dismiss();
                                }

                                @Override
                                public void onRightClick() {
                                    mMvpView.checkStorgePermission();
                                }
                            })
                            .build();

                    versionDialog.show();
                    break;
            }
        }
    };


    @Override
    public void checkForUpdate() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Call<VersionResponseBean> call = RetrofitManager.getInstance().getService().checkForUpdate();
                call.enqueue(new Callback<VersionResponseBean>() {
                    @Override
                    public void onResponse(Call<VersionResponseBean> call, Response<VersionResponseBean> response) {
                        appUpdateBean = response.body().getData();

                        if (VersionUtil.version < appUpdateBean.getVersion()) {
                            //显示更新提示对话框
                            Message msg = new Message();
                            msg.what = 1;
                            handler.sendMessage(msg);
                        }
                    }

                    @Override
                    public void onFailure(Call<VersionResponseBean> call, Throwable t) {

                    }
                });
            }
        }, 3 * 1000);
    }

    @Override
    public void downloadNewApp() {
        //
        FileModel fileModel = new FileModel(mContext);

        outPath = Environment.getExternalStorageDirectory() + File.separator + NameUtil.APP_NAME
                + File.separator + NameUtil.APP_NAME + ".apk";

        //判断文件夹是否存在
        File dir = new File(Environment.getExternalStorageDirectory() + File.separator + NameUtil.APP_NAME);
        if (!dir.exists()) {
            dir.mkdirs();
        }


        //下载
        fileModel.download(appUpdateBean.getPath(), outPath);
        fileModel.setOnFileModelListener(new FileModel.OnFileModelListener() {
            @Override
            public void onSucceed() {
                AutoInstall.installApk(mContext, new File(outPath));

            }

            @Override
            public void onFailed() {
                mMvpView.showToast(R.string.download_failed);
            }
        });
    }

    /**
     * 设置未读的消息数量
     */
    private MessageModel messageModel = new MessageModel(mContext);
    private Timer timer;

    public void setUnreadNum() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                messageModel.getUnreadNum(ISharePreference.getUserId(mContext), new OnOnlyDataResponseListener() {
                    @Override
                    public void onSucceed(Object obj) {
                        mMvpView.getBottomNavigationBar().setUnreadNum(2, (int) obj);
                    }

                    @Override
                    public void onFalied(String error) {

                    }
                });
            }
        }, 400, 400);

    }

    public void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

}
