package com.storyshu.storyshu.model;

import android.content.Context;
import android.util.Log;

import com.storyshu.storyshu.R;
import com.storyshu.storyshu.download.KTDownloadManager;
import com.storyshu.storyshu.tool.observable.EventObservable;

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

        KTDownloadManager downloadManager = new KTDownloadManager(mContext);
        downloadManager.addDownloadFile(urlPath, savePath);
        downloadManager.setOnKTDownloadListener(new KTDownloadManager.OnKTDownloadListener() {
            @Override
            public void onProgress(float progress) {
                Log.i(TAG, "onProgress: " + progress);
                progress = Math.max(0.05f, progress);
                EventObservable.getInstance().notifyObservers(R.id.line_progress_bar, (int) (progress * 100));
            }

            @Override
            public void onSucceed() {
                if (onFileModelListener != null)
                    onFileModelListener.onSucceed();
            }
        });
        downloadManager.startDownload();

    }
}
