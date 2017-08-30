package com.storyshu.storyshu.activity.login

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.storyshu.storyshu.R
import com.storyshu.storyshu.activity.MainActivity
import com.storyshu.storyshu.activity.RegisterActivity
import com.storyshu.storyshu.activity.base.IBaseActivity
import com.storyshu.storyshu.mvp.login.LoginPresenterIml
import com.storyshu.storyshu.mvp.login.LoginView
import com.storyshu.storyshu.utils.KeyBordUtil
import com.storyshu.storyshu.utils.StatusBarUtils
import com.storyshu.storyshu.utils.ToastUtil
import java.util.*

/**
 * 登录界面
 * Created by bear on 2016/12/30.
 */

class LoginActivity : IBaseActivity(), LoginView, View.OnClickListener {
    private var mEmailEdit: EditText? = null //输入电话号码
    private var mPasswordEdit: EditText? = null //输入密码
    private var mLoginButton: TextView? = null //登录按钮
    private var mForgotPassword: View? = null //忘记密码
    private var mFreeRegister: TextView? = null //免费注册

    private var mMapLineBg: View? = null //地图的view

    private val count = 60 //倒计时
    private val mTimer: Timer? = null //定时器

    private var mLoginPresenter: LoginPresenterIml? = null //登录的逻辑控制

    override fun getUsername(): String {
        return mEmailEdit!!.text.toString()
    }

    override fun getPassword(): String {
        return mPasswordEdit!!.text.toString()
    }

    override fun showLoading() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_layout)
        initView()
        mLoginPresenter = LoginPresenterIml(this, this)
    }

    override fun onResume() {
        super.onResume()

        //设置重力感应的地图图片
        gravityMap()
    }

    override fun onPause() {

        //注销重力传感器的监听
        sensorManager!!.unregisterListener(sensorEventListener)
        super.onPause()
    }

    override fun onDestroy() {
        mLoginPresenter!!.distach()
        super.onDestroy()
    }

    override fun intent2MainActivity() {
        runOnUiThread {
            ToastUtil.Show(this, R.string.login_succeed)
        }

        KeyBordUtil.hideKeyboard(this@LoginActivity, mPasswordEdit)

        intentWithFlag(MainActivity::class.java, Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    override fun intent2RegisterActivity() {
        intentTo(RegisterActivity::class.java)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.login_login_button -> mLoginPresenter!!.loginUser()

            R.id.login_free_register -> mLoginPresenter!!.goRegister()

            R.id.login_forgot_password -> mLoginPresenter!!.forgotPassword()
        }
    }

    /**
     * 手机号码监听
     */
    private val mUsernameWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable) {
            val phone = s.toString()
        }
    }

    /**
     * 验证码监听
     */
    private val mVerifyNumberWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable) {
            val num = s.toString()
        }

    }

    /**
     * 密码监听
     */
    private val mPasswordWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable) {

        }

    }


    /**
     * 初始化
     */
    override fun initView() {
        //状态栏
        StatusBarUtils.setColor(this, R.color.colorWhite)

        //地图
        mMapLineBg = findViewById(R.id.map_line_bg)

        //手机号码
        mEmailEdit = findViewById(R.id.login_phone_number) as EditText
        mEmailEdit!!.addTextChangedListener(mUsernameWatcher)

        //密码
        mPasswordEdit = findViewById(R.id.login_password) as EditText
        mPasswordEdit!!.addTextChangedListener(mPasswordWatcher)

        //登录
        mLoginButton = findViewById(R.id.login_login_button) as TextView
        mLoginButton!!.setOnClickListener(this)

        //忘记密码
        mForgotPassword = findViewById(R.id.login_forgot_password)
        mForgotPassword!!.setOnClickListener(this)

        //免费注册
        mFreeRegister = findViewById(R.id.login_free_register) as TextView
        mFreeRegister!!.setOnClickListener(this)
    }

    override fun initData() {

    }

    override fun initEvents() {

    }

    override fun showToast(s: String) {
        ToastUtil.Show(this, s)
    }

    override fun showToast(stringRes: Int) {
        ToastUtil.Show(this, stringRes)
    }

    private var sensorManager: SensorManager? = null //传感器管理
    private var sensorEventListener: SensorEventListener? = null
    private var mGravityX = 0f //保存上一次 x y z 的坐标
    private var mGravityTime: Long = 0 //上一次的时间
    private var MapPosX: Float = 0.toFloat() //地图的位置变化

    /**
     * 设置重力感应，使背景的地图移动
     */
    private fun gravityMap() {
        //获得传感器服务
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        //得到重力感应服务
        val sensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        //传感器的回调函数
        sensorEventListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                val x = event.values[SensorManager.DATA_X]
                //X轴的速度
                val speedX = (x - mGravityX) / (System.currentTimeMillis() - mGravityTime) * 500

                if (Math.abs(speedX) > 1.5f) {
                    MapPosX = mMapLineBg!!.x - speedX
                    if (MapPosX > -mMapLineBg!!.width * (0.6 / 1.6f) && MapPosX < 0) {
                        //设置地图图片的位置
                        mMapLineBg!!.x = MapPosX
                    }
                }
                mGravityX = x
                mGravityTime = System.currentTimeMillis()
            }

            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {

            }
        }

        //注册listener，第三个参数是检测的精确度
        sensorManager!!.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_UI)
    }

    companion object {
        private val TAG = "LoginActivity"
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