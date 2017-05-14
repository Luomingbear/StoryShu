package com.storyshu.storyshu.widget.dialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.storyshu.storyshu.R;
import com.storyshu.storyshu.widget.text.RoundTextView;

/**
 * 一个通用的弹出框
 * 需要使用CustomDialog.Builder 设置数据
 * Created by bear on 2017/5/13.
 */

public class CustomDialog extends IBaseDialog {
    private TextView mTitleView; //标题栏
    private TextView mDescriptionView; //描述文本
    private RoundTextView mLeftView; //左边的按钮
    private RoundTextView mRightView; //右边的按钮

    public CustomDialog(Context context) {
        this(context, 0);
    }

    public CustomDialog(Context context, int themeResId) {
        super(context, R.style.TransparentDialogTheme);
    }

    public CustomDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.base_dialog_layout;
    }

    @Override
    public void initView() {
        mTitleView = (TextView) findViewById(R.id.title);
        mDescriptionView = (TextView) findViewById(R.id.describe);

        mLeftView = (RoundTextView) findViewById(R.id.left_button);
        mRightView = (RoundTextView) findViewById(R.id.right_button);

    }

    /**
     * 点击回调事件
     */
    public interface OnDialogClickListener {
        //左边被点击
        void onLeftClick();

        //右边被点击
        void onRightClick();
    }


    public static final class Builder {
        private String title; //标题
        private String description; //详细描述

        private String leftString; // 左边按钮的文字
        private String rightString; //右边按钮的文字

        private Context mContext;
        private OnDialogClickListener onDialogClickListener;

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder leftString(String leftString) {
            this.leftString = leftString;
            return this;
        }

        public Builder leftString(int stringRes) {
            this.leftString = mContext.getString(stringRes);
            return this;
        }

        public Builder rightString(String rightString) {
            this.rightString = rightString;
            return this;
        }

        public Builder rightString(int stringRes) {
            this.rightString = mContext.getString(stringRes);
            return this;
        }

        public Builder onDialogClickListener(OnDialogClickListener dialogClickListener) {
            this.onDialogClickListener = dialogClickListener;
            return this;
        }

        public CustomDialog build() {
            if (mContext == null)
                return null;

            final CustomDialog customDialog = new CustomDialog(mContext);

            customDialog.mTitleView.setText(this.title);
            customDialog.mDescriptionView.setText(this.description);
            customDialog.mLeftView.setText(this.leftString);
            customDialog.mRightView.setText(this.rightString);

            customDialog.mLeftView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onDialogClickListener != null)
                        onDialogClickListener.onLeftClick();
                    customDialog.dismiss();

                }
            });

            customDialog.mRightView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onDialogClickListener != null)
                        onDialogClickListener.onRightClick();
                    customDialog.dismiss();

                }
            });

            return customDialog;
        }
    }
}
