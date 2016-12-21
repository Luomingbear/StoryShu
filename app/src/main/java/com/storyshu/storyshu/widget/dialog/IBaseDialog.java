package com.storyshu.storyshu.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;

/**
 * dialog
 * Created by bear on 2016/12/7.
 */

public abstract class IBaseDialog extends Dialog {
    public IBaseDialog(Context context) {
        super(context);
        init();
    }

    public IBaseDialog(Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    public IBaseDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    private void init() {
        requestWindowFeature(Window.FEATURE_NO_TITLE); //取消标题栏
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Create();

    }

    /**
     * 创建的时候初始化，设置大小位置等
     */
    public abstract void Create();
}
