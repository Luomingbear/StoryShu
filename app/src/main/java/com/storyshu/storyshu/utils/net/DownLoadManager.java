package com.storyshu.storyshu.utils.net;

import android.util.Log;

import com.storyshu.storyshu.utils.DownloadThread;

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
    private DownloadThread threads[];

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

    Timer timer = new Timer();

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

            threads = new DownloadThread[maxThread];

            for (int i = 0; i < maxThread; i++) {
                long start = file_length / maxThread * i;
                long end = file_length / maxThread * (i + 1);

                threads[i] = new DownloadThread(downloadUrl, savePath, start, end);
                threads[i].start();


            }

            /**
             * 监听下载进度
             */

            downloadedAllSize = 0;

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    for (int i = 0; i < maxThread; i++) {
                        downloadedAllSize += threads[i].getDownloadLength();
                        if (threads[i].isCompleted()) {
                            isfinished++;
                        }

                        if (onDownloadListener != null) {
                            onDownloadListener.onProgressUpdate((int) (downloadedAllSize * 100 / file_length));
                        }

//                        Log.i(TAG, "download: 进度：" + (downloadedAllSize * 100 / file_length));
                    }

                    if (isfinished == maxThread) {
                        timer.cancel();
                        if (onDownloadListener != null) {
                            onDownloadListener.onSucceed();
                        }

                    }
                }
            }, 40, 40);

//            Log.i(TAG, "run: 下载完成！");

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}