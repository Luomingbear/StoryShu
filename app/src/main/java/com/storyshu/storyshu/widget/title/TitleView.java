package com.storyshu.storyshu.widget.title;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.storyshu.storyshu.R;
import com.storyshu.storyshu.tool.observable.EventObserver;

/**
 * 标题栏
 * Created by bear on 2016/12/2.
 */

public class TitleView extends RelativeLayout implements EventObserver {
    private static final String TAG = "TitleView";
    private int mTitleViewHeight; //控件的高度
    private int mIconWidth; // 图标的宽度
    private TextView mTitleTextView; //标题文本
    private int mTitleColor; // 标题栏字体颜色
    private float mTitleSize; // 标题栏字体大小
    private String mTitleString; //标题栏文本
    private RelativeLayout mRightButton; //右边的按钮
    private long oldClickTime; //上一次点击的时间

    private TitleMode mTitleMode; //按钮的模式

    public enum TitleMode {
        /**
         * 菜单-位置-筛选
         */
        MENU_POSITION_SIFT,

        /**
         * 返回-标题
         */
        BACK_TILE,

        /**
         * 返回-标题-继续
         */
        BACK_TILE_GO,

        /**
         * 返回-标题-截图
         */
        BACK_TILE_SHOT,


    }

    public TitleView(Context context) {
        this(context, null);
    }

    public TitleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TitleView);
        mTitleColor = typedArray.getColor(R.styleable.TitleView_title_color, Color.BLACK);
        mTitleSize = typedArray.getDimension(R.styleable.TitleView_title_size, getResources().getDimension(R.dimen.font_normal));
        mTitleString = typedArray.getString(R.styleable.TitleView_title_string);
        mTitleMode = TitleMode.values()[typedArray.getInt(R.styleable.TitleView_title_mode, 0)];
        typedArray.recycle();
        init();
        initTitle();
    }

    /**
     * 初始化信息
     */
    private void init() {

        mIconWidth = (int) getResources().getDimension(R.dimen.icon_small);
        mTitleViewHeight = (int) getResources().getDimension(R.dimen.title_height);
        setGravity(Gravity.CENTER_VERTICAL);
        //设置背景
        setBackgroundResource(R.color.colorWhite);

    }

    /**
     * 设置标题栏的样式，添加按钮等
     */
    private void initTitle() {
        //移除之前的所有的view
        removeAllViews();

        //动态的生成界面
        switch (mTitleMode) {

            case BACK_TILE:

                //left
                addView(newImageButton(RelativeLayout.ALIGN_PARENT_LEFT, R.drawable.back));
                //title
                addTitle();
                break;

            case BACK_TILE_GO:
                //left
                addView(newImageButton(RelativeLayout.ALIGN_PARENT_LEFT, R.drawable.back));
                //title
                addTitle();
                //go
                addView(mRightButton = newImageButton(RelativeLayout.ALIGN_PARENT_RIGHT, R.drawable.go));

                break;
            case MENU_POSITION_SIFT:

                //left
                addView(newImageButton(RelativeLayout.ALIGN_PARENT_LEFT, R.drawable.menu));
                //位置
                addPositionTitle();
                //right
                mRightButton = newImageButton(RelativeLayout.ALIGN_PARENT_RIGHT, R.drawable.sift);
                addView(mRightButton);
                break;

            case BACK_TILE_SHOT:
                //left
                addView(newImageButton(RelativeLayout.ALIGN_PARENT_LEFT, R.drawable.back));
                //title
                addTitle();
                //go
                addView(mRightButton = newImageButton(RelativeLayout.ALIGN_PARENT_RIGHT, R.drawable.screenshot2));
                break;
        }
    }

    /**
     * 添加左边按钮
     */
    private void addLeftButton() {
        addView(newImageButton(RelativeLayout.ALIGN_PARENT_LEFT, R.drawable.menu));
    }

    /**
     * 添加中间的标题
     */
    private void addPositionTitle() {
//        if (TextUtils.isEmpty(mTitleString))
//            return;

        LinearLayout titleLayout = new LinearLayout(getContext());
        LayoutParams p = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        p.addRule(CENTER_IN_PARENT);
        titleLayout.setLayoutParams(p);
        titleLayout.setOrientation(LinearLayout.HORIZONTAL);
        titleLayout.setGravity(CENTER_VERTICAL);

        /**
         * 添加位置图标
         */
//        ImageView locationIcon = newImageView(R.drawable.location, mTitleSize, mTitleSize);
        ImageView imageView = new ImageView(getContext());
        imageView.setBackgroundResource(R.drawable.location);
        LayoutParams pl = new LayoutParams((int) getResources().getDimension(R.dimen.icon_min),
                (int) getResources().getDimension(R.dimen.icon_min));
        imageView.setLayoutParams(pl);

//        locationIcon.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        titleLayout.addView(imageView);

        titleLayout.addView(newTitle());

        addView(titleLayout);
    }

    /**
     * 添加文本标题
     */
    private void addTitle() {
        mTitleTextView = newTitle();
        LayoutParams p = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        p.addRule(CENTER_IN_PARENT);
        mTitleTextView.setLayoutParams(p);
        addView(mTitleTextView);
    }

    /**
     * 文本标题
     *
     * @return
     */
    private TextView newTitle() {
        /**
         * 添加文本描述
         */
        //生成一个textView并且设置layoutParams
        mTitleTextView = new TextView(getContext());
        LayoutParams pa = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mTitleTextView.setLayoutParams(pa);

        //设置单行，句尾省略
        mTitleTextView.setEllipsize(TextUtils.TruncateAt.END);
        mTitleTextView.setSingleLine();

        mTitleTextView.setText(mTitleString);
        mTitleTextView.setTextColor(mTitleColor);
        mTitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTitleSize);

        //设置点击响应
        mTitleTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                long clickTime = System.currentTimeMillis();
                float spendTime = clickTime - oldClickTime;
                oldClickTime = clickTime;
                if (onTitleClickListener != null) {
                    /**
                     * 如果时间间隔小于500毫秒则是双击，否则是单击
                     */
                    if (spendTime < 500)
                        onTitleClickListener.onCenterDoubleClick();
                    else
                        onTitleClickListener.onCenterClick();
                }
            }

        });
        return mTitleTextView;
    }

    /**
     * 添加右边的按钮
     */
    private void addRightButton() {
        mRightButton = newImageButton(RelativeLayout.ALIGN_PARENT_RIGHT, R.drawable.sift);
        addView(mRightButton);
    }

    /**
     * 添加图标的饿按钮
     *
     * @param gravity 位置
     * @param resId   图标资源id
     */
    private RelativeLayout newImageButton(final int gravity, int resId) {
        RelativeLayout relativeLayout = new RelativeLayout(getContext());
        LayoutParams p = new LayoutParams(mTitleViewHeight, mTitleViewHeight);
        p.addRule(gravity);
        relativeLayout.setLayoutParams(p);
        relativeLayout.setGravity(Gravity.CENTER);

        relativeLayout.addView(newImageView(resId));
        relativeLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onTitleClickListener != null)
                    switch (gravity) {
                        case RelativeLayout.ALIGN_PARENT_LEFT:
                            onTitleClickListener.onLeftClick();
                            break;
                        case RelativeLayout.ALIGN_PARENT_RIGHT:
                            onTitleClickListener.onRightClick();
                            break;
                    }
            }
        });
        return relativeLayout;
    }

    private ImageView newImageView(int resId) {
        ImageView imageView = new ImageView(getContext());
        imageView.setBackgroundResource(resId);

        LayoutParams p = new LayoutParams(mIconWidth, mIconWidth);
        imageView.setLayoutParams(p);
        return imageView;
    }

    private ImageView newImageView(int resId, float width, float height) {
        ImageView imageView = new ImageView(getContext());
        imageView.setBackgroundResource(resId);
        LayoutParams p = new LayoutParams((int) width, (int) height);
        imageView.setLayoutParams(p);
        return imageView;
    }


    public void setTitleString(String titleString) {
        if (TextUtils.isEmpty(titleString))
            return;
        this.mTitleString = titleString;
        mTitleTextView.setText(mTitleString);
    }

    public void setTitleSize(float mTitleSize) {
        this.mTitleSize = mTitleSize;
        mTitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTitleSize);
    }

    public void setTitleCoclor(int mTitleCoclor) {
        this.mTitleColor = mTitleCoclor;
        mTitleTextView.setTextColor(mTitleCoclor);
    }

    private onTitleClickListener onTitleClickListener;

    public void setOnTitleClickListener(TitleView.onTitleClickListener onTitleClickListener) {
        this.onTitleClickListener = onTitleClickListener;
    }

    public TextView getTitleTextView() {
        return mTitleTextView;
    }

    public RelativeLayout getRightButton() {
        return mRightButton;
    }

    /**
     * 通知观察者，位置获取成功
     *
     * @param sender
     * @param eventId
     * @param args
     */
    @Override
    public void onNotify(Object sender, int eventId, Object... args) {
        Log.e(TAG, "onNotify: " + args[0].toString());
        if (eventId == R.id.title_view) {
            String title = args[0].toString();
            if (TextUtils.isEmpty(title))
                title = getResources().getString(R.string.in_location);

            if (!mTitleString.equals(title))
                mTitleString = title;
            setTitleString(mTitleString);
        }
    }

    /**
     * 标题栏的点击事件
     */
    public interface onTitleClickListener {
        //左边的按钮被点击
        void onLeftClick();

        //标题栏的按钮被点击
        void onCenterClick();

        //标题栏被双击
        void onCenterDoubleClick();

        //右边的按钮被点击
        void onRightClick();
    }
}
