package com.storyshu.storyshu.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.storyshu.storyshu.utils.SysUtils;

/**
 * dialog
 * Created by bear on 2016/12/7.
 */

public abstract class IBaseDialog extends Dialog {

    public IBaseDialog(Context context) {
        super(context);
        init();
    }

    public IBaseDialog(Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    public IBaseDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    private void init() {
        requestWindowFeature(Window.FEATURE_NO_TITLE); //取消标题栏

        if (getLayoutRes() != 0)
            setContentView(getLayoutRes()); //设置布局的id
        initView(); //初始化视图
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Create();
    }

    /**
     * 设置布局的id
     *
     * @return layoutID
     */
    public abstract int getLayoutRes();

    /**
     * 初始化视图
     */
    public abstract void initView();

    /**
     * 创建的时候初始化，设置大小位置等
     */
    public void Create() {
        //设置宽度和高度；
        int width = (int) (SysUtils.getScreenWidth(getContext()) * 0.85f);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = width;
        getWindow().setAttributes(params);
    }
}
