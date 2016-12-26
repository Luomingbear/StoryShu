package com.storyshu.storyshu.widget.poilist;

import android.content.Context;
import android.view.Display;
import android.view.WindowManager;

import com.storyshu.storyshu.utils.StatusBarUtil;
import com.storyshu.storyshu.widget.dialog.IBaseDialog;

/**
 * Created by bear on 2016/12/26.
 */

public class AoiDialog extends IBaseDialog {
    public AoiDialog(Context context) {
        super(context);
    }

    public AoiDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public AoiDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    public void Create() {

        WindowManager manager = getWindow().getWindowManager();
        Display display = manager.getDefaultDisplay();
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = display.getWidth();
        params.height = display.getHeight() - StatusBarUtil.getHeight(getContext());
        getWindow().setAttributes(params);
    }
}
