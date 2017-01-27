package com.storyshu.storyshu.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.storyshu.storyshu.R;
import com.storyshu.storyshu.activity.base.ChooseImageResultActivity;
import com.storyshu.storyshu.activity.story.StoryMapActivity;
import com.storyshu.storyshu.info.UserInfo;
import com.storyshu.storyshu.model.database.StoryDateBaseHelper;
import com.storyshu.storyshu.utils.ParcelableUtil;
import com.storyshu.storyshu.utils.sharepreference.ISharePreference;
import com.storyshu.storyshu.widget.imageview.RoundImageView;

/**
 * 完善用户信息
 * Created by bear on 2017/1/19.
 */

public class ImproveUserDataActivity extends ChooseImageResultActivity implements View.OnClickListener {
    private UserInfo userInfo;
    private RoundImageView avatar;
    private EditText nicknameEdit;
    private String mAvatarPath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_improve_user_data_layout);

        init();

        initView();
    }

    private void init() {
        userInfo = getIntent().getParcelableExtra(ParcelableUtil.USER);
    }

    private void initView() {

        avatar = (RoundImageView) findViewById(R.id.select_avatar);
        avatar.setOnClickListener(this);

        nicknameEdit = (EditText) findViewById(R.id.nickname_edit);

        findViewById(R.id.next).setOnClickListener(this);

    }

    /**
     * 检测输入的数据
     *
     * @return
     */
    private boolean checkDate() {
        return false;
    }

    @Override
    public void onResult(String imagePath) {
        mAvatarPath = imagePath;
        ImageLoader.getInstance().displayImage("file://" + mAvatarPath, avatar);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.select_avatar:
                chooseImage();
                break;
            case R.id.next:
                intent2HomePage();
                break;
        }
    }

    /**
     * 跳转到主页
     */
    private void intent2HomePage() {
        StoryDateBaseHelper storyDateBaseHelper = new StoryDateBaseHelper(ImproveUserDataActivity.this);
        userInfo.setNickname(nicknameEdit.getText().toString());
        userInfo.setAvatar(mAvatarPath);
        storyDateBaseHelper.insertUserData(userInfo);
        ISharePreference.saveUserData(ImproveUserDataActivity.this, userInfo);
        intentWithFlag(StoryMapActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
    }
}
