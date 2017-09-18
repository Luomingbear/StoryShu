package com.storyshu.storyshu.activity.create;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.storyshu.storyshu.R;
import com.storyshu.storyshu.activity.base.IBaseActivity;
import com.storyshu.storyshu.mvp.create.story.CreateStoryPresenterImpl;
import com.storyshu.storyshu.mvp.create.story.CreateStoryView;
import com.storyshu.storyshu.utils.KeyBordUtil;
import com.storyshu.storyshu.utils.NameUtil;
import com.storyshu.storyshu.utils.StatusBarUtils;
import com.storyshu.storyshu.utils.ToastUtil;
import com.storyshu.storyshu.widget.SlideButton;
import com.storyshu.storyshu.widget.title.TitleView;

import java.util.ArrayList;
import java.util.List;

/**
 * 编辑短文字
 */
public class CreateStoryActivity extends IBaseActivity implements CreateStoryView, View.OnClickListener {
    private static final String TAG = "CreateStoryActivity";
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
    private List<ImageItem> mOldPicList; //上一次添加的故事的图片列表
    private List<ImageItem> mPicList; //故事的图片列表
    private List<ImageItem> mChangePicPathList; //新增加的图片的列表
    private int maxPicCount = 9;
    private int maxStoryLength = 140;//故事的文字上线
    private CreateStoryPresenterImpl mCreateStoryPresenter; //代理人

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_mic_story);

        initView();

        initData();

        initEvents();

        mCreateStoryPresenter = new CreateStoryPresenterImpl(CreateStoryActivity.this, CreateStoryActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //搜索当前的地点
        mCreateStoryPresenter.getLocationPoi();
    }

    @Override
    public void changeAnonymous() {
        mSlideButton.setCheckedWithAnimation();
        if (mSlideButton.isChecked())
            mRealNameTv.setText(R.string.isAnonymous);
        else mRealNameTv.setText(R.string.real_name);
    }

    @Override
    public void initView() {
        //
        StatusBarUtils.setColor(this, R.color.colorRed);
        //
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
        mOldPicList = new ArrayList<>();
        mPicList = new ArrayList<>();
        mChangePicPathList = new ArrayList<>();
    }

    private TitleView.OnTitleClickListener onTitleClickListener = new TitleView.OnTitleClickListener() {
        @Override
        public void onLeftClick() {
            KeyBordUtil.hideKeyboard(CreateStoryActivity.this, mTitleView);
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

        mSlideButton.setOnSlideButtonClickListener(new SlideButton.OnSlideButtonClickListener() {
            @Override
            public void onClicked(boolean isChecked) {
                if (isChecked) {
                    mRealNameTv.setText(R.string.isAnonymous);
                } else {
                    mRealNameTv.setText(R.string.real_name);
                }
            }
        });
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
    public String getStoryContent() {
        return mStoryEdit.getText().toString();
    }

    @Override
    public List<String> getStoryPic() {
        List<String> list = new ArrayList<>();
        for (ImageItem imageItem : mPicList) {
            list.add(imageItem.path);
        }
        return list;
    }

    @Override
    public TextView getLocationTv() {
        return mLocationTv;
    }

    @Override
    public TextView getLifeTimeTv() {
        return mLifeTimeTv;
    }

    @Override
    public Boolean isAnonymous() {
        return mSlideButton.isChecked();
    }

    @Override
    public void showPicSelector() {
        int count = mPicList == null ? maxPicCount : maxPicCount - mPicList.size();
        Log.i(TAG, "showPicSelector: listSize:" + mPicList.size());
        count = Math.max(0, count);

        if (count > 0) {
            ImagePicker.getInstance()
                    .setMultiMode(true)
                    .setCrop(false)
                    .setSelectLimit(count);

            intent2ImagePickActivity();
        } else ToastUtil.Show(this, getResources().getString(R.string.select_limit, maxPicCount));
    }

    @Override
    public void addPic2Layout() {
        int width = (int) getResources().getDimension(R.dimen.image_big);
        int margin = (int) getResources().getDimension(R.dimen.margin_normal);

        if (mChangePicPathList != null && mChangePicPathList.size() > 0) {
            for (int i = 0; i < mChangePicPathList.size(); i++) {
                ImageView imageView = new ImageView(CreateStoryActivity.this);
                RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(width, width);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams(p);
                params.setMargins(margin, margin, 0, 0);
                imageView.setLayoutParams(params);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                Glide.with(this).load(mChangePicPathList.get(i).path).into(imageView);
                imageView.setTag(i);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCreateStoryPresenter.showPicturePreview((Integer) v.getTag());
                    }
                });

                mPicGridLayout.addView(imageView, mPicGridLayout.getChildCount()-1);
            }
        }
    }

    @Override
    public void backActivity() {
        setResult(NameUtil.REQUST_CREATE, null);
        onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && requestCode == REQUEST_CODE_IMAGE) {
            //保存选择的图片的数据
            mChangePicPathList = null;
            mChangePicPathList = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
            if (mChangePicPathList != null && mChangePicPathList.size() > 0) {
                //添加之前没有的
                for (ImageItem s : mChangePicPathList) {
                    if (s != null)
                        if (!mPicList.contains(s))
                            mPicList.add(s);
                }
            }

            //移除上一次已经添加了的
            if (mOldPicList != null && mOldPicList.size() > 0) {
                for (ImageItem s : mOldPicList) {
                    if (mChangePicPathList.contains(s))
                        mChangePicPathList.remove(s);
                }
            }

            mOldPicList = mChangePicPathList;

            addPic2Layout();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_pic:
                showPicSelector();
                break;

            case R.id.location_layout:
                mCreateStoryPresenter.showLocationDialog();
                break;

            case R.id.life_time_layout:
                mCreateStoryPresenter.setLifeTime();
                break;

            case R.id.anonymous_layout:
                changeAnonymous();
                break;
        }
    }

    @Override
    protected void onDestroy() {
//        mCreateStoryPresenter.distach();
        super.onDestroy();
    }
}
