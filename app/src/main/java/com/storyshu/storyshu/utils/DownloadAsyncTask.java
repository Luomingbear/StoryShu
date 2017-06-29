package com.storyshu.storyshu.utils;

import android.os.AsyncTask;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.net.URLConnection;

/**
 * 后台执行下载文件
 * Created by bear on 2017/5/13.
 */

public class DownloadAsyncTask extends AsyncTask<String, Integer, Void> {
    private static final String TAG = "DownloadAsyncTask";
    private int curThreadIndex = 1; //当前下载的线程
    private int maxThead = 1; //最大的下载线程
    private String savePath;
    private int total_length = 0; //当前下载的总量


    private OnDownloadAsyncTaskListener onDownloadAsyncTaskListener;

    public void setOnDownloadAsyncTaskListener(OnDownloadAsyncTaskListener onDownloadAsyncTaskListener) {
        this.onDownloadAsyncTaskListener = onDownloadAsyncTaskListener;
    }

    public interface OnDownloadAsyncTaskListener {
        void onProgressUpdate(int progress);

        void onPostExecute();
    }

    /**
     * 设置下载的线程数
     *
     * @param downloadIndex 当前的下载线程
     * @param maxThread     总线程
     */
    public DownloadAsyncTask(int downloadIndex, int maxThread, String savePath) {
        this.curThreadIndex = downloadIndex;
        this.maxThead = maxThread;
        this.savePath = savePath;
    }

    @Override
    protected Void doInBackground(String... params) {
        try {
            InputStream inputStream = null;
//            OutputStream outputStream = null;

            try {
                File dir = new File(savePath);
                if (!dir.exists()) {
                    dir.getParentFile().mkdirs();
                }


                /**
                 * 连接
                 */
                URL url = new URL(params[0]);
                URLConnection con = url.openConnection();
                // 建立链接
                con.connect();
                // 获取文件流大小，用于更新进度
                long file_length = con.getContentLength();

                /**
                 * 设置下载的起点和终点
                 */
                long blockLength = file_length / maxThead;
                long beginPosition = (curThreadIndex) * blockLength;  //开始下载的位置
                long endPosition = (curThreadIndex + 1) * blockLength; //结束下载的位置


                URLConnection conn = url.openConnection();
                conn.setRequestProperty("Range", "bytes=" + beginPosition + "-" + endPosition);

                // 建立链接
                conn.connect();
                // 获取输入流
                inputStream = conn.getInputStream();


                int len;
                byte[] data = new byte[1024];

                //保存的文件
                File file = new File(savePath);
                RandomAccessFile outFile = new RandomAccessFile(file, "rwd");
                outFile.setLength(file_length);
                outFile.seek(beginPosition);

                while ((len = inputStream.read(data)) != -1) {
                    total_length += len;
                    int value = (int) ((total_length / (float) file_length) * 100);
                    // 调用update函数，更新进度
                    publishProgress(value);

                    outFile.write(data, 0, len);
                }

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

        return null;
    }

    public int getDownloadedSize() {
        return total_length;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);

        if (onDownloadAsyncTaskListener != null)
            onDownloadAsyncTaskListener.onProgressUpdate(values[0]);
//        Log.i(TAG, "onProgressUpdate: " + values[0] + "-----------" + curThreadIndex);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (onDownloadAsyncTaskListener != null)
            onDownloadAsyncTaskListener.onPostExecute();
    }
}
