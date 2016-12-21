package com.storyshu.storyshu.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.storyshu.storyshu.R;
import com.storyshu.storyshu.info.CardInfo;
import com.storyshu.storyshu.utils.time.ConvertTimeUtil;
import com.storyshu.storyshu.widget.imageview.RoundImageView;

import java.util.List;

/**
 * 故事卡片适配器
 * Created by bear on 2016/12/4.
 */

public class CardAdapter extends IBaseAdapter {
    private static final String TAG = "CardAdapter";

    public CardAdapter(Context context, List<?> mList) {
        super(context, mList);
    }

    /**
     * 故事说明图的加载监听
     */
    private ImageLoadingListener detailPicLoadListener = new ImageLoadingListener() {
        @Override
        public void onLoadingStarted(String imageUri, View view) {

        }

        @Override
        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

        }

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            viewHolder.storyPic.setImageBitmap(loadedImage);
        }

        @Override
        public void onLoadingCancelled(String imageUri, View view) {

        }
    };

    /**
     * 用户头像的加载监听
     */
    private ImageLoadingListener headPortraitLoadListener = new ImageLoadingListener() {
        @Override
        public void onLoadingStarted(String imageUri, View view) {

        }

        @Override
        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

        }

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            viewHolder.headPortrait.setImageBitmap(loadedImage);
        }

        @Override
        public void onLoadingCancelled(String imageUri, View view) {

        }
    };

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = getInflater().inflate(R.layout.card_view_layout, null);

            viewHolder = new ViewHolder();

            viewHolder.storyPic = (RoundImageView) convertView.findViewById(R.id.card_view_detail_pic);
            viewHolder.title = (TextView) convertView.findViewById(R.id.card_view_title);
            viewHolder.headPortrait = (RoundImageView) convertView.findViewById(R.id.card_view_head_portrait);
            viewHolder.nickName = (TextView) convertView.findViewById(R.id.card_view_username);
            viewHolder.extract = (TextView) convertView.findViewById(R.id.card_view_extract);
            viewHolder.createDate = (TextView) convertView.findViewById(R.id.card_view_date);
            convertView.setTag(viewHolder);
        } else convertView.getTag();

        CardInfo cardInfo = (CardInfo) getItem(position);
        if (cardInfo == null)
            return convertView;
        ImageLoader.getInstance().displayImage(cardInfo.getDetailPic(), viewHolder.storyPic, detailPicLoadListener);
        viewHolder.title.setText(cardInfo.getTitle());
        ImageLoader.getInstance().displayImage(cardInfo.getUserInfo().getHeadPortrait(), viewHolder.headPortrait, headPortraitLoadListener);
        viewHolder.nickName.setText(cardInfo.getUserInfo().getNickname());
        viewHolder.extract.setText(cardInfo.getExtract());
        viewHolder.createDate.setText(ConvertTimeUtil.convertCurrentTime(cardInfo.getCreateDate()));
        return convertView;
    }

    private ViewHolder viewHolder;

    private class ViewHolder {
        RoundImageView storyPic; //故事的详情图
        TextView title; //故事标题
        RoundImageView headPortrait; //用户头像
        TextView nickName; //用户昵称
        TextView extract; //故事摘要
        TextView createDate; //发布时间
    }
}
