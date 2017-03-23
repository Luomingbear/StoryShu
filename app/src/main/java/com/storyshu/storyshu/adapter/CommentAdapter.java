package com.storyshu.storyshu.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.storyshu.storyshu.R;
import com.storyshu.storyshu.info.CommentInfo;
import com.storyshu.storyshu.utils.BitmapUtil;
import com.storyshu.storyshu.widget.ClickButton;
import com.storyshu.storyshu.widget.imageview.AvatarImageView;

import java.util.List;

/**
 * 评论项生成适配器
 * Created by bear on 2017/3/15.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHold> {
    private ViewHold viewHolder;
    private Context mContext;
    private List<CommentInfo> mCommentList;

    public CommentAdapter(Context mContext, List<CommentInfo> mCommentList) {
        this.mContext = mContext;
        this.mCommentList = mCommentList;
    }

    @Override
    public ViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.comment_layout, parent, false);
        ViewHold viewHolder = new ViewHold(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHold holder, int position) {
        viewHolder = holder;

        final CommentInfo commentInfo = mCommentList.get(position);

//        SImageLoader.getInstance().loadImage(commentInfo.getAvatar(), avatarLoaderListener);
        ImageLoader.getInstance().displayImage(commentInfo.getAvatar(), viewHolder.avatar);

        viewHolder.nickname.setText(commentInfo.getNickname());
        viewHolder.createTime.setText(commentInfo.getCreateTime());
        viewHolder.tag.setText(commentInfo.getTags());
        viewHolder.comment_content.setText(commentInfo.getComment());

        viewHolder.like.setNum(commentInfo.getLikeNum());
        viewHolder.oppose.setNum(commentInfo.getOpposeNum());
    }


    /**
     * 加载头像
     */
    private ImageLoadingListener avatarLoaderListener = new ImageLoadingListener() {
        @Override
        public void onLoadingStarted(String imageUri, View view) {

        }

        @Override
        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
            viewHolder.avatar.setImageBitmap(BitmapUtil.getScaledBitmap(imageUri, 78));
        }

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            viewHolder.avatar.setImageBitmap(loadedImage);
        }

        @Override
        public void onLoadingCancelled(String imageUri, View view) {

        }
    };

    @Override
    public int getItemCount() {
        return mCommentList == null ? 0 : mCommentList.size();
    }

    class ViewHold extends RecyclerView.ViewHolder {
        private AvatarImageView avatar;
        private TextView nickname;
        private TextView createTime;
        private TextView tag;
        private ClickButton like;
        private ClickButton oppose;
        private TextView comment_content;

        private TextView deathTime;
        private ClickButton comment;
        private TextView story;
        private ImageView story_pic;
        private TextView pic_size;
        private TextView location;
        private View report;

        public ViewHold(View itemView) {
            super(itemView);

            avatar = (AvatarImageView) itemView.findViewById(R.id.avatar);
            nickname = (TextView) itemView.findViewById(R.id.nickname);
            createTime = (TextView) itemView.findViewById(R.id.create_time);
            like = (ClickButton) itemView.findViewById(R.id.like);
            oppose = (ClickButton) itemView.findViewById(R.id.oppose);

            tag = (TextView) itemView.findViewById(R.id.floor_tag);
            comment_content = (TextView) itemView.findViewById(R.id.comment_content);
        }

    }

    /**
     * 类型
     */
    public enum Type {
        COMMENT,

        STORY
    }
}
