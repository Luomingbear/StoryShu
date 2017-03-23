package com.storyshu.storyshu.imagepicker.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.storyshu.storyshu.R;


/**
 * Created by Martin on 2017/1/17.
 */
public class PickerBottomLayout extends RelativeLayout {


    public android.widget.CheckBox originalCheckbox; //高清选择

    public CheckBox selectBox; //选中图片

    public TextView originalSize;

    public View originalContainer;

    private TextView select_text; //"选择"的文本

    private View select_btn; //选择图片的按钮
    private OnBottomListener onBottomListener;

    public void setOnBottomListener(OnBottomListener onBottomListener) {
        this.onBottomListener = onBottomListener;
    }

    public interface OnBottomListener {
        /**
         * 选择图片的按钮被点击
         *
         * @param isChecked checkbox点击之前是否是选中的
         */
        void onSelectClicked(boolean isChecked);
    }

    public PickerBottomLayout(Context context) {
        this(context, null);
    }

    public PickerBottomLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PickerBottomLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.picker_bottom_layout, this);

        select_btn = findViewById(R.id.select_button);
        select_btn.setOnClickListener(selectClickListener);
        selectBox = (CheckBox) findViewById(R.id.select_img);
        select_text = (TextView) findViewById(R.id.select_pic_text);

        originalSize = (TextView) findViewById(R.id.original_size);
        originalContainer = findViewById(R.id.original_container);
        originalCheckbox = (android.widget.CheckBox) findViewById(R.id.original_checkbox);
        originalCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                originalSize.setTextColor(isChecked
                        ? getResources().getColor(R.color.colorGrayLight)
                        : getResources().getColor(R.color.colorGray));
            }
        });
    }

    /**
     * 选择的点击事件
     */
    private OnClickListener selectClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (onBottomListener != null)
                onBottomListener.onSelectClicked(selectBox.isChecked());
        }
    };

    public void updateSelectedCount(int count) {
        if (count == 0) {
            originalContainer.setVisibility(View.GONE);
        } else {
            selectBox.setText(count + "");
            originalContainer.setVisibility(View.VISIBLE);
        }
    }

    public void updateSelectedSize(String size) {
        if (TextUtils.isEmpty(size)) {
            originalContainer.setVisibility(View.GONE);
            originalCheckbox.setChecked(false);
        } else {
            originalContainer.setVisibility(View.VISIBLE);
            originalSize.setText(getResources().getString(R.string.general_original) + " "
                    + getResources().getString(R.string.bracket_str, size));
        }
    }

    public void hide() {
        animate().translationY(getHeight())
                .setInterpolator(new AccelerateInterpolator(2));
    }

    public void show() {
        animate().translationY(0)
                .setInterpolator(new AccelerateInterpolator(2));
    }
}
