package com.storyshu.storyshu.widget.story;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.storyshu.storyshu.R;
import com.storyshu.storyshu.widget.text.IBaseEditText;

import java.util.ArrayList;
import java.util.List;

/**
 * 正文编辑的控件
 * 可以添加图片和文本
 * Created by bear on 2016/12/24.
 */

public class StoryEditView extends LinearLayout {
    private static final String TAG = "StoryEditView";
    private List<String> contentList; //正文列表 ,每一段是一个单独的textView，对应单独的String
    private List<IBaseEditText> mEditTextList; //textView的列表
    private int mTextColor; //文本的颜色
    private float mTextSize; //文本的字体大小
    private int mLineSpace; //行间距
    private int mEditTextSpace; //输入框的距离

    //字体设置

    private Context mContext;


    public StoryEditView(Context context) {
        this(context, null);
    }

    public StoryEditView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StoryEditView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.StoryEditView);
        mTextColor = typedArray.getColor(R.styleable.StoryEditView_contentColor, getResources().getColor(R.color.colorBlack));
        mTextSize = typedArray.getDimension(R.styleable.StoryEditView_contentSize, getResources().getDimension(R.dimen.font_normal));
        mLineSpace = (int) typedArray.getDimension(R.styleable.StoryEditView_lineSpace, getResources().getDimension(R.dimen.line_space_big));
        typedArray.recycle();
        init(context);
    }

    /**
     * 获取正文的内容
     *
     * @return
     */
    public String getContent() {
        if (mEditTextList == null)
            return null;
        return mEditTextList.get(0).getText().toString();
    }

    /**
     * 设置正文ø
     *
     * @param content
     */
    public void setContent(String content) {
        if (mEditTextList == null)
            return;
        mEditTextList.get(0).setText(content);
    }

    private void init(Context context) {
        setOrientation(VERTICAL);
        mContext = context;
        mEditTextSpace = mLineSpace * 2;
        //初始化列表
        contentList = new ArrayList<>();
        mEditTextList = new ArrayList<>();

        //默认添加一个textView
        addNewEditText(getResources().getString(R.string.content_hint));
    }

    /**
     * 添加文本编辑框
     */
    private void addNewEditText(String hint) {
        IBaseEditText editText = newEditText();
        editText.setHint(hint);
        mEditTextList.add(editText);
        //请求输入的焦点
        editText.requestFocus();
        addView(editText);
    }


    /**
     * 生成一个新的textView
     *
     * @return textView
     */
    private IBaseEditText newEditText() {
        final IBaseEditText edit = new IBaseEditText(mContext);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        int margin = (int) getResources().getDimension(R.dimen.margin_normal);
        params.setMargins(margin, 0, margin, 0);
        edit.setLayoutParams(params);

        edit.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
        edit.setTextColor(mTextColor);
        //取消下划线
        edit.setBackgroundResource(0);
        edit.setLineSpacing(mLineSpace, 1);

        //设置按键监听
        edit.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP) {
                    switch (keyCode) {
                        //回车键
                        case KeyEvent.KEYCODE_ENTER:
                            String s = edit.getText().toString();
                            contentList.add(s);
//                            addNewEditText();
                            break;

                        //删除键
                        case KeyEvent.KEYCODE_DEL:
//                            clearEditTextList();
                            break;
                    }
                }

                //禁止回车换行
//                return (event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
                return false;
            }
        });
        return edit;
    }

    /**
     * 清除空的edittext
     */
    private void clearEditTextList() {

        int i;
        //清除空的控件
        for (i = 0; i < getChildCount(); i++) {
            IBaseEditText editText = (IBaseEditText) getChildAt(i);
            if (TextUtils.isEmpty(editText.getText().toString())) {
                Log.e(TAG, "clearEditTextList: 移除！！！！");
                removeViewAt(0);
                mEditTextList.remove(editText);
                contentList.remove(editText);
                // TODO: 2016/12/24 删除有问题
                //恢复数据
                int j;
                for (j = 0; j < getChildCount(); j++) {
                    Log.e(TAG, "clearEditTextList: 设置！！！！");
                    mEditTextList.get(j).setText(contentList.get(getChildCount() - j - 1));
                }
            }
        }


    }


}
