package com.storyshu.storyshu.widget.imageview;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * 这只是一个简单的ImageView，可以存放Bitmap和Path等信息
 *
 * @author xmuSistone
 */
public class DataImageView extends AppCompatImageView {

    private String absolutePath;
    private String descripte; //描述

    private Bitmap bitmap;

    public DataImageView(Context context) {
        this(context, null);
    }

    public DataImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DataImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getDescripte() {
        return descripte;
    }

    public void setDescripte(String descripte) {
        this.descripte = descripte;
    }
}