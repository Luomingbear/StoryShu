package com.storyshu.storyshu.widget.tag;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.storyshu.storyshu.R;
import com.storyshu.storyshu.widget.text.RoundTextView;

/**
 * 标签view
 * 点击之后会改变背景色
 * Created by bear on 2017/2/22.
 */

public class TagView extends RoundTextView implements View.OnClickListener, View.OnLongClickListener {
    private boolean isCheck = false; //是否处于选中状态
    private int mCheckColor;
    private int mDefColor;
    private String tagString;

    public TagView(Context context) {
        this(context, null);
    }

    public TagView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TagView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    public boolean isCheck() {
        return isCheck;
    }

    /**
     * 恢复标签的文本内容
     */
    public void resumeTagString() {
        isCheck = false;
        setText(tagString);
        setBgColor(mDefColor);
    }

    /**
     * 添加标签的点击响应事件
     */
    public void setOnTagClickChangeListener(OnTagClickChangeListener onTagClickChangeListener) {
        this.onTagClickChangeListener = onTagClickChangeListener;
    }

    private void init() {
        setOnClickListener(this);
        setOnLongClickListener(this);

        mCheckColor = R.color.colorRedLight;
        mDefColor = R.color.colorGrayLight;

        tagString = getText().toString();
    }

    @Override
    public void onClick(View v) {
        if (isCheck) {
            if (onTagClickChangeListener != null)
                onTagClickChangeListener.onDeleteClick(getId());
        } else {
            if (onTagClickChangeListener != null)
                onTagClickChangeListener.onEditClick(getId());
        }
    }


    @Override
    public boolean onLongClick(View v) {
        isCheck = !isCheck;

        if (isCheck) {
            setBgColor(mCheckColor);
            tagString = getText().toString();

            setText(R.string.delete_tag);
        } else {

            setBgColor(mDefColor);
            setText(tagString);
        }
        return true;
    }

    private OnTagClickChangeListener onTagClickChangeListener;


    /**
     * 标签点击响应回调事件
     */
    public interface OnTagClickChangeListener {
        /**
         * 点击了删除操作
         *
         * @param tagId
         */
        void onDeleteClick(int tagId);

        /**
         * 点击了编辑按钮
         *
         * @param tagId
         */
        void onEditClick(int tagId);
    }

}
