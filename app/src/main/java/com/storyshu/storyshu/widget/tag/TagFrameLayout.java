package com.storyshu.storyshu.widget.tag;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.storyshu.storyshu.R;
import com.storyshu.storyshu.widget.text.RoundTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * 标签控件布局
 * Created by bear on 2017/2/22.
 */

public class TagFrameLayout extends FrameLayout implements TagView.OnTagClickChangeListener {
    private int mTextColor; //文字颜色
    private float mTextSize; //文字大小
    private int mbgColor; //标签背景颜色
    private float mToundSize; //圆角值的大小
    private float mInterval; //tag间隔的大小
    private String mTagDefText; //标签上默认显示的内容
    private RoundTextView mEditTagView; //添加标签的按钮

    private List<String> mTagList; //标签列表

    private Paint mPaint;

    public TagFrameLayout(Context context) {
        this(context, null);
    }

    public TagFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TagFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TagFrameLayout);
        mTextColor = typedArray.getColor(R.styleable.TagFrameLayout_tagTextColor, getResources().getColor(R.color.colorWhite));
        mTextSize = typedArray.getDimension(R.styleable.TagFrameLayout_tagTextSize, getResources().getDimension(R.dimen.font_normal));
        mToundSize = typedArray.getDimension(R.styleable.TagFrameLayout_tagRoundSize, getResources().getDimension(R.dimen.margin_min));
        mInterval = typedArray.getDimension(R.styleable.TagFrameLayout_tagInterval, getResources().getDimension(R.dimen.margin_min));
        typedArray.recycle();
        init();
    }

    /**
     * 获取标签列表
     *
     * @return
     */
    public List<String> getTagList() {
        mTagList.clear();

        for (int i = 0; i < getChildCount(); i++) {
            if (getChildAt(i) instanceof TagView) {
                TagView tagView = (TagView) getChildAt(i);
                mTagList.add(tagView.getText().toString());
            }
        }
        return mTagList;
    }

    /**
     * 设置标签列表
     *
     * @param tagList
     */
    public void setTagList(List<String> tagList) {
        this.mTagList = tagList;

        for (String s : mTagList) {
            addTagView(s);
        }

        addEditTagView();

        requestLayout();
    }

    public void setOnTagLayoutClickListener(OnTagLayoutClickListener onTagLayoutClickListener) {
        this.onTagLayoutClickListener = onTagLayoutClickListener;
    }

    /**
     * 添加标签
     *
     * @param tagString
     */
    public void addTag(String tagString) {
        mTagList.add(tagString);

        addTagView(tagString);

        addEditTagView();

        requestLayout();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        mTagList = new ArrayList<>();

        mTagDefText = getResources().getString(R.string.add_tag);
        mbgColor = getResources().getColor(R.color.colorGrayLight);
//
//        for (int i = 0; i < 4; i++) {
//            String s = "";
//            for (int j = 0; j <= i; j++) {
//                s += "标签";
//            }
//            mTagList.add(s);
//        }
//
//        for (String s : mTagList) {
//            addTagView(s);
//        }

        addEditTagView();

    }

    /**
     * 生成标签
     *
     * @param tag
     */
    private void addTagView(String tag) {
        TagView tagView = new TagView(getContext());
        tagView.setTextColor(mTextColor);
        tagView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
        tagView.setText(tag);
        tagView.setId(tagView.hashCode()); //设置id
        tagView.setOnTagClickChangeListener(this);

        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        tagView.setLayoutParams(params);
        tagView.setPadding((int) mInterval, (int) mInterval, (int) mInterval, (int) mInterval);

        addView(tagView);
    }

    private OnClickListener editTagClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (onTagLayoutClickListener != null)
                onTagLayoutClickListener.onAddTagClick();
        }
    };

    /**
     * 添加"添加标签"按钮
     */
    private void addEditTagView() {
        if (mEditTagView != null) {
            removeView(mEditTagView);
            mEditTagView = null;
        }
        mEditTagView = new RoundTextView(getContext());
        mEditTagView.setTextColor(mTextColor);
        mEditTagView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
        mEditTagView.setText(R.string.add_tag);
        mEditTagView.setBgColor(R.color.colorRedLight);
        mEditTagView.setId(mEditTagView.hashCode()); //设置id
        mEditTagView.setOnClickListener(editTagClickListener);

        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mEditTagView.setLayoutParams(params);
        mEditTagView.setPadding((int) mInterval, (int) mInterval, (int) mInterval, (int) mInterval);

        addView(mEditTagView);
    }

    float tagY = 0;

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        //添加"添加标签"按钮

        super.onLayout(changed, left, top, right, bottom);

        /**
         * 动态的显示标签，根据标签的内容判断它的位置
         */
        float x = mInterval;


        for (int i = 0; i < getChildCount(); i++) {
            View tagView = getChildAt(i);
            tagView.setX(x);
            tagView.setY(tagY);

            View nextView = getChildAt(i + 1);
            if (nextView == null)
                break;

            x += tagView.getWidth() + mInterval;
            if (x > right - nextView.getWidth()) {
                x = mInterval;
                tagY += tagView.getHeight() + mInterval * 1.5f;
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
//        // Children are just made to fill our space.
//        int childWidthSize = getMeasuredWidth();
//        widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY);
//        heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) (tagY + getChildAt(0).getHeight()), MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    private void resumeTagView() {
        for (int i = 0; i < getChildCount(); i++) {
            if (getChildAt(i) instanceof TagView) {
                TagView tagView = (TagView) getChildAt(i);

                if (tagView.isCheck())
                    tagView.resumeTagString();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                resumeTagView();
                break;
        }
        return true;
    }

    @Override
    public void onDeleteClick(int tagId) {
        for (int i = 0; i < getChildCount(); i++) {
            if (getChildAt(i) instanceof TagView) {
                TagView tagView = (TagView) getChildAt(i);

                if (tagView.getId() == tagId) {
                    removeView(tagView);
                    requestLayout();
                    break;
                }
            }
        }
    }

    @Override
    public void onEditClick(int tagId) {
        for (int i = 0; i < getChildCount(); i++) {
            if (getChildAt(i) instanceof TagView) {
                TagView tagView = (TagView) getChildAt(i);

                if (tagView.isCheck())
                    tagView.resumeTagString();

                if (tagView.getId() == tagId) {
                    if (onTagLayoutClickListener != null)
                        onTagLayoutClickListener.onEditTagClick(tagView);
                }
            }
        }
    }

    private OnTagLayoutClickListener onTagLayoutClickListener;

    public interface OnTagLayoutClickListener {
        /**
         * 点击了添加标签
         */
        void onAddTagClick();

        /**
         * 修改已有的标签
         *
         * @param tagView
         */
        void onEditTagClick(TagView tagView);
    }
}
