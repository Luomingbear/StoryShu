package com.storyshu.storyshu.utils.net;

import android.util.Log;

import com.storyshu.storyshu.utils.DownloadAsyncTask;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 下载文件的管家
 * Created by bear on 2017/5/13.
 */

public class DownLoadManager extends Thread {
    private static final String TAG = "DownLoadManager";

    private String downloadUrl;
    private String savePath;
    private int maxThread = 3;
    private int pross = 0;

    private OnDownloadListener onDownloadListener;
    private int isfinished = 0;
    private int downloadedAllSize = 0;
    private long file_length;
    private DownloadAsyncTask threads[];

    private int postEndIndex = 0; //当前下载完成几个线程

    public void setOnDownloadListener(OnDownloadListener onDownloadListener) {
        this.onDownloadListener = onDownloadListener;
    }

    public interface OnDownloadListener {
        void onProgressUpdate(int progress);

        void onSucceed();

        void onFailed();
    }


    public DownLoadManager(String downloadUrl, String savePath, int maxThread) {
        this.downloadUrl = downloadUrl;
        this.maxThread = maxThread;
        this.savePath = savePath;
    }

    private Timer timer = new Timer(); //刷新检测进度

    @Override
    public void run() {
        try {
            URL url = new URL(downloadUrl);
            Log.d(TAG, "download file http path:" + downloadUrl);
            URLConnection conn = url.openConnection();
            // 建立链接
            conn.connect();
            // 获取文件流大小，用于更新进度
            file_length = conn.getContentLength();

            threads = new DownloadAsyncTask[maxThread];

            for (int i = 0; i < maxThread; i++) {
                threads[i] = new DownloadAsyncTask(i, maxThread, savePath);
                threads[i].execute(downloadUrl);
                threads[i].setOnDownloadAsyncTaskListener(new DownloadAsyncTask.OnDownloadAsyncTaskListener() {
                    @Override
                    public void onProgressUpdate(int progress) {

                    }

                    @Override
                    public void onPostExecute() {
                        isfinished += 1;
                        if (isfinished == maxThread)
                            if (onDownloadListener != null) {
                                onDownloadListener.onSucceed();
                            }
                    }
                });
            }

            /**
             * 监听下载进度
             */


            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    downloadedAllSize = 0;

                    for (int i = 0; i < maxThread; i++) {
                        downloadedAllSize += threads[i].getDownloadedSize();

                        if (onDownloadListener != null) {
                            onDownloadListener.onProgressUpdate((int) (downloadedAllSize * 100 / file_length));
                        }

                    }

                    if (isfinished == maxThread) {
                        timer.cancel();
                        timer = null;
                    }
                }
            }, 40, 40);


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}