package com.storyshu.storyshu.widget.dialog;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.Display;
import android.view.WindowManager;
import android.widget.TextView;

import com.storyshu.storyshu.R;
import com.storyshu.storyshu.adapter.PicturePreviewAdapter;
import com.storyshu.storyshu.utils.SysUtils;

import java.util.List;

/**
 * 图片预览的弹窗
 * Created by bear on 2017/3/30.
 */

public class PicturePreviewDialog extends IBaseDialog {
    private ViewPager viewPager; //显示图片
    private TextView textView; //显示故事的内容

    /**
     * 设置图片的数据显示
     */
    public void setStoryListShow(List<String> list) {
        show();
        PicturePreviewAdapter adapter = new PicturePreviewAdapter(list, getContext());
        adapter.setOnPictureClickListener(new PicturePreviewAdapter.OnPictureClickListener() {
            @Override
            public void onClick() {
                //点击之后关闭弹窗
                dismiss();
            }
        });
        viewPager.setAdapter(adapter);
    }

    public PicturePreviewDialog(Context context) {
        this(context, R.style.TransparentDialogTheme);
    }

    public PicturePreviewDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public PicturePreviewDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.story_pic_preview_layout;
    }

    @Override
    public void initView() {
        viewPager = (ViewPager) findViewById(R.id.picture_view_page);
        textView = (TextView) findViewById(R.id.content);
    }

    @Override
    public void Create() {
        //设置宽度和高度；全屏
        Display display = SysUtils.getScreenDisplay(getContext());
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = display.getWidth();
        params.height = display.getHeight();
        getWindow().setAttributes(params);
    }
}
