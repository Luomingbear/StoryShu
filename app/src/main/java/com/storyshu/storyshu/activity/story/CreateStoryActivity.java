package com.storyshu.storyshu.activity.story;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;

import com.storyshu.storyshu.R;
import com.storyshu.storyshu.activity.base.IBaseActivity;
import com.storyshu.storyshu.info.LocationInfo;
import com.storyshu.storyshu.mvp.create.CreateStoryPresenterImpl;
import com.storyshu.storyshu.mvp.create.CreateStoryView;
import com.storyshu.storyshu.utils.ToastUtil;
import com.storyshu.storyshu.widget.SlideButton;
import com.storyshu.storyshu.widget.title.TitleView;

import java.util.List;

public class CreateStoryActivity extends IBaseActivity implements CreateStoryView, View.OnClickListener {
    private TitleView mTitleView; //标题栏
    private EditText mStoryEdit; //写故事的edit
    private GridLayout mPicGridLayout; //故事图片的布局
    private View mAddPic; //选择图片的按钮
    private View mLocation; //地点按钮布局
    private View mLifeTime; //生命期按钮布局
    private View mRealName; //匿名按钮布局

    private TextView mLocationTv; //位置的描述
    private TextView mLifeTimeTv; //故事的生命期描述
    private TextView mRealNameTv; //匿名的描述

    private SlideButton mSlideButton; //匿名的按钮

    private String mStoryContent; //故事的内容
    private int maxStoryLength = 140;//故事的文字上线

    private CreateStoryPresenterImpl mCreateStoryPresenter; //代理人

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_story);

        initView();

        initData();

        initEvents();

        mCreateStoryPresenter = new CreateStoryPresenterImpl(CreateStoryActivity.this);
    }

    @Override
    public void changeAnonymous() {

    }

    @Override
    public void initView() {
        mTitleView = (TitleView) findViewById(R.id.title_view);

        mStoryEdit = (EditText) findViewById(R.id.story_edit);

        mPicGridLayout = (GridLayout) findViewById(R.id.story_pic_gridlayout);

        mAddPic = findViewById(R.id.add_pic);

        mLocation = findViewById(R.id.location_layout);
        mLocationTv = (TextView) findViewById(R.id.location_text);

        mLifeTime = findViewById(R.id.life_time_layout);
        mLifeTimeTv = (TextView) findViewById(R.id.life_time_text);

        mRealName = findViewById(R.id.anonymous_layout);
        mRealNameTv = (TextView) findViewById(R.id.anonymous_text);
        mSlideButton = (SlideButton) findViewById(R.id.slide_button);
    }

    @Override
    public void initData() {

    }

    private TitleView.OnTitleClickListener onTitleClickListener = new TitleView.OnTitleClickListener() {
        @Override
        public void onLeftClick() {
            finish();
        }

        @Override
        public void onCenterClick() {

        }

        @Override
        public void onCenterDoubleClick() {

        }

        @Override
        public void onRightClick() {
            //发布故事
            mCreateStoryPresenter.issueStory();
        }
    };

    /**
     * 监听用户的输入
     */
    private TextWatcher storyWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String ss = s.toString();
            if (ss.length() > maxStoryLength) {
                ToastUtil.Show(getApplicationContext(), getResources().getString(R.string.story_length_too_long, maxStoryLength));

                //只保留最大限制之内的文本
                mStoryContent = ss.substring(0, maxStoryLength);
                mStoryEdit.setText(mStoryContent);
                mStoryEdit.setSelection(maxStoryLength);
            }
        }
    };

    @Override
    public void initEvents() {
        mTitleView.setOnTitleClickListener(onTitleClickListener);

        mStoryEdit.addTextChangedListener(storyWatcher);

        mAddPic.setOnClickListener(this);

        mLocation.setOnClickListener(this);

        mLifeTime.setOnClickListener(this);

        mRealName.setOnClickListener(this);
    }

    @Override
    public String getStoryContent() {
        return mStoryEdit.getText().toString();
    }

    @Override
    public List<String> getStoryPic() {
        return null;
    }

    @Override
    public LocationInfo getLocation() {
        return null;
    }

    @Override
    public String getLifeTime() {
        return null;
    }

    @Override
    public boolean isAnonymous() {
        return mSlideButton.isChecked();
    }

    @Override
    public void showLocationDialog() {

    }

    @Override
    public void showPicSelector() {

    }

    @Override
    public void showLifeTimeDialog() {

    }

    @Override
    public void backActivity() {

    }

    @Override
    public void toMainActivity() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_pic:
                mCreateStoryPresenter.getPicList();
                break;

            case R.id.location_layout:
                mCreateStoryPresenter.getLocationPoi();
                break;

            case R.id.life_time_layout:
                mCreateStoryPresenter.setLifeTime();
                break;

            case R.id.anonymous_layout:
                mSlideButton.setCheckedWithAnimation();
                break;
        }
    }
}
