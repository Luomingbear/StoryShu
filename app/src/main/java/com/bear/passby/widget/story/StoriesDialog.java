package com.bear.passby.widget.story;

import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.WindowManager;

import com.bear.passby.R;
import com.bear.passby.widget.dialog.IBaseDialog;

/**
 * 故事集的Dialog
 * Created by bear on 2016/12/4.
 */

public class StoriesDialog extends IBaseDialog {
    public StoriesDialog(Context context) {
        this(context, 0);
    }

    public StoriesDialog(Context context, int themeResId) {
        super(context, R.style.StoryDialogTheme);
    }

    public StoriesDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    public void Create() {
        getWindow().setGravity(Gravity.CENTER); //位置
        WindowManager manager = getWindow().getWindowManager();
        Display display = manager.getDefaultDisplay();
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = display.getWidth();
        params.height = (int) (display.getHeight() * 0.6f);
        getWindow().setAttributes(params);
    }
}
