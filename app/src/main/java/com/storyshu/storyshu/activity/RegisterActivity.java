package com.storyshu.storyshu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.storyshu.storyshu.R;
import com.storyshu.storyshu.activity.base.IBaseActivity;
import com.storyshu.storyshu.mvp.register.RegisterPresenterIml;
import com.storyshu.storyshu.mvp.register.RegisterView;
import com.storyshu.storyshu.utils.StatusBarUtils;
import com.storyshu.storyshu.utils.ToastUtil;
import com.storyshu.storyshu.widget.imageview.RoundImageView;
import com.storyshu.storyshu.widget.text.RoundTextView;
import com.storyshu.storyshu.widget.title.TitleView;

import java.util.ArrayList;

public class RegisterActivity extends IBaseActivity implements RegisterView, View.OnClickListener {
    private TitleView mTitleView; //标题栏
    private TextInputEditText mUsernameEdit; //用户名的编辑框
    private TextInputEditText mPasswordEdit; //密码的编辑框
    private TextInputEditText mNicknameEdit; //昵称的编辑框
    private TextView mNextButton; //下一步按钮
    private RoundTextView mStepOne; //第一步的圆点
    private RoundTextView mStepTwo; //第二步的圆点
    private int mStep = 1; //当前的步骤
    private RoundImageView mAvatarView; //头像的imageView
    private String mAvatarPath; //头像的地址

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
        if (data != null && requestCode == REQUEST_CODE_IMAGE) {
            ArrayList<ImageItem> list = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
            mAvatarPath = list.get(0).path;
            showAvatar();
        }
    }

    @Override
    public void onBack() {
        onBackPressed();
    }

    @Override
    public String getAvatar() {
        return mAvatarPath;
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
        ImagePicker.getInstance()
                .setMultiMode(false)
                .setCrop(true);
        intent2ImagePickActivity();
    }

    @Override
    public void showAvatar() {
        if (!TextUtils.isEmpty(mAvatarPath)) {
            Glide.with(RegisterActivity.this).load(mAvatarPath).into(mAvatarView);
            mAvatarHnitLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void change2StepOne() {
        mLoginInfoLayout.setVisibility(View.VISIBLE);
        mUserInfoLayout.setVisibility(View.GONE);
        mNextButton.setText(R.string.next);
        mStepTwo.setBgColor(R.color.colorGray);
        mStep = 1;
    }

    @Override
    public void change2StepTwo() {
        mLoginInfoLayout.setVisibility(View.GONE);
        mUserInfoLayout.setVisibility(View.VISIBLE);
        mNextButton.setText(R.string.register);
        mStepTwo.setBgColor(R.color.colorGoldLight);
        mStep = 2;
    }

    @Override
    public void initView() {
        //状态栏
        StatusBarUtils.setColor(RegisterActivity.this, R.color.colorBlack);
        //
        mTitleView = (TitleView) findViewById(R.id.title_view);

        mLoginInfoLayout = findViewById(R.id.login_info_layout);

        mUsernameEdit = (TextInputEditText) findViewById(R.id.username_edit);

        mPasswordEdit = (TextInputEditText) findViewById(R.id.password_edit);

        mNextButton = (TextView) findViewById(R.id.next_button);

        mUserInfoLayout = findViewById(R.id.user_info_layout);

        mAvatarHnitLayout = findViewById(R.id.avatar_hint_layout);

        mAvatarView = (RoundImageView) findViewById(R.id.avatar);

        mNicknameEdit = (TextInputEditText) findViewById(R.id.nickname_edit);

        mStepOne = (RoundTextView) findViewById(R.id.step_1);

        mStepTwo = (RoundTextView) findViewById(R.id.step_2);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvents() {
        mTitleView.setOnTitleClickListener(new TitleView.OnTitleClickListener() {
            @Override
            public void onLeftClick() {
                onBackPressed();
            }

            @Override
            public void onCenterClick() {

            }

            @Override
            public void onCenterDoubleClick() {

            }

            @Override
            public void onRightClick() {

            }
        });

        mNextButton.setOnClickListener(this);

        mAvatarView.setOnClickListener(this);
    }

    @Override
    public void showToast(String s) {
        ToastUtil.Show(this, s);
    }

    @Override
    public void showToast(int stringRes) {
        ToastUtil.Show(this, stringRes);
    }

    @Override
    public void onBackPressed() {
        mRegisterPresenter.onBackPressed();

        if (mStep == 2) {
            change2StepOne();
        } else
            super.onBackPressed();
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
