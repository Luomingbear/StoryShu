package com.storyshu.storyshu.activity.story;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.storyshu.storyshu.R;
import com.storyshu.storyshu.activity.base.IBaseActivity;
import com.storyshu.storyshu.info.CardInfo;
import com.storyshu.storyshu.model.database.StoryDateBaseHelper;
import com.storyshu.storyshu.utils.ParcelableUtil;
import com.storyshu.storyshu.utils.ToastUtil;
import com.storyshu.storyshu.widget.imageview.RoundImageView;
import com.storyshu.storyshu.widget.more.MoreDialogManager;
import com.storyshu.storyshu.widget.scrollview.IScrollView;
import com.storyshu.storyshu.widget.text.RichTextView;
import com.storyshu.storyshu.widget.title.TitleView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 故事详情
 * Created by bear on 2016/12/21.
 */

public class StoryDetailActivity extends IBaseActivity {
    private static final String TAG = "StoryDetailActivity";
    private CardInfo mCardInfo; //卡片信息
    private TitleView mTitleView; //标题栏
    private RoundImageView mAvatar; //头像
    private TextView mTitle; //标题
    private TextView mNickname; //用户昵称
    private RichTextView mContentView; //正文
    private IScrollView mScrollView; //滚动控件
    private View mLocationLayout; //位置
    private TextView mLocationText; //
    private View mLogoLayout; //app版权
    private View mBottomBar; //底部栏

    private int mAnimateTime = 260; //动画执行时间 毫秒
    private boolean isTitleShow = true; //标题栏是否隐藏了 否
    private boolean isTitleAnimate = false; //标题栏是否处于动画状态 否

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_detail_layout);

        init();

        initView();
    }

    /**
     * 初始化数据
     */
    private void init() {
        mCardInfo = getIntent().getParcelableExtra(ParcelableUtil.STORY);
    }

    /**
     * 初始化视图
     */
    private void initView() {
        initTitle();

        //头像
        mAvatar = (RoundImageView) findViewById(R.id.story_detail_avatar);
        if (mCardInfo.getUserInfo().getAvatar().contains("/storage/emulated/0"))
            ImageLoader.getInstance().displayImage("file://" + mCardInfo.getUserInfo().getAvatar(), mAvatar);
        else ImageLoader.getInstance().displayImage(mCardInfo.getUserInfo().getAvatar(), mAvatar);

        //昵称
        mNickname = (TextView) findViewById(R.id.story_detail_nickname);
        mNickname.setText(mCardInfo.getUserInfo().getNickname());

        //标题
        mTitle = (TextView) findViewById(R.id.story_detail_title);
        mTitle.setText(mCardInfo.getTitle());

        //封面图
        ImageView detailPic = (ImageView) findViewById(R.id.story_detail_pic);
        if (mCardInfo.getDetailPic().contains("/storage/emulated/0"))
            ImageLoader.getInstance().displayImage("file://" + mCardInfo.getDetailPic(), detailPic);
        else ImageLoader.getInstance().displayImage(mCardInfo.getDetailPic(), detailPic);


        //正文
        mContentView = (RichTextView) findViewById(R.id.story_detail_content_view);
        // TODO: 2016/12/26 获取正文
        StoryDateBaseHelper storyDateBaseHelper = new StoryDateBaseHelper(this);
        mContentView.setData(storyDateBaseHelper.getContent(mCardInfo.getStoryId()));

        //整个布局的滚动控件
        mScrollView = (IScrollView) findViewById(R.id.story_detail_list_view);
        mScrollView.setOnScrollListener(scrollListener);

        //位置信息
        mLocationLayout = findViewById(R.id.story_detail_location_layout);
        mLocationText = (TextView) findViewById(R.id.story_detail_location_text);
        mLocationText.setText(mCardInfo.getLocation());

        //app版权
        mLogoLayout = findViewById(R.id.story_detail_logo_layout);


        //底部栏
        mBottomBar = findViewById(R.id.story_detail_bottom_bar);

    }


    private int oldScrollY = 0;

    /**
     * 滚动监听世界
     */
    private IScrollView.OnScrollListener scrollListener = new IScrollView.OnScrollListener() {
        @Override
        public void onScroll(int scrollY) {

            float distanceY = scrollY - oldScrollY;

            /**
             * 如果手指的移动距离过小就不运行动画
             */
            if (Math.abs(distanceY) <= 1)
                return;

            /**
             * 滑动的时候显示标题和底部栏
             */
            mTitleView.setVisibility(View.VISIBLE);
            mBottomBar.setVisibility(View.VISIBLE);

            /**
             * 滑动底部的时候显示标题栏和底部栏
             */
            float scrollHeight = mScrollView.getChildAt(0).getHeight() - mScrollView.getHeight();
            if (scrollHeight - scrollY <= 5)
                animateTitleShow(true);

            /**
             * 手指上滑或者滚动至0的位置 隐藏标题栏
             * 手指下滑 显示标题栏
             */
            if (distanceY > 0 || scrollY <= 5) {
                animateTitleShow(false);
            } else animateTitleShow(true);

            oldScrollY = scrollY;
        }
    };


    /**
     * 标题栏、底部栏 隐藏、显示
     *
     * @param isShow true 显示 false 隐藏
     */
    private void animateTitleShow(boolean isShow) {
        if (isTitleAnimate || isTitleShow == isShow)
            return;

        float height = getResources().getDimension(R.dimen.title_height);


        /**
         * 标题栏
         */
        float fromY = isShow ? -height : 0;
        height = isShow ? height : -height;

        DecelerateInterpolator dl = new DecelerateInterpolator();  //减速
        ObjectAnimator titleAnimator = ObjectAnimator.ofFloat(mTitleView, "translationY",
                fromY, fromY + height);

        titleAnimator.setInterpolator(dl);
        titleAnimator.setDuration(mAnimateTime);
        titleAnimator.addListener(animatorListener);


        /**
         * 底部栏
         */
        fromY = isShow ? height : 0;
        ObjectAnimator bottomAnimator = ObjectAnimator.ofFloat(mBottomBar, "translationY",
                fromY, fromY - height);

        bottomAnimator.setInterpolator(dl);
        bottomAnimator.setDuration(mAnimateTime);


        titleAnimator.start();
        bottomAnimator.start();
        isTitleShow = isShow;
    }

    /**
     * 动画监听器
     */
    private Animator.AnimatorListener animatorListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
            isTitleAnimate = true;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            isTitleAnimate = false;
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            isTitleAnimate = false;
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
            isTitleAnimate = true;
        }
    };

    /**
     * 初始化标题栏
     */
    private void initTitle() {
        //标题
        mTitleView = (TitleView) findViewById(R.id.title_view);
        mTitleView.setTitleString(mCardInfo.getTitle());


        //按钮
        mTitleView.setOnTitleClickListener(new TitleView.OnTitleClickListener() {
            @Override
            public void onLeftClick() {
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
                MoreDialogManager.getInstance().setOnMoreDialogClickListener(onMoreDialogClickListener).showMoreDialog(StoryDetailActivity.this);
            }
        });
    }

    private MoreDialogManager.OnMoreDialogClickListener onMoreDialogClickListener = new MoreDialogManager.OnMoreDialogClickListener() {
        @Override
        public void onLongPicClick() {
            ToastUtil.Show(StoryDetailActivity.this, R.string.save_long_pic);
            saveStory2Image();
        }

        @Override
        public void onCoverClick() {
            ToastUtil.Show(StoryDetailActivity.this, R.string.save_cover);

        }

        @Override
        public void onReportClick() {
            ToastUtil.Show(StoryDetailActivity.this, R.string.report);

        }
    };

    /**
     * 将故事保存为图片分享
     */
    private void saveStory2Image() {
        //显示软件版权信息
        mLogoLayout.setVisibility(View.VISIBLE);
        //位置信息
        // TODO: 2017/1/5 请求服务器获取位置信息
        //
        Bitmap storyImage = compressImage(getBitmapByView(mScrollView));
        Log.i(TAG, "saveStory2Image: 开始保存到本地");
        /**
         * 保存到指定的路径
         */

        File fileDirection = new File(Environment.getExternalStorageDirectory() + "/storyImage");
        if (!fileDirection.isDirectory()) {
            try {
                fileDirection.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String fileName = mCardInfo.getTitle() + "-" + mCardInfo.getUserInfo().getNickname() + ".jpg";
        File f = new File(fileDirection + "/", fileName);
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            storyImage.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            Log.i(TAG, "已经保存");
            //隐藏版权信息
            mLogoLayout.setVisibility(View.GONE);

            //提示用户保存完毕
            ToastUtil.Show(StoryDetailActivity.this, R.string.save_screenshot_succeed);
        } catch (FileNotFoundException e) {
            ToastUtil.Show(StoryDetailActivity.this, R.string.save_screenshot_failed);
            e.printStackTrace();
        } catch (IOException e) {
            ToastUtil.Show(StoryDetailActivity.this, R.string.save_screenshot_failed);
            e.printStackTrace();
        }


        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(getContentResolver(),
                    f.getAbsolutePath(), fileName, null);
            Log.i(TAG, "saveStory2Image: 插入系统图库");
        } catch (FileNotFoundException e) {
            Log.e(TAG, "saveStory2Image: 系统图库插入失败");
            e.printStackTrace();
        }
        // 最后通知图库更新
//        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + f.getPath())));

        /**
         * 保存到系统相册
         */
//        MediaStore.Images.Media.insertImage(getContentResolver(), storyImage, mCardInfo.getTitle(), mCardInfo.getExtract());
//        Log.i(TAG, "已经保存");

    }

    /**
     * 截取scrollview的屏幕
     *
     * @param scrollView
     * @return
     */
    public static Bitmap getBitmapByView(ScrollView scrollView) {
        Log.i(TAG, "getBitmapByView: 开始获取bitmap");
        int h = 0;
        Bitmap bitmap = null;
        // 获取scrollview实际高度
        for (int i = 0; i < scrollView.getChildCount(); i++) {
            h += scrollView.getChildAt(i).getHeight();
            //设置每个view的背景色，否则屏幕不可见的地方是黑色的
            scrollView.getChildAt(i).setBackgroundColor(
                    Color.parseColor("#ffffff"));
        }
        // 创建对应大小的bitmap
        bitmap = Bitmap.createBitmap(scrollView.getWidth(), h,
                Bitmap.Config.ARGB_4444);
        final Canvas canvas = new Canvas(bitmap);
        scrollView.draw(canvas);
        Log.i(TAG, "getBitmapByView: bitmap获取结束");
        return bitmap;
    }

    /**
     * 压缩图片
     *
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {
        Log.i(TAG, "compressImage: 开始压缩图片");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int options = 100;
        // 循环判断如果压缩后图片是否大于1024kb,大于继续压缩
        while (baos.toByteArray().length / 1024 > 1024 * 1.5f) {
            // 重置baos
            baos.reset();
            // 这里压缩options%，把压缩后的数据存放到baos中
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);
            // 每次都减少10
            options -= 10;
            options = Math.max(options, 10);
        }
        // 把压缩后的数据baos存放到ByteArrayInputStream中
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        // 把ByteArrayInputStream数据生成图片
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
        Log.i(TAG, "compressImage: 压缩完成");
        return bitmap;
    }
}
