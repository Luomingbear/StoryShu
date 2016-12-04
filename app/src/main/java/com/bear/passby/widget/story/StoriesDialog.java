package com.bear.passby.widget.story;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.bear.passby.R;

/**
 * 故事集的Dialog
 * Created by bear on 2016/12/4.
 */

public class StoriesDialog extends Dialog {
    public StoriesDialog(Context context) {
        this(context, 0);
        init(context);
    }

    public StoriesDialog(Context context, int themeResId) {
        super(context, R.style.popDialogTheme);
        init(context);
    }

    public StoriesDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }

    private void init(Context context) {
        requestWindowFeature(Window.FEATURE_NO_TITLE); //取消标题栏

//        LayoutInflater layoutInflater = LayoutInflater.from(context);
//        View layout = layoutInflater.inflate(R.layout.stories_dialog_layout, null);
//        setContentView(layout);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setGravity(Gravity.CENTER);
        WindowManager manager = getWindow().getWindowManager();
        Display display = manager.getDefaultDisplay();
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = display.getWidth();
        params.height = (int) (display.getHeight() * 0.7f);
        getWindow().setAttributes(params);
    }
}
