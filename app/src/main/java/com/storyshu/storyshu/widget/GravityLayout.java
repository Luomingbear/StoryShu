package com.storyshu.storyshu.widget;

import android.content.Context;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * 能感应重力的布局
 * Created by bear on 2017/3/11.
 */

public class GravityLayout extends RelativeLayout {
    private SensorManager sensorMgr;

    public GravityLayout(Context context) {
        super(context);
    }

    public GravityLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GravityLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child == null)
                break;

            int y = (int) child.getY();
            int h = child.getHeight();
            child.layout(-(int) (r * 0.3f), y, (int) (r * 1.3f), y + h);
        }
    }
}
