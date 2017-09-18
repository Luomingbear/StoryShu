package com.storyshu.storyshu.download

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import java.util.*

/**
 * 下载管家
 * 进行多线程文件下载，支持暂停
 *
 * addDownloadFile() 下载文件
 *
 * Created by Bear on 2017/7/20.
 */
class KTDownloadManager(context: Context) {
    private val TAG = "KTDownloadManager"
    private val context: Context //
    private var onKTDownloadListener: OnKTDownloadListener? = null //下载监听
    private val downloadThreadNum = 3 //下载线程数量
    private var downloadUrl = "" //网络下载地址
    private var savePath = "" //本地保存地址
    val downloadList: MutableList<KTDownloadAsyncTask> = ArrayList<KTDownloadAsyncTask>() //下载线程
    var timer: Timer? = null //定时计算下载的量

    init {
        this.context = context
    }

    /**
     * 下载网络文件
     * @param downloadUrl 下载文件的网络地址
     * @param savePath 下载文件的本地保存地址
     * @param onDownloadListener 下载状态的监听函数
     */
    fun addDownloadFile(downloadUrl: String, savePath: String): KTDownloadManager {
        //
        if (downloadUrl.isNullOrEmpty() || savePath.isNullOrEmpty()) {
            Log.e(TAG, "下载地址或者文件保存路径不能为空")
            return this
        }
        this.downloadUrl = downloadUrl
        this.savePath = savePath

        return this
    }

    /**
     * 定时检测下载的进度
     */
    private fun addTimer() {

        val task = object : TimerTask() {
            override fun run() {
                var progress = 0f
                for (downloadAsy in downloadList) {
                    progress += downloadAsy.getProgress()
                }
                onKTDownloadListener?.onProgress(progress)
//                Log.i(TAG, "下载进度：" + progress * 100)

                if (progress >= 1) {
                    //清楚下载记录
                    for (downloadAsy in downloadList) {
                        downloadAsy.clearDownloadSize()
                    }
                    downloadList.clear()

                    onKTDownloadListener?.onSucceed()

                    timer?.cancel()
                    timer = null
                    Log.d(TAG, "下载完成！")
                }
            }
        }

        if (timer == null)
            timer = Timer()
        timer?.schedule(task, 500, 100)
    }

    /**
     * 暂停下载
     */
    fun pauseDownload() {
        if (downloadList.size > 0) {
            for (downloadAsy in downloadList) {
                downloadAsy.pauseDownload()
                downloadAsy.cancel(true)
            }
        }

        timer?.cancel()
        timer = null
    }

    /**
     * 开始下载
     */
    fun startDownload() {
        if (downloadUrl.isNullOrEmpty() || savePath.isNullOrEmpty())
            return

        downloadList.clear()
        for (i in 0..downloadThreadNum - 1) {
            val downloadAsyncTask = KTDownloadAsyncTask(context, i, downloadThreadNum, savePath)
            downloadList.add(downloadAsyncTask)
            //多线程同时运行，需要调用executeOnExecutor 方法！！！
            downloadAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, downloadUrl)
        }

        addTimer()

    }

    /**
     * 设置监听函数
     */
    fun setOnKTDownloadListener(onKTDownloadListener: OnKTDownloadListener) {
        this.onKTDownloadListener = onKTDownloadListener
    }

    /**
     * 下载进度、状态的监听
     */
    interface OnKTDownloadListener {
        fun onProgress(progress: Float) //下载进度,范围0-1

        fun onSucceed() //下载完毕
    }
}