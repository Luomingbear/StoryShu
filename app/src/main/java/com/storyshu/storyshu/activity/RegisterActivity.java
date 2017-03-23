package com.storyshu.storyshu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.TextView;

import com.storyshu.storyshu.R;
import com.storyshu.storyshu.activity.base.IBaseActivity;
import com.storyshu.storyshu.imagepicker.PickerInfo;
import com.storyshu.storyshu.imagepicker.SImagePicker;
import com.storyshu.storyshu.mvp.register.RegisterPresenterIml;
import com.storyshu.storyshu.mvp.register.RegisterView;
import com.storyshu.storyshu.utils.StatusBarUtils;
import com.storyshu.storyshu.widget.imageview.RoundImageView;
import com.storyshu.storyshu.widget.text.RoundTextView;

public class RegisterActivity extends IBaseActivity implements RegisterView, View.OnClickListener {
    private TextInputEditText mUsernameEdit; //用户名的编辑框
    private TextInputEditText mPasswordEdit; //密码的编辑框
    private TextInputEditText mNicknameEdit; //昵称的编辑框
    private TextView mNextButton; //下一步按钮
    private RoundTextView mStepOne; //第一步的圆点
    private RoundTextView mStepTwo; //第二步的圆点
    private RoundImageView mAvatarView; //头像的imageView

    private View mLoginInfoLayout;//登录信息的布局

    private View mUserInfoLayout;//账号信息的布局
    private View mAvatarHnitLayout; //选择头像的提示布局

    private RegisterPresenterIml mRegisterPresenter; //注册账号页面的逻辑实现


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();

        mRegisterPresenter = new RegisterPresenterIml(RegisterActivity.this, RegisterActivity.this);
        initEvents();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public String getUsername() {
        return mUsernameEdit.getText().toString();
    }

    @Override
    public String getPassword() {
        return mPasswordEdit.getText().toString();
    }

    @Override
    public TextView getNextButton() {
        return mNextButton;
    }

    @Override
    public String getNickname() {
        return mNicknameEdit.getText().toString();
    }

    @Override
    public void chooseAvatar() {
        SImagePicker
                .from(RegisterActivity.this)
                .maxCount(1)
                .pickMode(PickerInfo.MODE_AVATAR)
                .rowCount(3)
                .pickMode(PickerInfo.MODE_IMAGE)
                .forResult(REQUEST_CODE_IMAGE);
    }

    @Override
    public void change2StepOne() {
        mLoginInfoLayout.setVisibility(View.VISIBLE);
        mUserInfoLayout.setVisibility(View.GONE);
    }

    @Override
    public void change2StepTwo() {
        mLoginInfoLayout.setVisibility(View.GONE);
        mUserInfoLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void initView() {
        //状态栏
        StatusBarUtils.setColor(RegisterActivity.this, R.color.colorBlack);
        //
        mLoginInfoLayout = findViewById(R.id.login_info_layout);

        mUsernameEdit = (TextInputEditText) findViewById(R.id.username_edit);

        mPasswordEdit = (TextInputEditText) findViewById(R.id.password_edit);

        mNextButton = (TextView) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(this);

        mUserInfoLayout = findViewById(R.id.user_info_layout);

        mAvatarHnitLayout = findViewById(R.id.avatar_hint_layout);

        mAvatarView = (RoundImageView) findViewById(R.id.avatar);
        mAvatarView.setOnClickListener(this);

        mNicknameEdit = (TextInputEditText) findViewById(R.id.nickname_edit);

        mStepOne = (RoundTextView) findViewById(R.id.step_1);

        mStepTwo = (RoundTextView) findViewById(R.id.step_2);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvents() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next_button:
                mRegisterPresenter.nextStep();
                break;

            case R.id.avatar:
                chooseAvatar();
                break;
        }
    }
}
