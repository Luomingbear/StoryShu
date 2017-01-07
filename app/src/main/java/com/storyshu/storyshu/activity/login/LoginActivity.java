package com.storyshu.storyshu.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.storyshu.storyshu.R;
import com.storyshu.storyshu.activity.base.ChooseImageResultActivity;
import com.storyshu.storyshu.activity.storymap.StoryMapActivity;
import com.storyshu.storyshu.info.UserInfo;
import com.storyshu.storyshu.model.database.StoryDateBaseHelper;
import com.storyshu.storyshu.utils.ToastUtil;
import com.storyshu.storyshu.utils.number.PhoneFormatCheckUtils;
import com.storyshu.storyshu.utils.sharepreference.ISharePreference;
import com.storyshu.storyshu.widget.imageview.RoundImageView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 登录界面
 * Created by bear on 2016/12/30.
 */

public class LoginActivity extends ChooseImageResultActivity implements View.OnClickListener {
    private RoundImageView mAvatar; //头像
    private EditText mPhoneNumberEdit; //输入电话号码
    private EditText mVerifyEdit; //输入验证码
    private TextView mCountdown; //倒计时
    private Timer mTimer; //定时器

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        //头像
        mAvatar = (RoundImageView) findViewById(R.id.login_avatar);
        mAvatar.setOnClickListener(this);

        //手机号码
        mPhoneNumberEdit = (EditText) findViewById(R.id.login_phone_number);

        //验证码
        mVerifyEdit = (EditText) findViewById(R.id.login_verify_number);

        //获取验证码
        findViewById(R.id.login_get_verify_number).setOnClickListener(this);
        mCountdown = (TextView) findViewById(R.id.login_countdown);

        //登录
        findViewById(R.id.login_login).setOnClickListener(this);

        //侧试
//        findViewById(R.id.test).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_avatar:
                chooseImage();
                break;
            case R.id.login_get_verify_number:
                getVerifyCode();
                break;

            case R.id.login_login:
                login();
                break;

            case R.id.test:
                intentWithFlag(StoryMapActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                break;
        }
    }

    /**
     * 获取验证码
     */
    private void getVerifyCode() {
        startTimer();
    }


    int count = 60; //倒计时

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                mCountdown.setText(count + "s");
                if (count == 0)
                    stopTimer();
                count--;
            }
        }
    };

    /**
     * 开始倒计时
     * 60s后可以重新获取验证码
     */
    private void startTimer() {
        if (count != 60)
            return;
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
            }
        }, 0, 1000);
    }

    /**
     * 停止倒计时
     */
    private void stopTimer() {
        mTimer.cancel();
        mCountdown.setText(R.string.get_verify_number);
        count = 60;
    }

    /**
     * 登录
     */
    private void login() {
        //如果输入的值正确则登录
        if (checkInput()) {
            // TODO: 2016/12/30 请求服务器注册账号，登录账号跳转页面
            //保存本地数据
            ISharePreference.saveUserData(this, new UserInfo(mPhoneNumberEdit.getText().toString(), 1, mAvatarPath));
            StoryDateBaseHelper storyDateBaseHelper = new StoryDateBaseHelper(LoginActivity.this);
            storyDateBaseHelper.insertUserData(new UserInfo(mPhoneNumberEdit.getText().toString(), 1, mAvatarPath));
            //跳转页面
            intentWithFlag(StoryMapActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        }
    }

    /**
     * 检测输入的值
     *
     * @return 已经输入的值是否正确
     */
    private boolean checkInput() {
        //手机号
        if (TextUtils.isEmpty(mPhoneNumberEdit.getText())) {
            ToastUtil.Show(this, R.string.login_phone_number_empty);
            return false;
        } else if (!PhoneFormatCheckUtils.isPhoneLegal(mPhoneNumberEdit.getText().toString())) {
            ToastUtil.Show(this, R.string.login_phone_number_illegal);
            return false;
        }

        //验证码
        if (TextUtils.isEmpty(mVerifyEdit.getText())) {
            ToastUtil.Show(this, R.string.login_verify_number_empty);
            return false;
        } else if (!isVerifyLegal()) {
            ToastUtil.Show(this, R.string.login_verify_number_illegal);
            return false;
        }

        //头像
//        if (TextUtils.isEmpty(mAvatarPath)) {
//            ToastUtil.Show(this, R.string.login_avatar_empty);
//            return false;
//        }
        return true;
    }

    /**
     * 判断输入的验证码是否正确
     *
     * @return 是、否
     */
    private boolean isVerifyLegal() {
        // TODO: 2016/12/30 检测输入的验证码是否正确
        if (mVerifyEdit.getText().toString().equals("0000"))
            return true;
        return false;
    }

    private String mAvatarPath; //头像地址

    @Override
    public void onResult(String imagePath) {
        mAvatarPath = imagePath;
        ImageLoader.getInstance().displayImage("file://" + imagePath, mAvatar);
    }
}
