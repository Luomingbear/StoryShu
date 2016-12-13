package com.bear.passby.widget.sift;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.bear.passby.R;
import com.bear.passby.utils.ToastUtil;
import com.bear.passby.widget.selector.SelectorGroup;

import java.util.List;

/**
 * 筛选弹窗
 * Created by bear on 2016/12/3.
 */

public class SiftWindow extends PopupWindow implements SelectorGroup.OnSelectorCheckedListener {
    private static final String TAG = "SiftWindow";
    private Context mContext;

    public SiftWindow(Context context) {
        this(context, null);
    }

    public SiftWindow(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SiftWindow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.sift_window_layout, null);
        setContentView(view);

        //是否可以点击
        setTouchable(true);
        //点击外部消失
        setOutsideTouchable(true);
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        setBackgroundDrawable(new BitmapDrawable());
        float height = context.getResources().getDimension(R.dimen.selector_height);

        setWidth((int) context.getResources().getDimension(R.dimen.sift_window_width));
        setHeight((int) (height * 3));

        /**
         * 单选
         *
         */
        SelectorGroup selectorGroup = (SelectorGroup) view.findViewById(R.id.sift_group);
        selectorGroup.setOnSelectorCheckedListener(this);

    }

    @Override
    public void onChecked(List<Integer> checkedIdList) {
        if (checkedIdList.size() < 1)
            return;
        // TODO: 2016/12/3 选择之后的操作
        switch (checkedIdList.get(0)) {
            case R.id.sift_all:
                ToastUtil.Show(mContext, R.string.sift_all);
                break;
            case R.id.sift_hot:
                ToastUtil.Show(mContext, R.string.sift_hot);
                break;
            case R.id.sift_follow:
                ToastUtil.Show(mContext, R.string.sift_follow);
                break;

        }
        dismiss();
    }
}
