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
import com.storyshu.storyshu.utils.ToastUtil;
import com.storyshu.storyshu.widget.imageview.RoundImageView;
import com.storyshu.storyshu.widget.scrollview.IScrollView;
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
    private TextView mContent; //正文
    private IScrollView mScrollView; //滚动控件
    private View mLogoLayout; //app作者

    private boolean isTitleShow = true; //标题栏是否隐藏了 否
    private boolean isTitleAnimate = false; //标题栏是否处于动画状态 否

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.story_detail_layout);

        init();

        initView();
    }

    /**
     * 初始化数据
     */
    private void init() {
        mCardInfo = getIntent().getParcelableExtra("story");
    }

    /**
     * 初始化视图
     */
    private void initView() {
        initTitle();

        //头像
        mAvatar = (RoundImageView) findViewById(R.id.story_detail_avatar);
        ImageLoader.getInstance().displayImage(mCardInfo.getUserInfo().getAvatar(), mAvatar);

        //昵称
        mNickname = (TextView) findViewById(R.id.story_detail_nickname);
        mNickname.setText(mCardInfo.getUserInfo().getNickname());

        //标题
        mTitle = (TextView) findViewById(R.id.story_detail_title);
        mTitle.setText(mCardInfo.getTitle());

        //封面图
        ImageView detailPic = (ImageView) findViewById(R.id.story_detail_pic);
        ImageLoader.getInstance().displayImage(mCardInfo.getDetailPic(), detailPic);

        //正文
        mContent = (TextView) findViewById(R.id.story_detail_content_text);
        // TODO: 2016/12/26 获取正文
        mContent.setText("我相信这个世界上，一定会有一个爱你的人，他会穿过这个世间拥挤的人潮，一一的走过他们，怀着一颗用力跳动的心脏，捧着满腔的热，和沉甸甸的爱，走向你，抓紧你，你要等，他一定会找到你的。\n" +
                "\n" +
                "    书上说，世界很大，努力就能多看一些风景；\n" +
                "    书上说，我永远深爱着她，她也一直没有离开我；\n" +
                "    书上说，一个爱自己的人，才懂得爱别人，才值得被生命拥抱；\n" +
                "    书上说，总会有人为你奋不顾身，总有人为你赴汤蹈火；\n" +
                "    书上说，天下没有不散的宴席，别怕，书上还说了，人生何处不相逢。\n" +
                "    鹿人三千，一个喜欢碎片式写作的年轻人，94年生人，和我一般大，他是双子，我是天秤。\n" +
                "\n" +
                "    我是一个很喜欢听故事的人，相比于长篇名著或者实用性更强的专业教材，我更钟情于小说。听过一个朋友说小说不算是真正意义上的书籍吧，也许是的。不过我确实喜欢这种经历别人人生的舒畅感，它会让我满足，让我觉得真的有那么一个人坐在我旁边，告诉我他喜欢过一个怎样的女孩子，我也会问他后来他们有没有在一起，我会想知道他的过往，以及他们的未来。有时候他会笑着说她做饭很好吃，有时候他会哭着说他好像失去她了，有时候我会陪着他发呆，有时候他的故事说完了我会难受就像失去了一个老友。\n" +
                "\n" +
                "    你都如何回忆我，带着笑或是很沉默。\n");

        //整个布局的滚动控件
        mScrollView = (IScrollView) findViewById(R.id.story_detail_list_view);
        mScrollView.setOnScrollListener(scrollListener);

        //app版权
        mLogoLayout = findViewById(R.id.story_detail_logo_layout);

    }


    private int oldScrollY = 0;

    /**
     * 滚动监听世界
     */
    private IScrollView.OnScrollListener scrollListener = new IScrollView.OnScrollListener() {
        @Override
        public void onScroll(int scrollY) {
            float distanceY = scrollY - oldScrollY;
            if (Math.abs(distanceY) <= 1)
                return;

            /**
             * 手指上滑或者滚动至0的位置 隐藏标题栏
             * 手指下滑 显示标题栏
             */
            if (distanceY > 0 || scrollY == 0) {
                animateTitleShow(false);
            } else animateTitleShow(true);
            oldScrollY = scrollY;
        }
    };


    /**
     * 标题栏隐藏、显示
     *
     * @param isShow true 显示 false 隐藏
     */
    private void animateTitleShow(boolean isShow) {
        if (isTitleAnimate || isTitleShow == isShow)
            return;

        float height = getResources().getDimension(R.dimen.title_height);

        float fromY = isShow ? -height : 0;
        height = isShow ? height : -height;

        DecelerateInterpolator dl = new DecelerateInterpolator();  //减速
        ObjectAnimator translationAnimator = ObjectAnimator.ofFloat(mTitleView, "translationY",
                fromY, fromY + height);

        translationAnimator.setInterpolator(dl);
        translationAnimator.setDuration(260);
        translationAnimator.start();
        translationAnimator.addListener(animatorListener);
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
        mTitleView.setOnTitleClickListener(new TitleView.onTitleClickListener() {
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
                saveStory2Image();
            }
        });
    }

    /**
     * 将故事保存为图片分享
     */
    private void saveStory2Image() {
        //显示软件版权信息
        mLogoLayout.setVisibility(View.VISIBLE);

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
