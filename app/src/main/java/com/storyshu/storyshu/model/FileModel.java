package com.storyshu.storyshu.model;

import android.content.Context;
import android.util.Log;

import com.storyshu.storyshu.R;
import com.storyshu.storyshu.tool.observable.EventObservable;
import com.storyshu.storyshu.utils.net.DownLoadManager;

/**
 * 实现软件的更新下载操作
 * Created by bear on 2017/5/13.
 */

public class FileModel {
    private static final String TAG = "FileModel";
    private Context mContext;
    private OnFileModelListener onFileModelListener;

    public interface OnFileModelListener {
        void onSucceed();

        void onFailed();
    }

    public FileModel(Context mContext) {
        this.mContext = mContext;
    }


    public void setOnFileModelListener(OnFileModelListener onFileModelListener) {
        this.onFileModelListener = onFileModelListener;
    }

    /**
     * 下载更新文件
     *
     * @param urlPath
     */

    public void download(String urlPath, String savePath) {
        //开一个下载线程进行下载
//        DownloadAsyncTask downloadAsyncTask = new DownloadAsyncTask(0, 1, savePath);
//        downloadAsyncTask.execute(urlPath);
//        downloadAsyncTask.setOnDownloadAsyncTaskListener(new DownloadAsyncTask.OnDownloadAsyncTaskListener() {
//            @Override
//            public void onProgressUpdate(int progress) {
//
//                float spend = System.currentTimeMillis() - startTime;
//                Log.d(TAG, "onSucceed: spend time:" + spend);
//                EventObservable.getInstance().notifyObservers(R.id.line_progress_bar, progress);
////                Log.i(TAG, "onProgressUpdate: " + progress);
//            }
//
//            @Override
//            public void onPostExecute() {
//                if (onFileModelListener != null)
//                    onFileModelListener.onSucceed();
//            }
//        });

        EventObservable.getInstance().notifyObservers(R.id.line_progress_bar, 5); //设置默认进度5

        DownLoadManager downLoadManager = new DownLoadManager(urlPath, savePath, 3);
        downLoadManager.start();
        Log.i(TAG, "download: 开始！！");
        downLoadManager.setOnDownloadListener(new DownLoadManager.OnDownloadListener() {
            @Override
            public void onProgressUpdate(int progress) {
                progress = Math.max(progress, 5); //最低的进度5，改善用户的体验
                EventObservable.getInstance().notifyObservers(R.id.line_progress_bar, progress);
            }

            @Override
            public void onSucceed() {
                Log.i(TAG, "onSucceed: 成功！！");

                if (onFileModelListener != null)
                    onFileModelListener.onSucceed();
            }

            @Override
            public void onFailed() {

            }
        });
    }
}
