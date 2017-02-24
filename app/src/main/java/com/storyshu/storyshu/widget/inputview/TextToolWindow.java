package com.storyshu.storyshu.widget.inputview;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.PopupWindow;

import com.storyshu.storyshu.R;

/**
 * 编辑故事正文时的工具
 * Created by bear on 2017/2/14.
 */

public class TextToolWindow extends PopupWindow {
    private Context mContext;

    public TextToolWindow(Context context) {
        this(context, null);
    }

    public TextToolWindow(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextToolWindow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    public void init(Window window) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.text_edit_tool_layout, null);
        setContentView(view);

        //是否可以点击
        setTouchable(true);
        //点击外部消失
        setOutsideTouchable(false);
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        setBackgroundDrawable(new BitmapDrawable());

        //设置背景颜色
//        WindowManager.LayoutParams lp = window.getAttributes();
//        lp.dimAmount = 0.7f; //0.0-1.0
//        window.setAttributes(lp);

        //设置宽度和高度；
        Display display = window.getWindowManager().getDefaultDisplay();
        float height = mContext.getResources().getDimension(R.dimen.selector_height);
        setWidth(display.getWidth());
        setHeight((int) height);

        //
        view.findViewById(R.id.text_tool_undo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onTextToolClickListener != null)
                    onTextToolClickListener.FirstClick();
            }
        });
        view.findViewById(R.id.text_tool_tab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onTextToolClickListener != null)
                    onTextToolClickListener.SecondClick();
            }
        });
        view.findViewById(R.id.text_tool_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onTextToolClickListener != null)
                    onTextToolClickListener.ThirdClick();
            }
        });

    }

    private OnTextToolClickListener onTextToolClickListener;

    /**
     * 设置点击监听
     *
     * @param onTextToolClickListener
     */
    public void setOnTextToolClickListener(OnTextToolClickListener onTextToolClickListener) {
        this.onTextToolClickListener = onTextToolClickListener;
    }

    public interface OnTextToolClickListener {
        //左边起第一个按钮被点击
        void FirstClick();

        //左边起第二个按钮被点击
        void SecondClick();

        //左边起第三个按钮被点击
        void ThirdClick();


    }
}
