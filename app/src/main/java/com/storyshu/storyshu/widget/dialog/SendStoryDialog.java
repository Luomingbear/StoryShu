package com.storyshu.storyshu.widget.dialog;

import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.WindowManager;

import com.storyshu.storyshu.R;

/**
 * 发布故事的时候的弹窗
 * Created by bear on 2017/2/22.
 */

public class SendStoryDialog extends IBaseDialog {
    public SendStoryDialog(Context context) {
        super(context);
    }

    public SendStoryDialog(Context context, int themeResId) {
        super(context, themeResId);
        setContentView(R.layout.send_story_layout);

        initView();
    }

    @Override
    public void Create() {
        WindowManager manager = getWindow().getWindowManager();
        Display display = manager.getDefaultDisplay();
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = display.getWidth();
//        params.width = display.getWidth();
        getWindow().setAttributes(params);
    }

    private void initView() {
        getWindow().setGravity(Gravity.CENTER);


    }
}
