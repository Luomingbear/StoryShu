package com.storyshu.storyshu.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;

import com.storyshu.storyshu.R;
import com.storyshu.storyshu.activity.base.IBaseActivity;
import com.storyshu.storyshu.activity.storymap.StoryMapActivity;
import com.storyshu.storyshu.info.UserInfo;
import com.storyshu.storyshu.model.database.StoryDateBaseHelper;
import com.storyshu.storyshu.utils.ParcelableUtil;
import com.storyshu.storyshu.utils.sharepreference.ISharePreference;

/**
 * 完善用户信息
 * Created by bear on 2017/1/19.
 */

public class ImproveUserDataActivity extends IBaseActivity {
    private UserInfo userInfo;
    private EditText nicknameEdit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.improve_user_data_layout);

        init();

        initView();
    }

    private void init() {
        userInfo = getIntent().getParcelableExtra(ParcelableUtil.USER);
    }

    private void initView() {
        nicknameEdit = (EditText) findViewById(R.id.nickname_edit);

        findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StoryDateBaseHelper storyDateBaseHelper = new StoryDateBaseHelper(ImproveUserDataActivity.this);
                userInfo.setNickname(nicknameEdit.getText().toString());
                storyDateBaseHelper.insertUserData(userInfo);
                ISharePreference.saveUserData(ImproveUserDataActivity.this, userInfo);
                intentWithFlag(StoryMapActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            }
        });

    }
}
