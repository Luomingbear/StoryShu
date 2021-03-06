package com.storyshu.storyshu.widget.inputview;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.storyshu.storyshu.R;
import com.storyshu.storyshu.widget.dialog.IBaseDialog;

import java.util.Timer;
import java.util.TimerTask;


/**
 * 输入框弹窗
 * Created by ming.luo on 10/28/2016.
 */

public class InputDialog extends IBaseDialog {
    private static final String TAG = "InputDialog";
    private EditText mEditText; //输入框
    private View mSend; //发送按钮

    public InputDialog(Context context) {
        this(context, 0);
    }

    public InputDialog(Context context, int themeResId) {
        super(context, R.style.MenuDialogTheme);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.input_dialog_layout;
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
        /***
         * 由于界面为加载完全而无法弹出软键盘所以延时一段时间弹出键盘
         */
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
                           public void run() {
                               InputMethodManager inputMethodManager =
                                       (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                               inputMethodManager.showSoftInput(mEditText, 0);
                           }
                       },
                180);
    }

    /**
     * 隐藏键盘
     */
    public void hideKeyboard() {
        Log.i(TAG, "hideKeyboard: 隐藏");
        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
        InputDialog.this.dismiss();
    }

    @Override
    public void initView() {
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
                    mSend.setBackgroundColor(getContext().getResources().getColor(R.color.colorRed));
                else
                    mSend.setBackgroundColor(getContext().getResources().getColor(R.color.colorGrayLight));
            }
        });

        //发送
        mSend = findViewById(R.id.input_send);
        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onInputChangeListener != null)
                    onInputChangeListener.onSendClick(mEditText.getText().toString());
                //
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
        void onSendClick(String content);
    }

}
