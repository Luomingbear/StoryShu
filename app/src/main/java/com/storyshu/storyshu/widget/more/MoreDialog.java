package com.storyshu.storyshu.widget.more;

import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.WindowManager;

import com.storyshu.storyshu.R;
import com.storyshu.storyshu.widget.dialog.IBaseDialog;

/**
 * 更多 弹窗
 * Created by bear on 2016/12/28.
 */

public class MoreDialog extends IBaseDialog {

    public MoreDialog(Context context) {
        this(context, 0);
    }

    public MoreDialog(Context context, int themeResId) {
        super(context, R.style.MenuDialogTheme);
    }

    public MoreDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    public void Create() {
        getWindow().setGravity(Gravity.BOTTOM); //位置
        getWindow().setWindowAnimations(R.style.moreDialogAnimation); //设置进入退出的动画
        WindowManager manager = getWindow().getWindowManager();
        Display display = manager.getDefaultDisplay();

        WindowManager.LayoutParams params = getWindow().getAttributes();

        params.dimAmount = 0.3f; //设置布局之外的空间的透明度，即背景变暗的程度，不是dialog的背景
        params.height = (int) (display.getHeight() * 0.35f);
        params.width = display.getWidth();
        getWindow().setAttributes(params);
    }
}
