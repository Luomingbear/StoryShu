package com.storyshu.storyshu.widget.story;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.storyshu.storyshu.utils.StatusBarUtil;

/**
 * 封面控件
 * Created by bear on 2016/12/26.
 */

public class CoverLayout extends RelativeLayout {
    public CoverLayout(Context context) {
        super(context);
    }

    public CoverLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CoverLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 调整宽、高度和屏幕一样大
        float width = getMeasuredWidth();
        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        float height = wm.getDefaultDisplay().getHeight();

        ViewGroup.LayoutParams params = getLayoutParams();
        params.width = (int) width;
        params.height = (int) height - StatusBarUtil.getHeight(getContext());
//        params.height = (int) height;
        setLayoutParams(params);
    }
}
