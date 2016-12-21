package com.storyshu.storyshu.widget.inputview;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.storyshu.storyshu.R;
import com.storyshu.storyshu.widget.dialog.IBaseDialog;


/**
 * 输入框弹窗
 * Created by ming.luo on 10/28/2016.
 */

public class InputDialog extends IBaseDialog {
    private EditText mEditText; //输入框
    private View mSend; //发送按钮
    private InputMethodManager inputMethodManager;

    public InputDialog(Context context) {
        this(context, 0);
    }

    public InputDialog(Context context, int themeResId) {
        super(context, R.style.MenuDialogTheme);

        setContentView(R.layout.input_dialog_layout);

        initView();

        autoHideView();

    }

    @Override
    public void Create() {
        WindowManager manager = getWindow().getWindowManager();
        Display display = manager.getDefaultDisplay();
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = display.getWidth();
        getWindow().setAttributes(params);
    }

    /**
     * 初始化
     *
     * @param onInputChangeListener
     */
    public void init(OnInputChangeListener onInputChangeListener) {
        this.show();

        showKeyboard();

        setOnInputChangeListener(onInputChangeListener);
    }

    /**
     * 设置默认显示的文本
     *
     * @param oldText
     */
    public void setShowText(String oldText) {
        mEditText.setText(oldText);
    }

    /**
     * 显示键盘
     */
    public void showKeyboard() {
        inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(mEditText, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 隐藏键盘
     */
    public void hideKeyboard() {
        //// TODO: 16/10/29 隐藏键盘 
        inputMethodManager.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }

    private void initView() {
        //背景不变黑
        Window mWindow = getWindow();
        WindowManager.LayoutParams lp = mWindow.getAttributes();
        lp.dimAmount = 0f;

        getWindow().setGravity(Gravity.BOTTOM);

        //输入框
        mEditText = (EditText) findViewById(R.id.input_edit);
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (onInputChangeListener != null)
                    onInputChangeListener.onTextChange(s, start, before, count);
            }

            @Override
            public void afterTextChanged(Editable s) {
                //由文本的时候按钮高亮,否则就是灰的
                if (s.length() != 0)
                    mSend.setBackgroundColor(getContext().getResources().getColor(R.color.colorRedPomegranate));
                else
                    mSend.setBackgroundColor(getContext().getResources().getColor(R.color.colorGrayLight));
            }
        });

        //发送
        mSend = findViewById(R.id.input_send);
        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputDialog.this.dismiss();
                if (onInputChangeListener != null)
                    onInputChangeListener.onSendClick();
            }
        });
    }

    /**
     * 当键盘隐藏的时候隐藏输入框
     */
    private void autoHideView() {
        if (getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED) {
            this.dismiss();
        }
    }

    OnInputChangeListener onInputChangeListener; //输入面板的监听事件

    public void setOnInputChangeListener(OnInputChangeListener onInputChangeListener) {
        this.onInputChangeListener = onInputChangeListener;
    }

    /**
     * 输入面板的监听事件
     */
    public interface OnInputChangeListener {
        //输入的文本变化
        void onTextChange(CharSequence s, int start, int before, int count);

        //发送按钮被点击
        void onSendClick();
    }

}
