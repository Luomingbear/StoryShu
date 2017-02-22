package com.storyshu.storyshu.activity.create;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.storyshu.storyshu.R;
import com.storyshu.storyshu.activity.base.ChooseImageResultActivity;
import com.storyshu.storyshu.info.StoryInfo;
import com.storyshu.storyshu.utils.StatusBarUtil;
import com.storyshu.storyshu.utils.ToastUtil;
import com.storyshu.storyshu.utils.sharepreference.ISharePreference;
import com.storyshu.storyshu.widget.inputview.TextToolWindow;
import com.storyshu.storyshu.widget.text.RichTextEditor;
import com.storyshu.storyshu.widget.title.TitleView;

/**
 * 写故事的页面啊
 * Created by bear on 2016/12/19.
 */

public class CreateStoryActivity extends ChooseImageResultActivity implements View.OnClickListener {
    private static final String TAG = "CreateStoryActivity";
    private TitleView mTitleView; //标题栏
    private ScrollView mStoryScrollView; //滚动布局
    private ImageView mCoverIv; //封面view
    private EditText mTitleEt; //编辑标题栏
    private EditText mExtractEt; //编辑简介栏
    private RichTextEditor mStoryEdit; //正文编辑栏
    private TextToolWindow mTextToolWindow; //键盘上的工具栏

    private String mCoverPath; //封面图
    private String mContent; //正文
    private String mTitle; //正文
    private String mExtract; //简介
    private boolean isEditStory = false; //是否在编辑故事文本


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_story_layout);

        initData();

        initView();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mCoverPath = ISharePreference.getCoverPic(this);
        mTitle = ISharePreference.getTitle(this);
        mExtract = ISharePreference.getExtra(this);
        mContent = ISharePreference.getContent(this);
    }

    /**
     * 设置封面
     */
    private void setCover() {
        ImageLoader.getInstance().displayImage("file://" + mCoverPath, mCoverIv);
    }

    /**
     * 插入tab符在光标位置
     */
    private void insertTab() {
        int index = mStoryEdit.getLastFocusEdit().getSelectionStart();
        Editable editable = mStoryEdit.getLastFocusEdit().getText();
        editable.insert(index, "\t\t\t\t");
    }

    private TextToolWindow.OnTextToolClickListener textToolClickListener = new TextToolWindow.OnTextToolClickListener() {
        @Override
        public void FirstClick() {
            mStoryEdit.undoText();
        }

        @Override
        public void SecondClick() {
            insertTab();
        }

        @Override
        public void ThirdClick() {
            chooseImage();
        }
    };

    /**
     * 初始化
     */
    private void initView() {
        //标题栏
        mTitleView = (TitleView) findViewById(R.id.title_view);
        initTitleView();

        //滚动布局
        mStoryScrollView = (ScrollView) findViewById(R.id.story_scroll_layout);

        //封面
        mCoverIv = (ImageView) findViewById(R.id.cover_pic);
        mCoverIv.setOnClickListener(this);
        setCover();

        //标题
        mTitleEt = (EditText) findViewById(R.id.cover_title);
        mTitleEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                isEditStory = !hasFocus;
            }
        });
        mTitleEt.setText(mTitle);

        //简介
        mExtractEt = (EditText) findViewById(R.id.cover_extract);
        mExtractEt.setText(mExtract);
        mExtractEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                isEditStory = !hasFocus;
            }
        });

        //正文编辑()
        mStoryEdit = (RichTextEditor) findViewById(R.id.story_edit);
        if (!TextUtils.isEmpty(mContent))
            mStoryEdit.setDataContent(mContent);

        //键盘上的工具栏
        mTextToolWindow = new TextToolWindow(CreateStoryActivity.this);
        mTextToolWindow.init(getWindow());
        mTextToolWindow.setOnTextToolClickListener(textToolClickListener);
        autoShowTextTool();
    }

    /**
     * 计算键盘的高度
     */
    interface IKeyBoardVisibleListener {
        void onSoftKeyBoardVisible(boolean visible, int windowBottom);
    }

    boolean isVisiableForLast = false;

    public void addOnSoftKeyBoardVisibleListener(Activity activity, final IKeyBoardVisibleListener listener) {
        final View decorView = activity.getWindow().getDecorView();
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                decorView.getWindowVisibleDisplayFrame(rect);
                //计算出可见屏幕的高度
                int displayHight = rect.bottom - rect.top;
                //获得屏幕整体的高度
                int hight = decorView.getHeight();
                //获得键盘高度
                int keyboardHeight = hight - displayHight;
                boolean visible = (double) displayHight / hight < 0.8;
                if (visible != isVisiableForLast) {
                    listener.onSoftKeyBoardVisible(visible, keyboardHeight);
                }
                isVisiableForLast = visible;
            }
        });
    }

    /**
     * 处理文本工具显示与否
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
//                    mStoryScrollView.setScrollY(showBottom);
                    mTextToolWindow.showAtLocation(mStoryEdit, Gravity.BOTTOM, 0, showBottom);
                    findViewById(R.id.bottom_view).setVisibility(View.VISIBLE);
                    Log.i(TAG, "handleMessage: 显示文本工具");
                    break;
                case 2:
                    if (mTextToolWindow != null && mTextToolWindow.isShowing()) {
                        mTextToolWindow.dismiss();
                        findViewById(R.id.bottom_view).setVisibility(View.GONE);
                    }
                    Log.i(TAG, "handleMessage: 隐藏文本工具");
                    break;
            }
        }
    };

    private int showBottom; //文本工具显示的高度

    /**
     * 自动显示文本工具栏在键盘的上方
     */
    private void autoShowTextTool() {
        addOnSoftKeyBoardVisibleListener(this, new IKeyBoardVisibleListener() {
            @Override
            public void onSoftKeyBoardVisible(boolean visible, int windowBottom) {
                if (visible && isEditStory) {
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);
                    //减去状态栏的高度
                    showBottom = windowBottom - StatusBarUtil.getHeight(CreateStoryActivity.this);
                } else {
                    Message msg = new Message();
                    msg.what = 2;
                    handler.sendMessage(msg);
                }
            }
        });
    }

    /**
     * 检查故事的数据
     */
    private void checkStoryData() {
        //正文少于60字不能发布
        if (TextUtils.isEmpty(mStoryEdit.getEditString()) ||
                mStoryEdit.getEditString().length() < 60) {
            ToastUtil.Show(CreateStoryActivity.this, R.string.story_content_too_small);
            return;
        }

        //
        StoryInfo storyBaseInfo = new StoryInfo();
        storyBaseInfo.setContent(mStoryEdit.getEditString());
        if (TextUtils.isEmpty(ISharePreference.getExtra(CreateStoryActivity.this)))
            ISharePreference.saveExtra(CreateStoryActivity.this, storyBaseInfo.getExtract());
        storyBaseInfo.setExtract(ISharePreference.getExtra(CreateStoryActivity.this));

        storyBaseInfo.setDetailPic(mStoryEdit.getCoverPic());
        ISharePreference.saveContent(CreateStoryActivity.this, mStoryEdit.getEditString());
        if (TextUtils.isEmpty(ISharePreference.getCoverPic(CreateStoryActivity.this)))
            ISharePreference.saveCoverPic(CreateStoryActivity.this, mStoryEdit.getCoverPic());

//        intentWithParcelable(CreateCoverActivity.class, ParcelableUtil.STORY, storyBaseInfo);

    }

    /**
     * 标题栏的设置和响应事件
     */
    private void initTitleView() {
        mTitleView.setOnTitleClickListener(new TitleView.OnTitleClickListener() {
            @Override
            public void onLeftClick() {
                ISharePreference.saveContent(CreateStoryActivity.this, mStoryEdit.getEditString());
                finish();
            }

            @Override
            public void onCenterClick() {

            }

            @Override
            public void onCenterDoubleClick() {

            }

            @Override
            public void onRightClick() {
                //
                checkStoryData();
            }
        });

        mTitleView.setOnTitleInsertImageListener(new TitleView.OnTitleInsertImageListener() {
            @Override
            public void onInsertImageClick() {
                chooseImage();
            }
        });
    }

    @Override
    public void onResult(String imagePath, int requstCode) {
        switch (requstCode) {
            case IMAGE:
                mStoryEdit.insertImage(imagePath);
                break;
            case COVER:
                mCoverPath = imagePath;
                setCover();
                break;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        //保存临时数据
        ISharePreference.saveTitle(CreateStoryActivity.this, mTitleEt.getText().toString());
        ISharePreference.saveExtra(CreateStoryActivity.this, mExtractEt.getText().toString());
        ISharePreference.saveCoverPic(CreateStoryActivity.this, mCoverPath);
        ISharePreference.saveContent(CreateStoryActivity.this, mStoryEdit.getEditString());
        super.onStop();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cover_pic:
                chooseCover();
                break;
        }
    }
}
