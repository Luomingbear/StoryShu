package com.storyshu.storyshu.widget.card;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.storyshu.storyshu.R;

/**
 * 自动缩放比例的布局
 * Created by bear on 2016/12/4.
 */

public class AutoScaleLayout extends RelativeLayout {
    private float mScaleRate; //缩放比例

    public AutoScaleLayout(Context context) {
        this(context, null);
    }

    public AutoScaleLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoScaleLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AutoScaleLayout);
        mScaleRate = typedArray.getFloat(R.styleable.AutoScaleLayout_scaleRate, 1.277f);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // 调整宽、高度
        float width = getMeasuredWidth();
        float height = getMeasuredHeight();
        float ratio = height / width; //原始空件的显示比例

        if (ratio < mScaleRate)
            width = height / mScaleRate;
        else if (ratio >= mScaleRate)
            height = width * mScaleRate;

//
//        float boxWidth; //最小包围盒宽度
//        float boxHeight; //最小包围盒高度
////        float side = Math.min(width, height);
//
//        if (mScaleRate >= 1) {
//            boxHeight = height;
//            boxWidth = height / mScaleRate;
////            height = side;
////            width = height / mScaleRate;
//        } else {
//            boxWidth = width;
//            boxHeight = width * mScaleRate;
////            width = side;
////            height = width * mScaleRate;
//        }
//
//        if (boxWidth > width) {
//            height = width * mScaleRate;
//        } else {
//            width = boxWidth;
//        }
//
//        if (boxHeight < height)
//            height = boxHeight;

        ViewGroup.LayoutParams params = getLayoutParams();
        params.width = (int) width;
        params.height = (int) height;
        setLayoutParams(params);
    }
}
