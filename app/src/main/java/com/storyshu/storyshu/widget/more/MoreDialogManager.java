package com.storyshu.storyshu.widget.more;

import android.content.Context;
import android.view.View;

import com.storyshu.storyshu.R;

/**
 * 更多的按钮弹窗管家
 * Created by bear on 2016/12/28.
 */

public class MoreDialogManager implements View.OnClickListener {
    private static MoreDialogManager instance;
    private MoreDialog mMoreDialog; //更多的dialog
    private Context mContext;


    public static MoreDialogManager getInstance() {
        if (instance == null) {
            synchronized (MoreDialogManager.class) {
                if (instance == null)
                    instance = new MoreDialogManager();
            }
        }
        return instance;
    }

    protected MoreDialogManager() {
    }

    /**
     * 显示菜单弹窗
     *
     * @param context
     */
    public void showMoreDialog(Context context) {
        if (mMoreDialog != null && mMoreDialog.isShowing()) {
            return;
        }

        mContext = context;
        mMoreDialog = new MoreDialog(context);
        mMoreDialog.setContentView(R.layout.more_dialog_layout);
        mMoreDialog.show();
        initView();
    }

    /**
     * 隐藏
     */
    public void dismissMoreDialog() {
        if (mMoreDialog == null)
            return;
        if (mMoreDialog.isShowing())
            mMoreDialog.dismiss();
    }

    private void initView() {
        //
        View longPic = mMoreDialog.findViewById(R.id.more_long_pic);
        longPic.setOnClickListener(this);

        //
        View cover = mMoreDialog.findViewById(R.id.more_cover);
        cover.setOnClickListener(this);

        //
        View report = mMoreDialog.findViewById(R.id.more_report);
        report.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.more_long_pic:
                if (onMoreDialogClickListener != null)
                    onMoreDialogClickListener.onLongPicClick();
                break;
            case R.id.more_cover:
                if (onMoreDialogClickListener != null)
                    onMoreDialogClickListener.onCoverClick();
                break;
            case R.id.more_report:
                if (onMoreDialogClickListener != null)
                    onMoreDialogClickListener.onReportClick();
                break;
        }
    }

    private OnMoreDialogClickListener onMoreDialogClickListener;

    public MoreDialogManager setOnMoreDialogClickListener(OnMoreDialogClickListener onMoreDialogClickListener) {
        this.onMoreDialogClickListener = onMoreDialogClickListener;
        return this;
    }

    public interface OnMoreDialogClickListener {
        /**
         * 长截图
         */
        void onLongPicClick();

        /**
         * 封面
         */
        void onCoverClick();

        /**
         * 举报
         */
        void onReportClick();
    }

}
