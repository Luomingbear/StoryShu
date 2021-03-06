package com.storyshu.storyshu.widget.title;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.storyshu.storyshu.R;
import com.storyshu.storyshu.tool.observable.EventObserver;
import com.storyshu.storyshu.utils.DipPxConversion;

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
    private RelativeLayout mLeftLayout; //左边的按钮
    private LinearLayout mTitleLayout; //标题 布局
    private RelativeLayout mRightLayout; //右边的按钮
    private int mButtonTextColor; //按钮的字体颜色

    private long oldClickTime; //上一次点击的时间

    private TitleMode mTitleMode; //按钮的模式

    public enum TitleMode {

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

        /**
         * 返回-标题-发布
         */
        BACK_TILE_SEND,

        /**
         * 中间是位置标题
         */
        POSITION_TITLE,

        /**
         * 消息-粉丝
         */
        MESSAGE_FRIEND,


        /**
         * 返回-标题-即时讨论
         */
        BACK_TILE_IM,


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
        mTitleColor = typedArray.getColor(R.styleable.TitleView_title_color, getResources().getColor(R.color.colorBlack));
        mTitleSize = typedArray.getDimension(R.styleable.TitleView_title_size, getResources().getDimension(R.dimen.font_big));
        mTitleString = typedArray.getString(R.styleable.TitleView_title_string);
        mButtonTextColor = typedArray.getColor(R.styleable.TitleView_button_color,
                getResources().getColor(R.color.colorWhite));
        mTitleMode = TitleMode.values()[typedArray.getInt(R.styleable.TitleView_title_mode, 0)];
        typedArray.recycle();

        init();
        initTitle();
    }

    /**
     * 初始化信息
     */
    private void init() {

        mIconWidth = (int) getResources().getDimension(R.dimen.font_normal);
        mTitleViewHeight = (int) getResources().getDimension(R.dimen.title_height);
        setGravity(Gravity.CENTER_VERTICAL);
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
                addLeftButton(R.drawable.back);
                //title
                addTitle();
                //line
//                addBottomLine();
                break;

            case BACK_TILE_GO:
                //left
                addLeftButton(R.drawable.back);
                //go
                addRightButton(R.drawable.go);
                //title
                addTitle();
                //line
//                addBottomLine();
                break;

            case BACK_TILE_SHOT:
                //left
                addBackButton();
                //screenShot
                addRightButton(R.drawable.search);
                //title
                addTitle();
                break;

            case BACK_TILE_SEND:
                //left
                addLeftButton(R.drawable.back);
                addRightText(R.string.issue);
                //title
                addTitle();
                break;

            case POSITION_TITLE:
                addPositionTitle();
                break;

            case MESSAGE_FRIEND:
                addTitle();
                addRightText(R.string.friend);
                break;

            case BACK_TILE_IM:
                //left
                addLeftButton(R.drawable.back);
                addRightText(R.string.discuss);
                //title
                addTitle();
                break;

        }
    }

    /**
     * 添加左边按钮
     */
    private void addLeftButton(int resId) {
        mLeftLayout = newImageButton(RelativeLayout.ALIGN_PARENT_LEFT, resId);
        addView(mLeftLayout);
        mLeftLayout.setId(mLeftLayout.hashCode());
    }

    /**
     * 添加返回键
     */
    private void addBackButton() {
        mLeftLayout = new RelativeLayout(getContext());
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        mLeftLayout.setLayoutParams(params);

        //
        LinearLayout linearLayout = new LinearLayout(getContext());
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        lp.setMargins((int) (getResources().getDimension(R.dimen.margin_small)), 0, 0, 0);
        linearLayout.setLayoutParams(lp);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setGravity(Gravity.CENTER_VERTICAL);
        mLeftLayout.addView(linearLayout);

        //
        ImageView imageView = newImageView(R.drawable.back);
        LayoutParams p = new LayoutParams(mIconWidth, mIconWidth);
        imageView.setLayoutParams(p);
        linearLayout.addView(imageView);

        //
        TextView textView = new TextView(getContext());
        LayoutParams tp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(tp);
        textView.setText(R.string.back);
        textView.setTextColor(mButtonTextColor);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTitleSize);
        linearLayout.addView(textView);

        //add
        addView(mLeftLayout);
        mLeftLayout.setId(mLeftLayout.hashCode());

        //listener
        mLeftLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onTitleClickListener != null)
                    onTitleClickListener.onLeftClick();
            }
        });
    }

    /**
     * 添加中间的标题
     */
    private void addPositionTitle() {

        mTitleLayout = new LinearLayout(getContext());
        LayoutParams p = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        p.addRule(RelativeLayout.CENTER_IN_PARENT);

        mTitleLayout.setLayoutParams(p);
        mTitleLayout.setOrientation(LinearLayout.HORIZONTAL);
        mTitleLayout.setGravity(Gravity.CENTER);

        /**
         * 添加位置图标
         */
        ImageView imageView = new ImageView(getContext());
        imageView.setBackgroundResource(R.drawable.location);
        LayoutParams pl = new LayoutParams(mIconWidth, mIconWidth);
        imageView.setLayoutParams(pl);

        mTitleLayout.addView(imageView);

        mTitleLayout.addView(newTitle());

        addView(mTitleLayout);
    }

    /**
     * 添加文本标题
     */
    private void addTitle() {
        mTitleLayout = new LinearLayout(getContext());
        LayoutParams p = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        p.addRule(CENTER_IN_PARENT);
        mTitleLayout.setLayoutParams(p);
        mTitleLayout.setOrientation(LinearLayout.HORIZONTAL);
        mTitleLayout.setGravity(CENTER_VERTICAL);
        addView(mTitleLayout);

        mTitleTextView = newTitle();
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        p.addRule(CENTER_IN_PARENT);
        mTitleTextView.setLayoutParams(params);

        mTitleLayout.addView(mTitleTextView);
    }

    /**
     * 生成文本控件
     *
     * @param textColor
     * @param stringRes
     * @return
     */
    private TextView newTextView(int textColor, int stringRes) {
        TextView textView = new TextView(getContext());
        LayoutParams pa = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(pa);

        textView.setGravity(Gravity.CENTER);
        textView.setText(stringRes);
        textView.setTextColor(textColor);
        return textView;
    }

    private TextView newTextView(int textColor, String string) {
        TextView textView = new TextView(getContext());
        LayoutParams pa = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(pa);

        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(textColor);
        textView.setText(string);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTitleSize);
        return textView;
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
        mTitleTextView = newTextView(mTitleColor, mTitleString);
        mTitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTitleSize);
        //设置单行，句尾省略
        mTitleTextView.setEllipsize(TextUtils.TruncateAt.START);
        mTitleTextView.setSingleLine();


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
     * 右边的文字按钮
     *
     * @param stringRes
     */
    private void addRightText(int stringRes) {
        mRightLayout = new RelativeLayout(getContext());
        LayoutParams p = new LayoutParams(LayoutParams.WRAP_CONTENT, mTitleViewHeight);
        p.setMargins((int) (getResources().getDimension(R.dimen.margin_normal)), 0,
                (int) (getResources().getDimension(R.dimen.margin_normal)), 0);
        p.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        mRightLayout.setLayoutParams(p);
        mRightLayout.setGravity(Gravity.CENTER);

        TextView textView = newTextView(mButtonTextColor, stringRes);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mIconWidth);
        mRightLayout.addView(textView);
        addView(mRightLayout);

        mRightLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onTitleClickListener != null)
                    onTitleClickListener.onRightClick();
            }
        });
    }

    /**
     * 添加右边的按钮
     */
    private void addRightButton(int resId) {
        mRightLayout = newImageButton(RelativeLayout.ALIGN_PARENT_RIGHT, resId);
        mRightLayout.setId(mRightLayout.hashCode());
        addView(mRightLayout);
    }

    /**
     * 添加图标的饿按钮
     *
     * @param gravity 位置
     * @param resId   图标资源id
     */
    private RelativeLayout newImageButton(final int gravity, int resId) {
        RelativeLayout relativeLayout = new RelativeLayout(getContext());
        LayoutParams p = new LayoutParams((int) (mIconWidth + (2 * getResources().getDimension(R.dimen.margin_normal))), mTitleViewHeight);

        p.addRule(gravity);
        relativeLayout.setLayoutParams(p);
        relativeLayout.setPadding((int) (getResources().getDimension(R.dimen.margin_normal)), 0,
                (int) (getResources().getDimension(R.dimen.margin_small)), 0);
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

    /**
     * 添加图片的按钮
     */
    private void addInsertImageButton() {
        RelativeLayout imageLayout = new RelativeLayout(getContext());
        LayoutParams p = new LayoutParams(mTitleViewHeight, mTitleViewHeight);
        p.addRule(RelativeLayout.LEFT_OF, mRightLayout.getId());
        imageLayout.setLayoutParams(p);
        imageLayout.setGravity(Gravity.CENTER);

//        imageLayout.addView(newImageView(R.drawable.cover));

        imageLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onTitleInserImageListener != null)
                    onTitleInserImageListener.onInsertImageClick();
            }
        });
        addView(imageLayout);
    }

    /**
     * 添加底部的分界线
     */
    private void addBottomLine() {
        View lineView = new View(getContext());
        LayoutParams p = new LayoutParams(LayoutParams.MATCH_PARENT, DipPxConversion.dip2px(getContext(), 1));
        p.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lineView.setLayoutParams(p);
//        lineView.setBackgroundResource(R.drawable.shadow_bg);
        lineView.setBackgroundColor(getResources().getColor(R.color.colorGray));
        addView(lineView);
    }

    private ImageView newImageView(int resId) {
        return newImageView(resId, mIconWidth, mIconWidth);
    }

    private ImageView newImageView(int resId, float width, float height) {
        ImageView imageView = new ImageView(getContext());
        imageView.setBackgroundResource(resId);
        LayoutParams p = new LayoutParams((int) width, (int) height);
        imageView.setLayoutParams(p);
        return imageView;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        LayoutParams p = (LayoutParams) mTitleLayout.getLayoutParams();
        int marginLeft = mLeftLayout == null ? 0 : mLeftLayout.getWidth();
        int marginRight = mRightLayout == null ? 0 : mRightLayout.getWidth();
        p.setMargins(marginLeft, 0, marginRight, 0);
        mTitleLayout.setLayoutParams(p);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
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

    private OnTitleClickListener onTitleClickListener;

    public void setOnTitleClickListener(OnTitleClickListener onTitleClickListener) {
        this.onTitleClickListener = onTitleClickListener;
    }

    public TextView getTitleTextView() {
        return mTitleTextView;
    }

    public RelativeLayout getRightButton() {
        return mRightLayout;
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
        if (eventId == R.id.title_view) {
            String title = args[0].toString();
            if (TextUtils.isEmpty(title))
                title = getResources().getString(R.string.app_name);

            if (!title.equals(mTitleString))
                mTitleString = title;
            setTitleString(mTitleString);
        }
    }

    /**
     * 标题栏的点击事件
     */
    public interface OnTitleClickListener {
        //左边的按钮被点击
        void onLeftClick();

        //标题栏的按钮被点击
        void onCenterClick();

        //标题栏被双击
        void onCenterDoubleClick();

        //右边的按钮被点击
        void onRightClick();
    }

    private OnTitleInsertImageListener onTitleInserImageListener;

    public void setOnTitleInsertImageListener(OnTitleInsertImageListener onTitleInserImageListener) {
        this.onTitleInserImageListener = onTitleInserImageListener;
    }

    public interface OnTitleInsertImageListener {
        void onInsertImageClick();
    }
}