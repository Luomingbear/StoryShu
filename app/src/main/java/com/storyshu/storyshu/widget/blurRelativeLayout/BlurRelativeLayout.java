package com.storyshu.storyshu.widget.blurRelativeLayout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.storyshu.storyshu.R;
import com.storyshu.storyshu.utils.BitmapUtil;
import com.storyshu.storyshu.utils.sharepreference.ISharePreference;

/**
 * 背景模糊的相对布局
 * Created by bear on 2017/1/27.
 */

public class BlurRelativeLayout extends RelativeLayout {
    public BlurRelativeLayout(Context context) {
        this(context, null);
    }

    public BlurRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BlurRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        if (TextUtils.isEmpty(ISharePreference.getUserData(getContext()).getAvatar())) {
            setBackgroundResource(R.drawable.home_bg);
        } else {
            Bitmap bitmap = BitmapFactory.decodeFile(ISharePreference.getUserData(getContext()).getAvatar());
//          Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.home_bg);
            Bitmap blurbmp1 = BitmapUtil.blurBitmap(bitmap, getContext(), 25);
            Bitmap blurbmp = BitmapUtil.blurBitmap(blurbmp1, getContext(), 25);
//            Bitmap blurbmp = BitmapUtil.blurBitmap(bitmap, getContext(), 25);

            ImageView imageView = new ImageView(getContext());
            LayoutParams p = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            imageView.setLayoutParams(p);
            imageView.setImageBitmap(blurbmp);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            addView(imageView);
        }

    }
}
