package com.storyshu.storyshu.activity.login;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.storyshu.storyshu.R;
import com.storyshu.storyshu.activity.base.IBaseActivity;
import com.storyshu.storyshu.activity.story.StoryMapActivity;
import com.storyshu.storyshu.info.UserInfo;
import com.storyshu.storyshu.utils.ParcelableUtil;
import com.storyshu.storyshu.utils.StatusBarUtil;
import com.storyshu.storyshu.utils.ToastUtil;
import com.storyshu.storyshu.utils.number.PhoneFormatCheckUtils;
import com.storyshu.storyshu.widget.imageview.RoundImageView;
import com.storyshu.storyshu.widget.text.RoundTextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 登录界面
 * Created by bear on 2016/12/30.
 */

public class LoginActivity extends IBaseActivity implements View.OnClickListener {
    private static final String TAG = "LoginActivity";
    private RoundImageView mAvatar; //头像
    private EditText mPhoneNumberEdit; //输入电话号码
    private EditText mPasswordEdit; //输入密码
    private View mVerfiyLayout; //验证码的布局
    private EditText mVerifyEdit; //输入验证码
    private RoundTextView mCountdownTextView; //倒计时
    private TextView mLoginButton; //登录按钮
    private View mForgotPassword; //忘记密码
    private TextView mFreeRigister; //免费注册

    private int count = 60; //倒计时
    private Timer mTimer; //定时器
    private boolean isLoginLayout = true; //是否是登录界面

    private View mMapLineBg; //地图的view
    private SensorManager sensorManager; //传感器管理
    private SensorEventListener sensorEventListener;
    private float mGravityX = 0; //保存上一次 x y z 的坐标
    private long mGravityTime; //上一次的时间
    private float MapPosX; //地图的位置变化

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        initView();

    }

    /**
     * 设置重力感应，使背景的地图移动
     */
    private void gravityMap() {
        //获得传感器服务
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        //得到重力感应服务
        final Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        //传感器的回调函数
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float x = event.values[SensorManager.DATA_X];
                //X轴的速度
                float speedX = (x - mGravityX) / (System.currentTimeMillis() - mGravityTime) * 1000;

                if (Math.abs(speedX) > 1.5f) {
                    MapPosX = mMapLineBg.getX() - speedX;
                    if (MapPosX > -mMapLineBg.getWidth() * (0.6 / 1.6f)
                            && MapPosX < 0) {
                        //设置地图图片的位置
                        mMapLineBg.setX(MapPosX);
                    }
                }
                mGravityX = x;
                mGravityTime = System.currentTimeMillis();
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

        //注册listener，第三个参数是检测的精确度
        sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_UI);
    }

    /**
     * 手机号码监听
     */
    private TextWatcher mPhoneWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String phone = s.toString();
            /**
             * 如果没有点击获取验证码，则会检测输入的手机号码是否符合规范
             * 符合就会把获取验证码的背景变为红色，否则为灰色
             *
             * 如果已经点击了则把背景变为灰色
             */
            if (count == 60) {
                if (PhoneFormatCheckUtils.isPhoneLegal(phone))
                    mCountdownTextView.setBgColor(R.color.colorRedPomegranateLight);
                else mCountdownTextView.setBgColor(R.color.colorGrayLight);

            } else {
                mCountdownTextView.setBgColor(R.color.colorGrayLight);
            }

            setLoginButtonBg();

        }
    };

    /**
     * 验证码监听
     */
    private TextWatcher mVerifyNumberWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String num = s.toString();

//            if (isLoginLayout) {
//            } else {
//                if (PhoneFormatCheckUtils.isPhoneLegal(mPhoneNumberEdit.getText().toString())
//                        && !TextUtils.isEmpty(mPasswordEdit.getText())
//                        && !TextUtils.isEmpty(num))
//                    mLoginButton.setBgColor(R.color.colorRedPomegranateLight);
//                else
//                    mLoginButton.setBgColor(R.color.colorGrayLight);
//            }
        }

    };

    /**
     * 密码监听
     */
    private TextWatcher mPasswordWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            setLoginButtonBg();
        }

    };

    /**
     * 设置登录按钮的背景颜色
     */
    private void setLoginButtonBg() {
//        if (isLoginLayout) {
//            if (PhoneFormatCheckUtils.isPhoneLegal(mPhoneNumberEdit.getText().toString())
//                    && !TextUtils.isEmpty(mPasswordEdit.getText()))
//                mLoginButton.setBgColor(R.color.colorRedPomegranateLight);
//            else mLoginButton.setBgColor(R.color.colorGrayLight);
//
//        } else {
//            if (PhoneFormatCheckUtils.isPhoneLegal(mPhoneNumberEdit.getText().toString())
//                    && !TextUtils.isEmpty(mPasswordEdit.getText())
//                    && !TextUtils.isEmpty(mVerifyEdit.getText()))
//                mLoginButton.setBgColor(R.color.colorRedPomegranateLight);
//            else
//                mLoginButton.setBgColor(R.color.colorGrayLight);
//        }
    }


    /**
     * 初始化
     */
    private void initView() {
        //头像
//        mAvatar = (RoundImageView) findViewById(R.id.login_avatar);
//        mAvatar.setOnClickListener(this);

        //状态栏蓝色
        StatusBarUtil.setWindowStatusBarColor(this, R.color.colorBlack);

        //地图
        mMapLineBg = findViewById(R.id.map_line_bg);

        //手机号码
        mPhoneNumberEdit = (EditText) findViewById(R.id.login_phone_number);
        mPhoneNumberEdit.addTextChangedListener(mPhoneWatcher);

        //密码
        mPasswordEdit = (EditText) findViewById(R.id.login_password);
        mPasswordEdit.addTextChangedListener(mPasswordWatcher);

        //验证码布局
        mVerfiyLayout = findViewById(R.id.login_verify_layout);

        //验证码
        mVerifyEdit = (EditText) findViewById(R.id.login_verify_number);
        mVerifyEdit.addTextChangedListener(mVerifyNumberWatcher);

        //获取验证码
        mCountdownTextView = (RoundTextView) findViewById(R.id.login_get_verify_number);
        mCountdownTextView.setOnClickListener(this);


        //登录
        mLoginButton = (TextView) findViewById(R.id.login_login_button);
        mLoginButton.setOnClickListener(this);

        //忘记密码
        mForgotPassword = findViewById(R.id.login_forgot_password);

        //免费注册
        mFreeRigister = (TextView) findViewById(R.id.login_free_register);
        mFreeRigister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_avatar:
                break;
            case R.id.login_get_verify_number:
                getVerifyCode();
                break;

            case R.id.login_login_button:
                login();
                break;

            case R.id.login_free_register:
                changeLayout();
                break;
        }
    }

    /**
     * 获取验证码
     */
    private void getVerifyCode() {

        if (PhoneFormatCheckUtils.isPhoneLegal(mPhoneNumberEdit.getText().toString())) {
            // TODO: 2017/2/6 发送验证码
            startTimer();
        } else {
            ToastUtil.Show(LoginActivity.this, R.string.login_phone_number_illegal);
        }
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                mCountdownTextView.setText(count + "s");
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
        mCountdownTextView.setBgColor(R.color.colorGrayLight);
    }

    /**
     * 停止倒计时
     */
    private void stopTimer() {
        mTimer.cancel();
        mCountdownTextView.setText(R.string.get_verify_number);
        count = 60;
    }

    /**
     * 登录
     */
    private void login() {
        //如果输入的值正确则登录
        if (checkInput()) {
            // TODO: 2016/12/30 请求服务器注册账号，登录账号跳转页面
            //跳转页面
            if (isLoginLayout)
                intentWithFlag(StoryMapActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            else
                intentWithParcelable(ImproveUserDataActivity.class, ParcelableUtil.USER, new UserInfo("", 1, ""));
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

        if (!isLoginLayout) {
            //验证码
            if (TextUtils.isEmpty(mVerifyEdit.getText())) {
                ToastUtil.Show(this, R.string.login_verify_number_empty);
                return false;
            } else if (!isVerifyLegal()) {
                ToastUtil.Show(this, R.string.login_verify_number_illegal);
                return false;
            }
        } else {
            ToastUtil.Show(this, R.string.login_password_illegal);
            return false;
        }

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

    /**
     * 在注册与登录之间切换
     */
    private void changeLayout() {
        if (isLoginLayout) {
            mForgotPassword.setVisibility(View.VISIBLE);
//            mVerfiyLayout.setVisibility(View.VISIBLE);
            mLoginButton.setText(R.string.register);
//            mForgotPassword.setVisibility(View.GONE);
            mFreeRigister.setText(R.string.already_register);
            isLoginLayout = false;

            //
            mPhoneNumberEdit.setText("");
            mPasswordEdit.setText("");
            mVerifyEdit.setText("");
        } else {
            mForgotPassword.setVisibility(View.GONE);
//            mVerfiyLayout.setVisibility(View.GONE);
            mLoginButton.setText(R.string.login);
//            mForgotPassword.setVisibility(View.VISIBLE);
            mFreeRigister.setText(R.string.free_register);
            isLoginLayout = true;

            //
            mPhoneNumberEdit.setText("");
            mPasswordEdit.setText("");
            mVerifyEdit.setText("");
        }

//        mLoginButton.setBgColor(R.color.colorGrayLight);
    }

    @Override
    protected void onResume() {
        super.onResume();

        gravityMap();

    }

    @Override
    protected void onPause() {

        //注销重力传感器的监听
        sensorManager.unregisterListener(sensorEventListener);

        super.onPause();
    }

    @Override
    protected void onStop() {

        super.onStop();
    }
}