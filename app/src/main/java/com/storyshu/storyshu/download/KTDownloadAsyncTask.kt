package com.storyshu.storyshu.download

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import com.storyshu.storyshu.database.DBManager
import com.storyshu.storyshu.database.KTDownload
import com.storyshu.storyshu.database.KTDownloadDao
import java.io.File
import java.io.IOException
import java.io.RandomAccessFile
import java.net.HttpURLConnection
import java.net.URL

/**
 * 异步下载
 * 支持下载进度的记录，实现断点续传
 * Created by Bear on 2017/7/21.
 */
class KTDownloadAsyncTask(context: Context, curThread: Int, allThread: Int, savePath: String) : AsyncTask<String, Float, Unit>() {
    private val TAG = "DownloadAsyncTask"
    private var context: Context
    private var curThread = 0 //当前线程
    private var allThread = 0 //总线程
    private var startPosition: Long = 0 //开始下载的位置
    private var downloadSize: Long = 0 //已经下载的文件位置
    private var length: Long = 0 //文件的总大小
    private var beginPosition: Long = 0 //线程开始的位置
    private var savePath = "" //保存的本地路径
    private var isComplete = false //是否下载完成
    private var isPause = false //是否暂停

    init {
        this.context = context
        this.curThread = curThread
        this.allThread = allThread
        this.savePath = savePath
    }

    /**
     * 后台下载
     */
    override fun doInBackground(vararg params: String?) {
        var urlConnection: HttpURLConnection? = null //http连接
        var file: RandomAccessFile? = null //保存的文件
        try {
            val url = URL(params[0])
            //用来获取文件长度的链接
            val conn = url.openConnection() as HttpURLConnection
            conn.connectTimeout = 2000
            conn.requestMethod = "GET"

            //如果连接成功就下载
            if (conn.responseCode == HttpURLConnection.HTTP_OK) {
                length = conn.contentLength.toLong()//总大小
                if (length <= 0)
                    return


                // 如果SD卡目录不存在创建
                val folder = File(savePath)
                if (!folder.exists()) {
                    folder.parentFile.mkdirs()
                }

                //创建文件
                val temp = File(savePath)
                if (!temp.exists()) {
                    clearDownloadData(params[0]!!)
                    temp.createNewFile()
                }

                //读取文件
                file = RandomAccessFile(temp, "rwd")
                file.setLength(length)

                //检查上次下载的位置
                checkStartPosition(params[0]!!, length)
                file.seek(startPosition)
                val endPosition: Long = ((curThread + 1) / allThread.toFloat() * length).toLong()
                Log.d(TAG, "线程：" + curThread + " 开始值 ：" + startPosition + ",,,,,, 结束值：" + endPosition)

                /**
                 * 检查是否已经下载完毕
                 */
                if (startPosition >= endPosition) {
                    isComplete = true
                    return
                }

                /**
                 * 连接下载
                 */
                urlConnection = url.openConnection() as HttpURLConnection?
                urlConnection?.connectTimeout = 3000
                urlConnection?.requestMethod = "GET"
                urlConnection?.setRequestProperty("Range", "bytes=" + startPosition + "-" + endPosition)

                //获取输入流
                var inputStream = urlConnection?.inputStream
                val buffer = ByteArray(1024)
                var len = -1
                //下载
                while ((inputStream?.read(buffer, 0, 1024).apply { len = this ?: 0 }) != -1) {
                    //
                    file.write(buffer, 0, len)
                    //当前下载的位置
                    downloadSize += len

                    //保存下载信息
                    updateDownloadSize(params[0]!!, savePath, beginPosition + downloadSize >= endPosition)

                    if (startPosition + downloadSize >= endPosition)
                        return

                    //
                    if (isPause)
                        return
                }

                conn.disconnect()
                inputStream?.close()
            }

        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            urlConnection?.disconnect()
            file?.close()
        }
    }

    /**
     * 搜索数据库检查上一次下载的位置
     * 原理：搜索数据库匹配下载链接和当前的线程数，如果在数据库找到相关记录，则返回记录值
     *      如果没有找到记录，则根据当前的线程和总线程计算开始的位置
     *      @param downloadUrl 下载网址
     *      @param length 文件的总大小
     */
    private fun checkStartPosition(downloadUrl: String, length: Long) {
        //搜索数据库的下载数据
        val downloadDao = DBManager.getReadDaoSession(context).ktDownloadDao
        val downloadList = downloadDao.queryBuilder()
                .where(KTDownloadDao.Properties.Download_id.eq((downloadUrl.plus(curThread + allThread).hashCode()).toLong())).list()

        /**
         * 如果有下载记录，则接着之前的下载
         */
        if (downloadList.size > 0) {
            startPosition = downloadList[0].downloadSize

            //如果已经下载完毕，则不再进行下载
            if (downloadList[0].isCompleted) {
                downloadSize = (length / allThread.toFloat()).toLong()
            } else {
                downloadSize = (startPosition - beginPosition)
            }
        } else {
            startPosition = beginPosition
        }

        //不能小于起点
        startPosition = Math.max(startPosition, beginPosition)
    }

    /**
     * 更新已经下载的大小
     */
    private fun updateDownloadSize(downloadUrl: String, savePath: String, isComplete: Boolean) {
        val downloadSession = DBManager.getWriteDaoSession(context)
        val downloadInfo = KTDownload(downloadUrl.plus(curThread + allThread).hashCode().toLong(), downloadUrl, savePath,
                beginPosition + downloadSize, curThread, isComplete)

        /**
         * 如果之前有数据了，就更新数据
         * 否则就要插入数据
         */
        val downloadList = downloadSession.ktDownloadDao.queryBuilder()
                .where(KTDownloadDao.Properties.Download_id.eq((downloadUrl.plus(curThread + allThread).hashCode()).toLong()),
                        KTDownloadDao.Properties.DownloadUrl.eq(downloadUrl),
                        KTDownloadDao.Properties.CurThread.eq(curThread)).list()

        if (downloadList.isNotEmpty() && downloadList.size > 0) {
            downloadSession.ktDownloadDao.update(downloadInfo)
        } else {
            downloadSession.ktDownloadDao.insertOrReplace(downloadInfo)
        }
    }

    /**
     * 清除之前的下载数据
     */
    private fun clearDownloadData(downloadUrl: String) {
        val downloadSession = DBManager.getWriteDaoSession(context)

        beginPosition = ((curThread / allThread.toFloat()) * length).toLong()

        /**
         * 如果之前有数据了，就删除数据
         * 否则就要插入数据
         */
        val downloadList = downloadSession.ktDownloadDao.queryBuilder()
                .where(KTDownloadDao.Properties.DownloadUrl.eq(downloadUrl)).list()
        if (downloadList.isNotEmpty() && downloadList.size > 0) {
            downloadSession.ktDownloadDao.deleteInTx(downloadList)
        }
        startPosition = beginPosition
    }

    /**
     * 获取已经下载的文件的大小
     */
    fun getDownloadSize(): Long {
        return downloadSize
    }

    /**
     * 获取下载的进度
     */
    fun getProgress(): Float {
        if (length == 0L)
            return 0.0f

        return downloadSize / length.toFloat()
    }

    /**
     * 清除下载记录
     */
    fun clearDownloadSize() {
        downloadSize = 0
    }

    /**
     * 暂停下载
     */
    fun pauseDownload() {
        isPause = true
    }

    override fun onCancelled() {
        super.onCancelled()
        isPause = true
    }
}