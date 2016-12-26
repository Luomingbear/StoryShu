package com.storyshu.storyshu.activity.create;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.storyshu.storyshu.R;
import com.storyshu.storyshu.activity.base.IBaseActivity;
import com.storyshu.storyshu.activity.storymap.StoryMapActivity;
import com.storyshu.storyshu.info.StoryBaseInfo;
import com.storyshu.storyshu.utils.ParcelableUtil;
import com.storyshu.storyshu.utils.sharepreference.ISharePreference;
import com.storyshu.storyshu.widget.imageview.RoundImageView;
import com.storyshu.storyshu.widget.inputview.InputDialog;
import com.storyshu.storyshu.widget.poilist.AoiDialogManger;
import com.storyshu.storyshu.widget.title.TitleView;

/**
 * 封面设置
 * Created by bear on 2016/12/25.
 */

public class CreateCoverActivity extends IBaseActivity implements View.OnClickListener {
    private TextView mTitleTextView; //标题文字显示框
    private TextView mExtraTextView; //摘要文字框
    private TextView mLocationTextView; //所在位置文字框
    private RoundImageView mCoverPic; //封面图
    private static final int IMAGE = 1;
    private static final int CAMERA = 2;
    private StoryBaseInfo mStoryInfo; //获取上个页面传来的数据

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cover_layout);
        //
        initDate();
        //
        initView();

    }

    private void initView() {
        initTitle();

        //选择封面图
        View coverPic = findViewById(R.id.cover_add_image);
        coverPic.setOnClickListener(this);
        mCoverPic = (RoundImageView) findViewById(R.id.cover_detail_pic);
        showImage(ISharePreference.getCoverPic(this));

        //标题文字
        mTitleTextView = (TextView) findViewById(R.id.cover_title);
        mTitleTextView.setOnClickListener(this);
        if (!TextUtils.isEmpty(ISharePreference.getCoverPic(this)))
            mTitleTextView.setText(ISharePreference.getTitle(this));

        //摘要
        mExtraTextView = (TextView) findViewById(R.id.cover_extract);
        mExtraTextView.setText(mStoryInfo.getExtract());
        mExtraTextView.setOnClickListener(this);

        //所在位置
        mLocationTextView = (TextView) findViewById(R.id.cover_location);
        mLocationTextView.setOnClickListener(this);

    }

    /**
     * 获取故事的正文数据
     */
    private void initDate() {
        mStoryInfo = getIntent().getParcelableExtra(ParcelableUtil.STORY);
    }

    private void initTitle() {
        TitleView titleView = (TitleView) findViewById(R.id.title_view);
        titleView.setOnTitleClickListener(new TitleView.onTitleClickListener() {
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
                ISharePreference.saveExtra(CreateCoverActivity.this, "");
                ISharePreference.saveContent(CreateCoverActivity.this, "");
                ISharePreference.saveCoverPic(CreateCoverActivity.this, "");
                ISharePreference.saveTitle(CreateCoverActivity.this, "");

                intentWithFlag(StoryMapActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            }
        });
    }

    /**
     * 调用系统相册
     */
    private void chooseImage() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, IMAGE);
    }

    /**
     * 调用系统相册返回
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //获取相册图片
        if (requestCode == IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            String path = c.getString(columnIndex);
            //显示
            showImage(path);
            c.close();
            ISharePreference.saveCoverPic(CreateCoverActivity.this, path);
        }
    }

    /**
     * 将图片显示出来
     *
     * @param path
     */
    private void showImage(String path) {
        if (!ImageLoader.getInstance().isInited()) {
            DisplayImageOptions options = new DisplayImageOptions.Builder().bitmapConfig(Bitmap.Config.RGB_565).cacheInMemory(true).cacheOnDisk(true).build();
            ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(this).defaultDisplayImageOptions(options).build();
            ImageLoader.getInstance().init(configuration);

        }
        ImageLoader.getInstance().displayImage("file://" + path, mCoverPic);

    }

    /**
     * 显示标题输入框
     */
    private void editTitleShow() {
        final InputDialog titleDialog = new InputDialog(this);
        titleDialog.init(new InputDialog.OnInputChangeListener() {
            @Override
            public void onTextChange(CharSequence s, int start, int before, int count) {
                mTitleTextView.setText(s);
            }

            @Override
            public void onSendClick() {
                titleDialog.hideKeyboard();
            }
        });

        /**
         * 当输入框消失的时候把数据保存在本地
         */
        titleDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                ISharePreference.saveTitle(CreateCoverActivity.this, mTitleTextView.getText().toString());
            }
        });

        titleDialog.setShowText(mTitleTextView.getText().toString());
    }

    /**
     * 显示摘要输入框
     */
    private void editExtraShow() {
        final InputDialog extraDialog = new InputDialog(this);
        extraDialog.init(new InputDialog.OnInputChangeListener() {
            @Override
            public void onTextChange(CharSequence s, int start, int before, int count) {
                mExtraTextView.setText(s);
            }

            @Override
            public void onSendClick() {
                extraDialog.hideKeyboard();
            }
        });

        /**
         * 当输入框消失的时候把数据保存在本地
         */
        extraDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                ISharePreference.saveExtra(CreateCoverActivity.this, mExtraTextView.getText().toString());
            }
        });

        extraDialog.setShowText(mExtraTextView.getText().toString());
    }

    /**
     * 选择当前的位置
     */
    private void chooseAoiLocation() {
        AoiDialogManger.getInstance().showAoiDialog(this).setOnPoiChooseListener(new AoiDialogManger.OnPoiChooseListener() {
            @Override
            public void onChoose(PoiItem choosePoi) {
                mLocationTextView.setText(choosePoi.getTitle());
                AoiDialogManger.getInstance().dismissAoiDialog();
            }
        });


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cover_add_image:
                chooseImage();
                break;
            case R.id.cover_title:
                editTitleShow();
                break;
            case R.id.cover_extract:
                editExtraShow();
                break;
            case R.id.cover_location:
                chooseAoiLocation();
                break;
        }
    }
}
