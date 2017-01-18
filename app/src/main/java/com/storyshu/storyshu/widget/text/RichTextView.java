package com.storyshu.storyshu.widget.text;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.storyshu.storyshu.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 富文本显示view
 * Created by bear on 2017/1/17.
 */

public class RichTextView extends LinearLayout {
    private static final String TAG = "RichTextView";

    public RichTextView(Context context) {
        this(context, null);
    }

    public RichTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RichTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        setOrientation(VERTICAL);
    }

    /**
     * 设置数据内容
     *
     * @param dataString
     */
    public void setData(String dataString) {
        Log.i(TAG, "setDataContent: 开始匹配！！！");
        String regxp = "<[^>]+>"; //匹配文本

        Pattern pattern = Pattern.compile(regxp);
        Matcher matcher = pattern.matcher(dataString);
        StringBuffer sb = new StringBuffer();
        boolean result1 = matcher.find();

        int index = 0;
        while (result1) {
            StringBuffer s = new StringBuffer();
            matcher.appendReplacement(s, "");
            result1 = matcher.find();

            if (TextUtils.isEmpty(s))
                continue;
            createViewByData(index, s.toString());
            index++;
        }
        matcher.appendTail(sb);
    }

    /**
     * 根据类型添加文本或者图片
     *
     * @param index
     * @param s
     */
    private void createViewByData(int index, String s) {
        if (index == 0)
            removeAllViews();

        if (s.substring(0, 3).equals(RichTextEditor.TAG_TXT)) {
            addTextView(index, s.substring(RichTextEditor.TAG_TXT.length()));
        } else if (s.substring(0, 3).equals(RichTextEditor.TAG_IMG)) {
            String path = s.substring(RichTextEditor.TAG_IMG.length());
            addImageViewAtIndex(index, path);
        }
    }

    /**
     * 添加文字view
     *
     * @param index
     * @param text
     */
    private void addTextView(int index, String text) {
        TextView textview = new TextView(getContext());

        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        int margin = (int) getResources().getDimension(R.dimen.margin_normal);
        params.setMargins(0, margin, 0, 0);
        textview.setLayoutParams(params);

        textview.setLineSpacing(getResources().getDimension(R.dimen.line_space), 1);
        textview.setTextColor(getResources().getColor(R.color.colorBlack));
        textview.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_small));
        textview.setText(text);

        addView(textview, index);
    }

    /**
     * 添加图片
     *
     * @param index
     * @param imgPath
     */
    public void addImageViewAtIndex(int index, String imgPath) {
        ImageView imageView = new ImageView(getContext());
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        int margin = (int) getResources().getDimension(R.dimen.margin_normal);
        params.setMargins(0, margin, 0, 0);
        imageView.setLayoutParams(params);

        imageView.setImageBitmap(getScaledBitmap(imgPath, 1024));
//        ImageLoader.getInstance().displayImage(imgPath, imageView);
        addView(imageView, index);
    }

    /**
     * 根据view的宽度，动态缩放bitmap尺寸
     *
     * @param width view的宽度
     */
    private Bitmap getScaledBitmap(String filePath, int width) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        int sampleSize = options.outWidth > width ? options.outWidth / width
                + 1 : 1;
        options.inJustDecodeBounds = false;
        options.inSampleSize = sampleSize;
        return BitmapFactory.decodeFile(filePath, options);
    }
}
