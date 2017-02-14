package com.storyshu.storyshu.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.storyshu.storyshu.R;
import com.storyshu.storyshu.info.StoryInfo;
import com.storyshu.storyshu.utils.BitmapUtil;
import com.storyshu.storyshu.utils.time.ConvertTimeUtil;
import com.storyshu.storyshu.widget.imageview.RoundImageView;

import java.util.List;

/**
 * 我的故事的适配器
 * Created by bear on 2017/1/18.
 */

public class MyStoryAdapter extends RecyclerView.Adapter implements View.OnClickListener {
    private static final String TAG = "MyStoryAdapter";
    private ViewHold viewHolder;
    private Context mContext;
    private List<StoryInfo> mStoryList;

    public MyStoryAdapter(Context context, List<StoryInfo> mList) {
        mContext = context;
        mStoryList = mList;
    }

    /**
     * 详情图片的加载监听
     */
    private ImageLoadingListener detailLoaderListener = new ImageLoadingListener() {
        @Override
        public void onLoadingStarted(String imageUri, View view) {

        }

        @Override
        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
            if (failReason.getType() == FailReason.FailType.UNKNOWN)
                ImageLoader.getInstance().displayImage("file://" + imageUri, viewHolder.detailPic);
        }

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            viewHolder.detailPic.setImageBitmap(loadedImage);
        }

        @Override
        public void onLoadingCancelled(String imageUri, View view) {

        }
    };

    private ImageLoadingListener avatarLoaderListener = new ImageLoadingListener() {
        @Override
        public void onLoadingStarted(String imageUri, View view) {

        }

        @Override
        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
            viewHolder.avater.setImageBitmap(BitmapUtil.getScaledBitmap(imageUri, 78));
        }

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            viewHolder.avater.setImageBitmap(loadedImage);
        }

        @Override
        public void onLoadingCancelled(String imageUri, View view) {

        }
    };


    /**
     * 生成布局文件
     * 配合viewholder
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.story_item_layout, parent, false);

        viewHolder = new ViewHold(view);
        view.setOnClickListener(this);
        return viewHolder;
    }

    /**
     * 将数据与视图绑定
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        viewHolder = (ViewHold) holder;
        StoryInfo storyInfo = mStoryList.get(position);
        ImageLoader.getInstance().loadImage(storyInfo.getUserInfo().getAvatar(), avatarLoaderListener);
        ImageLoader.getInstance().loadImage(storyInfo.getDetailPic(), detailLoaderListener);
        viewHolder.nickname.setText(storyInfo.getUserInfo().getNickname());
        viewHolder.location.setText(storyInfo.getLocation());
        viewHolder.createDate.setText(ConvertTimeUtil.convertCurrentTime(storyInfo.getCreateDate()));
        viewHolder.title.setText(storyInfo.getTitle());
        viewHolder.extarct.setText(storyInfo.getExtract());


        viewHolder.clickLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: !!!!");
                if (onStoryItemClickListener != null)
                    onStoryItemClickListener.onStoryClick(mStoryList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mStoryList == null ? 0 : mStoryList.size();
    }

    @Override
    public void onClick(View v) {

    }

    private class ViewHold extends RecyclerView.ViewHolder {
        private RoundImageView avater;
        private TextView nickname;
        private TextView location;
        private TextView createDate;
        private RoundImageView detailPic;
        private TextView title;
        private TextView extarct;
        private View clickLayout;

        public ViewHold(View itemView) {
            super(itemView);
            avater = (RoundImageView) itemView.findViewById(R.id.avatar);
            nickname = (TextView) itemView.findViewById(R.id.nickname);
            location = (TextView) itemView.findViewById(R.id.location);
            createDate = (TextView) itemView.findViewById(R.id.create_date);
            detailPic = (RoundImageView) itemView.findViewById(R.id.detail_pic);
            title = (TextView) itemView.findViewById(R.id.story_title);
            extarct = (TextView) itemView.findViewById(R.id.extract);
            clickLayout = itemView.findViewById(R.id.story_click_layout);
        }
    }


    private OnStoryItemClickListener onStoryItemClickListener;

    public void setOnStoryItemClickListener(OnStoryItemClickListener onStoryItemClickListener) {
        this.onStoryItemClickListener = onStoryItemClickListener;
    }

    public interface OnStoryItemClickListener {
        /**
         * 故事被点击
         */
        void onStoryClick(StoryInfo storyInfo);
    }
}
