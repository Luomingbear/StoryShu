package com.storyshu.storyshu.widget.poilist;

import android.content.Context;
import android.view.Display;
import android.view.WindowManager;

import com.storyshu.storyshu.R;
import com.storyshu.storyshu.utils.StatusBarUtil;
import com.storyshu.storyshu.widget.dialog.IBaseDialog;

/**
 * Created by bear on 2016/12/26.
 */

public class PoiDialog extends IBaseDialog {
    public PoiDialog(Context context) {
        this(context, 0);
    }

    public PoiDialog(Context context, int themeResId) {
        super(context, R.style.MenuDialogTheme);
    }

    public PoiDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
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
