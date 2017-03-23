package com.storyshu.storyshu.imagepicker.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.storyshu.storyshu.R;
import com.storyshu.storyshu.imagepicker.CapturePhotoHelper;
import com.storyshu.storyshu.imagepicker.FileChooseInterceptor;
import com.storyshu.storyshu.imagepicker.PhotoLoadListener;
import com.storyshu.storyshu.imagepicker.PickerAction;
import com.storyshu.storyshu.imagepicker.PickerInfo;
import com.storyshu.storyshu.imagepicker.SImagePicker;
import com.storyshu.storyshu.imagepicker.adapter.PhotoAdapter;
import com.storyshu.storyshu.imagepicker.control.AlbumController;
import com.storyshu.storyshu.imagepicker.control.PhotoController;
import com.storyshu.storyshu.imagepicker.model.Album;
import com.storyshu.storyshu.imagepicker.model.Photo;
import com.storyshu.storyshu.imagepicker.util.CollectionUtils;
import com.storyshu.storyshu.imagepicker.widget.GridInsetDecoration;
import com.storyshu.storyshu.imagepicker.widget.SquareRelativeLayout;

import java.io.File;
import java.util.ArrayList;


/**
 * Created by Martin on 2017/1/17.
 */
public class PhotoPickerActivity extends BasePickerActivity implements PickerAction, View.OnClickListener {

    public static final String EXTRA_RESULT_SELECTION = "EXTRA_RESULT_SELECTION";
    public static final String EXTRA_RESULT_ORIGINAL = "EXTRA_RESULT_ORIGINAL";

    public static final String PARAM_MODE = "PARAM_MODE";
    public static final String PARAM_MAX_COUNT = "PARAM_MAX_COUNT";
    public static final String PARAM_SELECTED = "PARAM_SELECTED";
    public static final String PARAM_ROW_COUNT = "PARAM_ROW_COUNT";
    public static final String PARAM_SHOW_CAMERA = "PARAM_SHOW_CAMERA";
    public static final String PARAM_CUSTOM_PICK_TEXT_RES = "PARAM_CUSTOM_PICK_TEXT_RES";
    public static final String PARAM_FILE_CHOOSE_INTERCEPTOR = "PARAM_FILE_CHOOSE_INTERCEPTOR";

    public static final int REQUEST_CODE_PICKER_PREVIEW = 100;
    public static final int REQUEST_CODE_CROP_IMAGE = 101;

    private RelativeLayout titleLayout; //标题栏
    private View cancel; //取消
    private TextView titleText; //标题内容
    private View ok; //确定
    RecyclerView recyclerView;
//    Toolbar toolbar;

    private GridLayoutManager layoutManager;
    private int maxCount;
    private int mode;
    private int rowCount;
    private boolean showCamera = false;
    private String avatarFilePath;
    private
    @StringRes
    int pickRes;
    private
    @StringRes
    int pickNumRes;
    private FileChooseInterceptor fileChooseInterceptor;
    private CapturePhotoHelper capturePhotoHelper;


    private final PhotoController photoController = new PhotoController();
    /**
     * 相册管理；
     */
    private final AlbumController albumController = new AlbumController();
    private final AlbumController.OnDirectorySelectListener directorySelectListener =
            new AlbumController.OnDirectorySelectListener() {
                @Override
                public void onSelect(Album album) {
                    photoController.resetLoad(album);
                }

                @Override
                public void onReset(Album album) {
                    photoController.load(album);
                }
            };

    private final PhotoAdapter.OnPhotoActionListener selectionChangeListener =
            new PhotoAdapter.OnPhotoActionListener() {

                @Override
                public void onSelect(String filePath) {
                    updateTopBar();
                }

                @Override
                public void onDeselect(String filePath) {
                    updateTopBar();
                    refreshCheckbox();
                }

                @Override
                public void onPreview(final int position, Photo photo, final View view) {
                    if (mode == PickerInfo.MODE_IMAGE) {
                        photoController.getAllPhoto(new PhotoLoadListener() {
                            @Override
                            public void onLoadComplete(ArrayList<Uri> photoUris) {
                                if (!CollectionUtils.isEmpty(photoUris)) {
                                    PickerPreviewActivity.startPicturePreviewFromPicker(PhotoPickerActivity.this,
                                            photoUris, photoController.getSelectedPhoto(), position, false
                                            , maxCount, rowCount,
                                            fileChooseInterceptor,
                                            pickRes, pickNumRes,
                                            PickerPreviewActivity.AnchorInfo.newInstance(view),
                                            REQUEST_CODE_PICKER_PREVIEW);
                                }
                            }

                            @Override
                            public void onLoadError() {

                            }
                        });
                    } else if (mode == PickerInfo.MODE_AVATAR) {
                        CropImageActivity.startImageCrop(PhotoPickerActivity.this, photo.getFilePath(),
                                REQUEST_CODE_CROP_IMAGE, avatarFilePath);
                    }
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PickerInfo pickerInfo = getIntent().getParcelableExtra(SImagePicker.SImagePicker);
        mode = pickerInfo.getPickMode();
        maxCount = pickerInfo.getMaxCount();
        avatarFilePath = pickerInfo.getAvatarFilePath();
        rowCount = pickerInfo.getRowCount();
        showCamera = pickerInfo.isShowCamera();
        initUI();
    }

    private void initUI() {
        //标题
        titleLayout = (RelativeLayout) findViewById(R.id.title_layout);
        cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(this);
        titleText = (TextView) findViewById(R.id.title_text);
        titleText.setOnClickListener(this);
        ok = findViewById(R.id.ok);
        ok.setOnClickListener(this);

        //图片加载
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        layoutManager = new GridLayoutManager(this, rowCount);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new GridInsetDecoration());
        if (!showCamera) {
            photoController.onCreate(this, recyclerView, selectionChangeListener, maxCount, rowCount,
                    mode);
        } else {
            capturePhotoHelper = new CapturePhotoHelper(this);
            photoController.onCreate(this, recyclerView, selectionChangeListener, maxCount, rowCount,
                    mode, capturePhotoHelper);
        }
        photoController.loadAllPhoto(this);

        fileChooseInterceptor = getIntent().getParcelableExtra(PARAM_FILE_CHOOSE_INTERCEPTOR);
        ArrayList<String> selected = getIntent().getStringArrayListExtra(PARAM_SELECTED);
        if (!CollectionUtils.isEmpty(selected)) {
            photoController.setSelectedPhoto(selected);
        }
        pickRes = getIntent().getIntExtra(PARAM_CUSTOM_PICK_TEXT_RES, 0);
        updateTopBar();

        //下拉列表显示相册
//        albumController.onCreate(this, albumSpinner, directorySelectListener);
//        albumController.loadAlbums();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_photo_picker;
    }


    @Override
    protected void onDestroy() {
        albumController.onDestroy();
        photoController.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        setResultAndFinish(photoController.getSelectedPhoto(), false, Activity.RESULT_CANCELED);
    }

    /**
     * 完成
     */
    private void commit() {
        if (!photoController.getSelectedPhoto().isEmpty()) {
            setResultAndFinish(photoController.getSelectedPhoto(), false, Activity.RESULT_OK);
        }
    }

    private void setResultAndFinish(ArrayList<String> selected, boolean original, int resultCode) {
        if (fileChooseInterceptor != null
                && !fileChooseInterceptor.onFileChosen(this, selected, original, resultCode, this)) {
            // Prevent finish if interceptor returns false.
            return;
        }
        proceedResultAndFinish(selected, original, resultCode);
    }

    @Override
    public void proceedResultAndFinish(ArrayList<String> selected, boolean original, int resultCode) {
        Intent intent = new Intent();
        intent.putStringArrayListExtra(EXTRA_RESULT_SELECTION, selected);
        intent.putExtra(EXTRA_RESULT_ORIGINAL, original);
        setResult(resultCode, intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PICKER_PREVIEW) {
            if (data != null) {
                ArrayList<String> selected =
                        data.getStringArrayListExtra(PickerPreviewActivity.KEY_SELECTED);
                boolean selectOriginal =
                        data.getBooleanExtra(PickerPreviewActivity.KEY_SELECTED_ORIGINAL, false);
                if (resultCode == Activity.RESULT_CANCELED) {
                    photoController.setSelectedPhoto(selected);
                    updateTopBar();
                } else if (resultCode == Activity.RESULT_OK) {
                    setResultAndFinish(selected, selectOriginal, Activity.RESULT_OK);
                }
            }
        } else if (requestCode == REQUEST_CODE_CROP_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                String path = data.getStringExtra(CropImageActivity.RESULT_PATH);
                ArrayList<String> result = new ArrayList<>();
                result.add(path);
                setResultAndFinish(result, true, Activity.RESULT_OK);
            }
        } else if (requestCode == CapturePhotoHelper.CAPTURE_PHOTO_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_CANCELED) {
                if (capturePhotoHelper.getPhoto() != null && capturePhotoHelper.getPhoto().exists()) {
                    capturePhotoHelper.getPhoto().delete();
                }
            } else if (resultCode == Activity.RESULT_OK) {
                if (mode == PickerInfo.MODE_AVATAR) {
                    File photoFile = capturePhotoHelper.getPhoto();
                    if (photoFile != null) {
                        CropImageActivity.startImageCrop(this, photoFile.getAbsolutePath(),
                                REQUEST_CODE_CROP_IMAGE,
                                avatarFilePath);
                    }
                } else {
                    File photoFile = capturePhotoHelper.getPhoto();
                    ArrayList<String> result = new ArrayList<>();
                    result.add(photoFile.getAbsolutePath());
                    setResultAndFinish(result, true, Activity.RESULT_OK);
                }
            }
        }
    }

    /**
     * 更新顶部的标题栏
     */
    private void updateTopBar() {

    }

    private void refreshCheckbox() {
        int firstVisible = layoutManager.findFirstVisibleItemPosition();
        int lastVisible = layoutManager.findLastVisibleItemPosition();
        for (int i = firstVisible; i <= lastVisible; i++) {
            View view = layoutManager.findViewByPosition(i);
            if (view instanceof SquareRelativeLayout) {
                SquareRelativeLayout item = (SquareRelativeLayout) view;
                if (item != null) {
                    String photoPath = (String) item.getTag();
                    if (photoController.getSelectedPhoto().contains(photoPath)) {
                        item.checkBox.setText(String.valueOf(photoController.getSelectedPhoto()
                                .indexOf(photoPath) + 1));
                        item.checkBox.refresh(false);
                    }
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //取消
            case R.id.cancel:
                onBackPressed();
                break;

            //选择文件夹
            case R.id.title_text:
                albumController.showAlbum();
                break;

            case R.id.ok:
                commit();
                break;
        }
    }
}
