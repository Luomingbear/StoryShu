package com.storyshu.storyshu.mvp.main

import android.content.Context
import android.os.Environment
import android.os.Handler
import android.os.Message
import com.hyphenate.EMConnectionListener
import com.hyphenate.EMError
import com.hyphenate.chat.EMClient
import com.hyphenate.util.NetUtils
import com.storyshu.storyshu.R
import com.storyshu.storyshu.bean.checkForUpdate.AppUpdateBean
import com.storyshu.storyshu.bean.checkForUpdate.VersionResponseBean
import com.storyshu.storyshu.model.FileModel
import com.storyshu.storyshu.model.MessageModel
import com.storyshu.storyshu.model.OnOnlyDataResponseListener
import com.storyshu.storyshu.mvp.base.IBasePresenter
import com.storyshu.storyshu.utils.AutoInstall
import com.storyshu.storyshu.utils.NameUtil
import com.storyshu.storyshu.utils.VersionUtil
import com.storyshu.storyshu.utils.net.RetrofitManager
import com.storyshu.storyshu.utils.sharepreference.ISharePreference
import com.storyshu.storyshu.widget.dialog.CustomDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.*

/**
 * 主页的逻辑实现
 * Created by bear on 2017/5/13.
 */

class MainPresenterIml(mContext: Context, mvpView: MainView) : IBasePresenter<MainView>(mContext, mvpView), MainPresenter {
    private var appUpdateBean: AppUpdateBean? = null //版本的信息
    private lateinit var outPath: String    //保存路径

    private val handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)

            when (msg.what) {
                1 -> {
                    val versionDialog = CustomDialog.Builder(mContext)
                            .title(appUpdateBean!!.title)
                            .description(appUpdateBean!!.description.replace("\\n", "\n"))
                            .leftString(R.string.cancel)
                            .rightString(R.string.update)
                            .onDialogClickListener(object : CustomDialog.OnDialogClickListener {
                                override fun onLeftClick() {
                                    //                                    versionDialog.dismiss();
                                }

                                override fun onRightClick() {
                                    mMvpView.checkStorgePermission()
                                }
                            })
                            .build()

                    versionDialog!!.show()
                }
            }
        }
    }


    override fun checkForUpdate() {
        val timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                val call = RetrofitManager.getInstance().service.checkForUpdate()
                call.enqueue(object : Callback<VersionResponseBean> {
                    override fun onResponse(call: Call<VersionResponseBean>, response: Response<VersionResponseBean>) {
                        appUpdateBean = response.body().data

                        if (VersionUtil.getVersionName(mContext).toFloat() < (appUpdateBean?.version) ?: 0f) {
                            //显示更新提示对话框
                            val msg = Message()
                            msg.what = 1
                            handler.sendMessage(msg)
                        }
                    }

                    override fun onFailure(call: Call<VersionResponseBean>, t: Throwable) {

                    }
                })
            }
        }, (3 * 1000).toLong())
    }

    override fun downloadNewApp() {
        //
        val fileModel = FileModel(mContext)

        outPath = Environment.getExternalStorageDirectory().toString() + File.separator + NameUtil.APP_NAME +
                File.separator + NameUtil.APP_NAME + ".apk"

        //判断文件夹是否存在
        val dir = File(Environment.getExternalStorageDirectory().toString() + File.separator + NameUtil.APP_NAME)
        if (!dir.exists()) {
            dir.mkdirs()
        }


        //下载
        fileModel.download(appUpdateBean!!.path, outPath)
        fileModel.setOnFileModelListener(object : FileModel.OnFileModelListener {
            override fun onSucceed() {
                AutoInstall.installApk(mContext, File(outPath))

            }

            override fun onFailed() {
                mMvpView.showToast(R.string.download_failed)
            }
        })
    }

    /**
     * 设置未读的消息数量
     */
    private val messageModel = MessageModel(mContext)
    private var timer: Timer? = null

    fun setUnreadNum() {
        timer = Timer()
        timer!!.schedule(object : TimerTask() {
            override fun run() {
                messageModel.getUnreadNum(ISharePreference.getUserId(mContext), object : OnOnlyDataResponseListener {
                    override fun onSucceed(obj: Any) {
                        mMvpView.bottomNavigationBar.setUnreadNum(2, obj as Int)
                    }

                    override fun onFalied(error: String) {

                    }
                })
            }
        }, 400, 400)

    }

    fun stopTimer() {
        if (timer != null) {
            timer!!.cancel()
            timer = null
        }
    }

    /**
     * 添加网络片状态监听
     */
    override fun addConnectListener() {
        EMClient.getInstance().addConnectionListener(connectListener)

    }

    private val connectListener = object : EMConnectionListener {
        override fun onConnected() {

        }

        override fun onDisconnected(p0: Int) {
            if (p0 == EMError.USER_REMOVED) {
                // 显示帐号已经被移除
                mMvpView.showToast(R.string.net_error)
            } else if (p0 == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                // 显示帐号在其他设备登录
                mMvpView.showToast(R.string.user_logined_error)
            } else {
                if (NetUtils.hasNetwork(mContext))
                    //连接不到聊天服务器
                    mMvpView.showToast(R.string.service_connect_error)
                else
                    //当前网络不可用，请检查网络设置
                    mMvpView.showToast(R.string.net_error)
            }
        }
    }


}
