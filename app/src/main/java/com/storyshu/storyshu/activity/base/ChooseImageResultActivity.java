package com.storyshu.storyshu.activity.base;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * 调用系统相册获取图片的activity
 * Created by bear on 2016/12/29.
 */

public abstract class ChooseImageResultActivity extends IBaseActivity {

    public static final int IMAGE = 1;
    public static final int COVER = IMAGE + 1;
    public static final int CAMERA = COVER + 1;
    private String imagePath;

    /**
     * 调用系统相册
     */
    public void chooseImage() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, IMAGE);
    }

    /**
     * 调用系统相册
     */
    public void chooseCover() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, COVER);
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
        if (requestCode == IMAGE || requestCode == COVER && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            imagePath = c.getString(columnIndex);
            c.close();
            onResult(imagePath, requestCode);
        }
    }

    /**
     * 获取图片成功后执行
     */
    public abstract void onResult(String imagePath, int requestCode);

}
