package com.storyshu.storyshu.activity.login;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.storyshu.storyshu.R;
import com.storyshu.storyshu.activity.MainActivity;
import com.storyshu.storyshu.activity.RegisterActivity;
import com.storyshu.storyshu.activity.base.IBaseActivity;
import com.storyshu.storyshu.mvp.login.LoginPresenterIml;
import com.storyshu.storyshu.mvp.login.LoginView;
import com.storyshu.storyshu.utils.StatusBarUtils;
import com.storyshu.storyshu.utils.ToastUtil;

import java.util.Timer;

/**
 * 登录界面
 * Created by bear on 2016/12/30.
 */

public class LoginActivity extends IBaseActivity implements LoginView, View.OnClickListener {
    private static final String TAG = "LoginActivity";
    private EditText mEmailEdit; //输入电话号码
    private EditText mPasswordEdit; //输入密码
    private TextView mLoginButton; //登录按钮
    private View mForgotPassword; //忘记密码
    private TextView mFreeRegister; //免费注册

    private View mMapLineBg; //地图的view

    private int count = 60; //倒计时
    private Timer mTimer; //定时器

    private LoginPresenterIml mLoginPresenter; //登录的逻辑控制

    @Override
    public String getUsername() {
        return mEmailEdit.getText().toString();
    }

    @Override
    public String getPassword() {
        return mPasswordEdit.getText().toString();
    }

    @Override
    public void showLoading() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        initView();
        mLoginPresenter = new LoginPresenterIml(this, this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //设置重力感应的地图图片
        gravityMap();
    }

    @Override
    protected void onPause() {

        //注销重力传感器的监听
        sensorManager.unregisterListener(sensorEventListener);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mLoginPresenter.distach();
        super.onDestroy();
    }

    @Override
    public void intent2MainActivity() {
        intentWithFlag(MainActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    @Override
    public void intent2RegisterActivity() {
        intentTo(RegisterActivity.class);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_login_button:
                mLoginPresenter.loginUser();
                break;

            case R.id.login_free_register:
                mLoginPresenter.goRegister();
                break;

            case R.id.login_forgot_password:
                mLoginPresenter.forgotPassword();
        }
    }

    /**
     * 手机号码监听
     */
    private TextWatcher mUsernameWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String phone = s.toString();
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

        }

    };


    /**
     * 初始化
     */
    @Override
    public void initView() {
        //状态栏
        StatusBarUtils.setColor(this, R.color.colorBlack);

        //地图
        mMapLineBg = findViewById(R.id.map_line_bg);

        //手机号码
        mEmailEdit = (EditText) findViewById(R.id.login_phone_number);
        mEmailEdit.addTextChangedListener(mUsernameWatcher);

        //密码
        mPasswordEdit = (EditText) findViewById(R.id.login_password);
        mPasswordEdit.addTextChangedListener(mPasswordWatcher);

        //登录
        mLoginButton = (TextView) findViewById(R.id.login_login_button);
        mLoginButton.setOnClickListener(this);

        //忘记密码
        mForgotPassword = findViewById(R.id.login_forgot_password);
        mForgotPassword.setOnClickListener(this);

        //免费注册
        mFreeRegister = (TextView) findViewById(R.id.login_free_register);
        mFreeRegister.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvents() {

    }

    @Override
    public void showToast(String s) {
        ToastUtil.Show(this, s);
    }

    @Override
    public void showToast(int stringRes) {
        ToastUtil.Show(this, stringRes);
    }

    private SensorManager sensorManager; //传感器管理
    private SensorEventListener sensorEventListener;
    private float mGravityX = 0; //保存上一次 x y z 的坐标
    private long mGravityTime; //上一次的时间
    private float MapPosX; //地图的位置变化

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
                float speedX = (x - mGravityX) / (System.currentTimeMillis() - mGravityTime) * 500;

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


//    private Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            if (msg.what == 1) {
//                mCountdownTextView.setText(count + "s");
//                if (count == 0)
//                    stopTimer();
//                count--;
//            }
//        }
//    };

    /**
     * 开始倒计时
     * 60s后可以重新获取验证码
     */
//    private void startTimer() {
//        if (count != 60)
//            return;
//        mTimer = new Timer();
//        mTimer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                Message msg = new Message();
//                msg.what = 1;
//                handler.sendMessage(msg);
//            }
//        }, 0, 1000);
//        mCountdownTextView.setBgColor(R.color.colorGrayLight);
//    }

    /**
     * 停止倒计时
     */
//    private void stopTimer() {
//        mTimer.cancel();
//        mCountdownTextView.setText(R.string.get_verify_number);
//        count = 60;
//    }
}