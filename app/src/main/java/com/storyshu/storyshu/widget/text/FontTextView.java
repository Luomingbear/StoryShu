package com.storyshu.storyshu.widget.text;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.storyshu.storyshu.R;

/**
 * 可定义字体的textView
 * Created by bear on 2017/1/5.
 */

public class FontTextView extends TextView {
    private Font mFont;

    /**
     * 字体
     */
    private enum Font {

        //汉仪中黑简
        HanYiZhongHeiJian,

        //汉仪长美黑简
        HanYiMeiHei,

        //

    }

    public FontTextView(Context context) {
        this(context, null);
    }

    public FontTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FontTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FontTextView);
        mFont = Font.values()[typedArray.getInt(R.styleable.FontTextView_textFont, 0)];
        init();
    }

    private void init() {
        setTypeface(getFontTypeface());//设置字体
    }

    private Typeface getFontTypeface() {

        AssetManager mgr = getContext().getAssets();//得到AssetManager\
        Typeface tf = Typeface.createFromAsset(mgr, "fonts/HanYiZhongHeiJian.ttf"); //根据路径得到Typeface;
        switch (mFont) {
            case HanYiZhongHeiJian:
                tf = Typeface.createFromAsset(mgr, "fonts/HanYiZhongHeiJian.ttf"); //根据路径得到Typeface
                break;
            case HanYiMeiHei:
                tf = Typeface.createFromAsset(mgr, "fonts/HanYiMeiHei.ttf"); //根据路径得到Typeface
                break;
        }
        return tf;
    }
}
