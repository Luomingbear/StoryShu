package com.storyshu.storyshu.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.storyshu.storyshu.R;
import com.storyshu.storyshu.activity.base.ChooseImageResultActivity;
import com.storyshu.storyshu.activity.story.StoryMapActivity;
import com.storyshu.storyshu.info.UserInfo;
import com.storyshu.storyshu.model.database.StoryDateBaseHelper;
import com.storyshu.storyshu.utils.ParcelableUtil;
import com.storyshu.storyshu.utils.ToastUtil;
import com.storyshu.storyshu.utils.sharepreference.ISharePreference;
import com.storyshu.storyshu.widget.imageview.RoundImageView;
import com.storyshu.storyshu.widget.text.RoundTextView;

/**
 * 完善用户信息
 * Created by bear on 2017/1/19.
 */

public class ImproveUserDataActivity extends ChooseImageResultActivity implements View.OnClickListener {
    private UserInfo mUserInfo; //用户信息
    private RoundImageView mAvatar; //头像image view
    private EditText mNicknameEdit; //用户名的编辑框
    private String mAvatarPath; //头像的地址
    private RoundTextView mDoneButton; //完成按钮

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_improve_user_data_layout);

        init();

        initView();
    }

    private void init() {
        mUserInfo = getIntent().getParcelableExtra(ParcelableUtil.USER);
    }

    /**
     * 昵称输入的监听器
     */
    private TextWatcher nicknameWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            setDoneBg();
        }
    };

    private void initView() {

        mAvatar = (RoundImageView) findViewById(R.id.select_avatar);
        mAvatar.setOnClickListener(this);

        mNicknameEdit = (EditText) findViewById(R.id.nickname_edit);
        mNicknameEdit.addTextChangedListener(nicknameWatcher);

        mDoneButton = (RoundTextView) findViewById(R.id.next);
        mDoneButton.setOnClickListener(this);

    }

    @Override
    public void onResult(String imagePath) {
        mAvatarPath = imagePath;
        ImageLoader.getInstance().displayImage("file://" + mAvatarPath, mAvatar);
        setDoneBg();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.select_avatar:
                chooseImage();
                break;
            case R.id.next:
                checkDate();
                break;
        }
    }

    /**
     * 检测输入的数据
     *
     * @return
     */
    private void checkDate() {
        if (TextUtils.isEmpty(mNicknameEdit.getText())) {
            ToastUtil.Show(ImproveUserDataActivity.this, R.string.input_nickname);
            return;
        }
        if (TextUtils.isEmpty(mAvatarPath)) {
            ToastUtil.Show(ImproveUserDataActivity.this, R.string.select_avatar);
            return;
        }
        intent2HomePage();
    }

    /**
     * 跳转到主页
     */
    private void intent2HomePage() {
        StoryDateBaseHelper storyDateBaseHelper = new StoryDateBaseHelper(ImproveUserDataActivity.this);
        mUserInfo.setNickname(mNicknameEdit.getText().toString());
        mUserInfo.setAvatar(mAvatarPath);
        storyDateBaseHelper.insertUserData(mUserInfo);
        ISharePreference.saveUserData(ImproveUserDataActivity.this, mUserInfo);
        intentWithFlag(StoryMapActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    /**
     * 设置完成按钮的背景
     */
    private void setDoneBg() {
        if (!TextUtils.isEmpty(mAvatarPath) &&
                !TextUtils.isEmpty(mNicknameEdit.getText()))
            mDoneButton.setBgColor(R.color.colorRedPomegranateLight);
        else mDoneButton.setBgColor(R.color.colorGrayLight);
    }
}
