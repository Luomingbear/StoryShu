package com.storyshu.storyshu.widget.dialog;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.Display;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.storyshu.storyshu.R;
import com.storyshu.storyshu.adapter.PicturePreviewAdapter;
import com.storyshu.storyshu.utils.StatusBarUtils;
import com.storyshu.storyshu.utils.SysUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 图片预览的弹窗
 * Created by bear on 2017/3/30.
 */

public class PicturePreviewDialog extends IBaseDialog {
    private ViewPager viewPager; //显示图片
    private TextView textView; //显示故事的内容
    private RadioGroup pointLayout; //图片的位置的圆点显示的位置
    private List<RadioButton> pointList = new ArrayList<>();

    /**
     * 设置图片的数据显示
     */
    public void setStoryListShow(List<String> list) {
        if (list.size() == 0)
            return;

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

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                pointList.get(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //设置圆点
        for (int i = 0; i < list.size(); i++) {
            RadioButton point = newPoint();
            pointLayout.addView(point);
            pointList.add(point);
        }

        pointList.get(0).setChecked(true);
    }

    private RadioButton newPoint() {
        RadioButton radioButton = new RadioButton(getContext());
        int width = (int) getContext().getResources().getDimension(R.dimen.margin_min);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, width);
        params.setMargins(width * 2, 0, 0, 0);
        radioButton.setLayoutParams(params);

        radioButton.setButtonDrawable(null);
        radioButton.setBackgroundResource(R.drawable.point_bg);
        return radioButton;
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

        pointLayout = (RadioGroup) findViewById(R.id.point_layout);
    }

    @Override
    public void Create() {
        //设置宽度和高度；全屏
        Display display = SysUtils.getScreenDisplay(getContext());
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = display.getWidth();
        params.height = display.getHeight() - StatusBarUtils.getStatusBarHeight(getContext());
        getWindow().setAttributes(params);
    }
}
