package com.storyshu.storyshu.utils;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.net.URLConnection;

/**
 * 下载文件线程
 * Created by bear on 2017/5/13.
 */

public class DownloadThread extends Thread {
    private static final String TAG = "DownloadThread";
    private String downloadUrl = "";
    private String savePath = "";
    //    private String savePath;
    private long start;
    private long end;

    private long downloadLength = 0; //已经下载的大小
    private float progress = 0;

    /**
     * 当前下载是否完成
     */
    private boolean isCompleted = false;

    public DownloadThread(String url, String savePath, long start, long end) {
        this.start = start;
        this.end = end;
        this.downloadUrl = url;
        this.savePath = savePath;
    }

    @Override
    public void run() {
//        super.run();
        try {
            InputStream inputStream = null;
//            OutputStream outputStream = null;

            try {


                /**
                 * 设置下载的起点和终点
                 */
                URL url = new URL(downloadUrl);
                URLConnection conn = url.openConnection();
                conn.setRequestProperty("Range", "bytes=" + start + "-" + end);

                // 建立链接
                conn.connect();
                // 获取输入流
                inputStream = conn.getInputStream();


                int len;
                byte[] data = new byte[1024];

                //保存的文件
                File file = new File(savePath);
                RandomAccessFile outFile = new RandomAccessFile(file, "rwd");
//                outFile.setLength(file_length);
                outFile.seek(start);

                while ((len = inputStream.read(data)) != -1) {
                    outFile.write(data, 0, len);
                    //
                    downloadLength += len;
                    progress = (downloadLength / ((end - start) * 100.0f));
                    Log.i(TAG, "run: 进度：" + downloadLength / ((end - start) * 100.0f) + "-------------");
                }

                isCompleted = true;

                outFile.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally

            {
                if (inputStream != null) {
                    inputStream.close();
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取当前下载的总量
     *
     * @return
     */
    public long getDownloadLength() {
        return downloadLength;
    }

    /**
     * 线程文件是否下载完毕
     */
    public boolean isCompleted() {
        return isCompleted;
    }
}
