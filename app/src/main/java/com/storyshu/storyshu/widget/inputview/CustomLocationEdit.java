package com.storyshu.storyshu.widget.inputview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.storyshu.storyshu.R;
import com.storyshu.storyshu.widget.text.IBaseEditText;

/**
 * 输入自定义的位置的控件
 * Created by bear on 2016/12/27.
 */

public class CustomLocationEdit extends LinearLayout {
    private IBaseEditText customLocationName;

    public CustomLocationEdit(Context context) {
        this(context, null);
    }

    public CustomLocationEdit(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomLocationEdit(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);

        addEditText();
        addOKButton();
    }

    private void addEditText() {
        customLocationName = new IBaseEditText(getContext());
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                (int) getResources().getDimension(R.dimen.title_height), 1);
        p.setMargins((int) getResources().getDimension(R.dimen.margin_normal), 0, 0, 0);
        customLocationName.setLayoutParams(p);

        customLocationName.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_normal));
        customLocationName.setTextColor(getResources().getColor(R.color.colorBlack));
        customLocationName.setHint(R.string.custom_location);
        addView(customLocationName);
    }

    private void addOKButton() {
        TextView okButton = new TextView(getContext());
        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams((int) getResources().getDimension(R.dimen.title_height),
                (int) getResources().getDimension(R.dimen.title_height));
        okButton.setLayoutParams(p);

        okButton.setGravity(Gravity.CENTER);
        okButton.setTextColor(getResources().getColor(R.color.colorWhite));
        okButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_normal));
        okButton.setText(R.string.custom_location_ok);
        okButton.setBackgroundColor(getResources().getColor(R.color.colorRedDeep));
        addView(okButton);

        okButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onCustomLocationEditListener != null)
                    onCustomLocationEditListener.OnCustomLocation(customLocationName.getText().toString());
            }
        });
    }

    private OnCustomLocationEditListener onCustomLocationEditListener;

    public void setOnCustomLocationEditListener(OnCustomLocationEditListener onCustomLocationEditListener) {
        this.onCustomLocationEditListener = onCustomLocationEditListener;
    }

    public interface OnCustomLocationEditListener {
        void OnCustomLocation(String locationName);
    }
}
