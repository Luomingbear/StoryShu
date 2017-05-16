package com.storyshu.storyshu.widget.dialog;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.PopupWindow;

import com.amap.api.services.core.PoiItem;
import com.storyshu.storyshu.R;
import com.storyshu.storyshu.widget.LocationSelector;
import com.storyshu.storyshu.widget.inputview.InputDialog;
import com.storyshu.storyshu.widget.tag.TagFrameLayout;
import com.storyshu.storyshu.widget.tag.TagView;

import java.util.List;

/**
 * 发布故事的时候的弹窗
 * Created by bear on 2017/2/22.
 */

public class SendStoryDialog extends PopupWindow {
    private List<PoiItem> mPoiList; //数据源
    private LocationSelector mLocationSelector; //位置选择器
    private TagFrameLayout mTagFrameLayout; //标签控件
    private Context mContext;

    public SendStoryDialog(Context context) {
        this(context, null);
    }

    public SendStoryDialog(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SendStoryDialog(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, R.style.MenuDialogTheme);

        mContext = context;
    }

    public void init(List<PoiItem> poiList, Window window) {
        mPoiList = poiList;

        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.send_story_layout, null);
        setContentView(view);

        //是否可以点击
        setTouchable(true);
        //点击外部消失
        setOutsideTouchable(false);
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        setBackgroundDrawable(new BitmapDrawable());

        //设置背景颜色
//        WindowManager.LayoutParams lp = window.getAttributes();
//        lp.dimAmount = 0.5f; //0.0-1.0
//        window.setAttributes(lp);

        //设置宽度和高度；
        Display display = window.getWindowManager().getDefaultDisplay();
        setHeight(display.getWidth());

        //
        mLocationSelector = (LocationSelector) view.findViewById(R.id.select_location);
        mTagFrameLayout = (TagFrameLayout) view.findViewById(R.id.tag_layout); //标签控件
        mTagFrameLayout.setOnTagLayoutClickListener(onTagLayoutClickListener);

        mLocationSelector.setLocationList(mPoiList);
    }


    String tag = "";
    private TagFrameLayout.OnTagLayoutClickListener onTagLayoutClickListener = new TagFrameLayout.OnTagLayoutClickListener() {
        @Override
        public void onAddTagClick() {
            final InputDialog inputDialog = new InputDialog(mContext);

            inputDialog.init(new InputDialog.OnInputChangeListener() {
                @Override
                public void onTextChange(CharSequence s, int start, int before, int count) {
                    tag = s.toString();
                }

                @Override
                public void onSendClick(String content) {
                    inputDialog.hideKeyboard();
                    mTagFrameLayout.addTag(tag);
                }
            });

        }

        @Override
        public void onEditTagClick(TagView tagView) {

        }
    };
}
