package com.storyshu.storyshu.widget.dialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.storyshu.storyshu.R;

/**
 * 签到的弹窗
 * Created by bear on 2017/3/26.
 */

public class SignDialog extends IBaseDialog {
    private TextView mExplainText; //描述的文本
    private View mOkButton; //确定按钮


    /**
     * 设置提示语，并且显示
     *
     * @param text
     */
    public void setTextAndShow(String text) {
        if (!isShowing())
            this.show();

        mExplainText.setText(text);
    }

    public SignDialog(Context context) {
        super(context);
    }

    public SignDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public SignDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.sign_in_layout;
    }

    @Override
    public void initView() {
        mExplainText = (TextView) findViewById(R.id.explain_text);
        mOkButton = findViewById(R.id.ok);
        mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShowing())
                    dismiss();
            }
        });
    }

    @Override
    public void Create() {

    }
}
