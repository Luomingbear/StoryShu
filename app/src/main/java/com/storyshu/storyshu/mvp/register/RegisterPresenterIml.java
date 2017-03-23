package com.storyshu.storyshu.mvp.register;

import android.content.Context;

/**
 * mvp模式
 * 注册账号页面的控制实现
 * Created by bear on 2017/3/22.
 */

public class RegisterPresenterIml implements RegisterPresenter {
    private Context mContext;
    private RegisterView mRegisterView;
    private int mStep = 1; //当前在第几步

    public RegisterPresenterIml(Context mContext, RegisterView mRegisterView) {
        this.mContext = mContext;
        this.mRegisterView = mRegisterView;
    }


    @Override
    public void nextStep() {
        if (mStep == 1) {
            mRegisterView.change2StepTwo();
            mStep = 2;
        } else if (mStep == 2) {
            mRegisterView.change2StepOne();
            mStep = 1;
        }
    }

    @Override
    public void registerUser() {

    }
}
