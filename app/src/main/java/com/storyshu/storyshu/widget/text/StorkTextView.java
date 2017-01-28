package com.storyshu.storyshu.widget.text;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

import com.storyshu.storyshu.R;

import java.lang.reflect.Field;

/**
 * 带描边的textView
 * Created by bear on 2017/1/28.
 */

public class StorkTextView extends TextView {
    private Paint mPaint;
    private int mStorkColor; //描边颜色
    private int mInerColor; //文本颜色

    public StorkTextView(Context context) {
        this(context, null);
    }

    public StorkTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StorkTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.StorkTextView);
        mStorkColor = typedArray.getColor(R.styleable.StorkTextView_storkColor, getResources().getColor(R.color.colorGrayDeep));
        typedArray.recycle();
        mInerColor = getTextColors().getDefaultColor();

        mPaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 描外层
        //super.setTextColor(Color.BLUE); // 不能直接这么设，如此会导致递归
        setTextColorUseReflection(mStorkColor);
        mPaint.setStrokeWidth(3);  // 描边宽度
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE); //描边种类
        mPaint.setFakeBoldText(true); // 外层text采用粗体
        mPaint.setShadowLayer(1, 0, 0, 0); //字体的阴影效果，可以忽略
        super.onDraw(canvas);


        // 描内层，恢复原先的画笔

        //super.setTextColor(Color.BLUE); // 不能直接这么设，如此会导致递归
        setTextColorUseReflection(mInerColor);
        mPaint.setStrokeWidth(0);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setFakeBoldText(false);
        mPaint.setShadowLayer(0, 0, 0, 0);

        super.onDraw(canvas);
    }

    private void setTextColorUseReflection(int color) {
        Field textColorField;
        try {
            textColorField = TextView.class.getDeclaredField("mCurTextColor");
            textColorField.setAccessible(true);
            textColorField.set(this, color);
            textColorField.setAccessible(false);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            mPaint.setColor(color);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
