package com.storyshu.storyshu.widget.dialog;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.storyshu.storyshu.R;
import com.storyshu.storyshu.widget.LifeTimeSelector;

/**
 * 选择故事生命周期的弹窗
 * Created by bear on 2017/3/28.
 */

public class LifeTimeDialog extends IBaseDialog {
    private static final String TAG = "LifeTimeDialog";
    private OnLifeSelectedListener onLifeSelectedListener;

    public void setOnLifeSelectedListener(OnLifeSelectedListener onLifeSelectedListener) {
        this.onLifeSelectedListener = onLifeSelectedListener;
    }

    public interface OnLifeSelectedListener {
        //选择的故事保留时间
        void onSelected(int minute);
    }

    public LifeTimeDialog(Context context) {
        super(context);
    }

    public LifeTimeDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public LifeTimeDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.select_life_time_layout;
    }

    @Override
    public void initView() {
        //时间选择器
        final LifeTimeSelector lifeTimeSelector = (LifeTimeSelector) findViewById(R.id.life_time_selector);

        //取消
        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        //确定
        findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: 选择的时间：" + lifeTimeSelector.getLifeTime());
                if (onLifeSelectedListener != null)
                    onLifeSelectedListener.onSelected(lifeTimeSelector.getLifeTime());
                dismiss();
            }
        });
    }
}
