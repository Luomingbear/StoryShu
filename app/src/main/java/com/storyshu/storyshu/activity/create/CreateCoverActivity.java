package com.storyshu.storyshu.activity.create;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.storyshu.storyshu.R;
import com.storyshu.storyshu.activity.base.ChooseImageResultActivity;
import com.storyshu.storyshu.activity.story.StoryMapActivity;
import com.storyshu.storyshu.info.StoryInfo;
import com.storyshu.storyshu.model.database.StoryDateBaseHelper;
import com.storyshu.storyshu.model.location.ILocationManager;
import com.storyshu.storyshu.model.location.ILocationQueryTool;
import com.storyshu.storyshu.utils.ParcelableUtil;
import com.storyshu.storyshu.utils.ToastUtil;
import com.storyshu.storyshu.utils.sharepreference.ISharePreference;
import com.storyshu.storyshu.utils.time.ConvertTimeUtil;
import com.storyshu.storyshu.widget.imageview.RoundImageView;
import com.storyshu.storyshu.widget.inputview.InputDialog;
import com.storyshu.storyshu.widget.poilist.PoiDialogManger;
import com.storyshu.storyshu.widget.title.TitleView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 封面设置
 * Created by bear on 2016/12/25.
 */

public class CreateCoverActivity extends ChooseImageResultActivity implements View.OnClickListener {
    private static final String TAG = "CreateCoverActivity";
    private TextView mTitleTextView; //标题文字显示框
    private TextView mExtraTextView; //摘要文字框
    private TextView mLocationTextView; //所在位置文字框
    private RoundImageView mCoverPic; //封面图
    private TextView mCreateTime; //创建时间
    private static final int IMAGE = 1;
    private static final int CAMERA = 2;
    private StoryInfo mStoryInfo; //故事的数据
    private String coverPicPath; //封面图片的地址
    private List<PoiItem> poiItemList; //搜索的结果的列表
    private int radius = 10; //搜索的半径


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_cover_layout);
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
        mExtraTextView.setText(ISharePreference.getExtra(CreateCoverActivity.this));
        mExtraTextView.setOnClickListener(this);

        //所在位置
        mLocationTextView = (TextView) findViewById(R.id.cover_location);
        mLocationTextView.setOnClickListener(this);

        //创建时间
        mCreateTime = (TextView) findViewById(R.id.cover_date);
        setCreateTime();

        //搜索位置
        ILocationQueryTool queryTool = new ILocationQueryTool(this);
        queryTool.startRegeocodeQuery(ISharePreference.getLatLngData(CreateCoverActivity.this), radius);
        queryTool.setOnLocationQueryListener(new ILocationQueryTool.OnLocationQueryListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeAddress regeocodeAddress) {
                poiItemList = regeocodeAddress.getPois();
                //将列表的第一个名称设置为默认
                PoiItem defaultPoi = poiItemList.get(0);
                mLocationTextView.setText(defaultPoi.getTitle());
                ISharePreference.saveExtra(CreateCoverActivity.this, defaultPoi.getTitle());
                mStoryInfo.setLocation(defaultPoi.getTitle());
                mStoryInfo.setLatLng(defaultPoi.getLatLonPoint());
            }

            @Override
            public void onGeocodeSearched(List<GeocodeAddress> geocodeAddressList) {

            }
        });
    }

    /**
     * 获取故事的正文数据
     */
    private void initDate() {
        mStoryInfo = getIntent().getParcelableExtra(ParcelableUtil.STORY);
        mStoryInfo.setExtract(ISharePreference.getExtra(CreateCoverActivity.this));
        coverPicPath = mStoryInfo.getDetailPic();
    }

    /**
     * 标题栏
     */
    private void initTitle() {
        TitleView titleView = (TitleView) findViewById(R.id.title_view);
        titleView.setOnTitleClickListener(new TitleView.OnTitleClickListener() {
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

                StoryDateBaseHelper storyDateBaseHelper = new StoryDateBaseHelper(CreateCoverActivity.this);
                //更新故事数据
                mStoryInfo.setExtract(mExtraTextView.getText().toString());
                mStoryInfo.setCreateDate(ConvertTimeUtil.convert2TimeText(new Date(System.currentTimeMillis())));
                mStoryInfo.setDetailPic(coverPicPath);
                mStoryInfo.setTitle(mTitleTextView.getText().toString());
                mStoryInfo.setContent(ISharePreference.getContent(CreateCoverActivity.this));
                mStoryInfo.setUserInfo(ISharePreference.getUserData(CreateCoverActivity.this));

                //保存到本地数据库
                storyDateBaseHelper.insertStoryData(mStoryInfo);

                // TODO: 2016/12/27 发布故事到服务器
                mStoryInfo.setStoryId(storyDateBaseHelper.getLocalStory().size());


                //清除本地的小缓存，不是数据库
                ISharePreference.saveExtra(CreateCoverActivity.this, "");
                ISharePreference.saveContent(CreateCoverActivity.this, "");
                ISharePreference.saveCoverPic(CreateCoverActivity.this, "");
                ISharePreference.saveTitle(CreateCoverActivity.this, "");
                //
                checkStoryData();
            }
        });
    }

    /**
     * 检查故事的数据完整性
     */
    private void checkStoryData() {
        if (TextUtils.isEmpty(mStoryInfo.getTitle())) {
            ToastUtil.Show(CreateCoverActivity.this, R.string.input_title);
            return;
        }

        if (TextUtils.isEmpty(mStoryInfo.getExtract())) {
            ToastUtil.Show(CreateCoverActivity.this, R.string.input_extra);
            return;
        }

        if (TextUtils.isEmpty(coverPicPath)) {
            ToastUtil.Show(CreateCoverActivity.this, R.string.select_cover);
            return;
        }

        intentWithFlag(StoryMapActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

    }

    @Override
    public void onResult(String imagePath, int requestCode) {
        if (requestCode == IMAGE) {
            coverPicPath = imagePath;
            showImage(coverPicPath);
            ISharePreference.saveCoverPic(CreateCoverActivity.this, coverPicPath);
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
//        titleDialog.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                ISharePreference.saveTitle(CreateCoverActivity.this, mTitleTextView.getText().toString());
//            }
//        });

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
//        extraDialog.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                ISharePreference.saveExtra(CreateCoverActivity.this, mExtraTextView.getText().toString());
//            }
//        });

        extraDialog.setShowText(mExtraTextView.getText().toString());
    }

    /**
     * 选择当前的位置
     */
    private void chooseAoiLocation() {
        PoiDialogManger.getInstance().showAoiDialog(this, poiItemList).setOnPoiChooseListener(new PoiDialogManger.OnPoiChooseListener() {
            @Override
            public void onChoose(PoiItem choosePoi) {
                mLocationTextView.setText(choosePoi.getTitle());
                mStoryInfo.setLatLng(choosePoi.getLatLonPoint());
                mStoryInfo.setLocation(choosePoi.getTitle());

                Log.i(TAG, "onChoose: PoiItem:" + choosePoi);
                PoiDialogManger.getInstance().dismissAoiDialog();
            }
        });
    }

    /**
     * 设置当前的时间
     */
    private void setCreateTime() {
        Date createDate = new Date(System.currentTimeMillis());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");
        String str = formatter.format(createDate);
        mCreateTime.setText(str);
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

    @Override
    protected void onPause() {
        super.onPause();
        ILocationManager.getInstance().stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ILocationManager.getInstance().start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        ILocationManager.getInstance().stop();
    }
}
