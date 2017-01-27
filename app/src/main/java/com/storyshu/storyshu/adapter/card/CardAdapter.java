package com.storyshu.storyshu.adapter.card;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.storyshu.storyshu.R;
import com.storyshu.storyshu.adapter.base.IBaseAdapter;
import com.storyshu.storyshu.info.CardInfo;
import com.storyshu.storyshu.utils.BitmapUtil;
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
    private ImageLoadingListener coverLoadListener = new ImageLoadingListener() {
        @Override
        public void onLoadingStarted(String imageUri, View view) {
//            viewHolder.cover
            Log.i(TAG, "onLoadingStarted: ");
        }

        @Override
        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
            Log.i(TAG, "onLoadingFailed: " + failReason.getType());
            if (failReason.getType() == FailReason.FailType.UNKNOWN) {
                viewHolder.cover.setImageBitmap(BitmapUtil.getScaledBitmap(imageUri, 400));
            }
        }

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            Log.i(TAG, "onLoadingComplete: ");
            viewHolder.cover.setImageBitmap(loadedImage);
        }

        @Override
        public void onLoadingCancelled(String imageUri, View view) {
            Log.i(TAG, "onLoadingCancelled: ");
        }
    };

    /**
     * 用户头像的加载监听
     */
    private ImageLoadingListener avatarLoadListener = new ImageLoadingListener() {
        @Override
        public void onLoadingStarted(String imageUri, View view) {

        }

        @Override
        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

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
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = getInflater().inflate(R.layout.card_view_layout, null);

            viewHolder = new ViewHolder();

            viewHolder.cover = (RoundImageView) convertView.findViewById(R.id.card_view_detail_pic);
            viewHolder.title = (TextView) convertView.findViewById(R.id.card_view_title);
            viewHolder.avatar = (RoundImageView) convertView.findViewById(R.id.card_view_head_portrait);
            viewHolder.nickName = (TextView) convertView.findViewById(R.id.card_view_username);
            viewHolder.extract = (TextView) convertView.findViewById(R.id.card_view_extract);
            viewHolder.createDate = (TextView) convertView.findViewById(R.id.card_view_date);
            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();

        CardInfo cardInfo = (CardInfo) getItem(position);
        if (cardInfo == null)
            return convertView;
        //显示本地图片的时候会出现闪烁
//        if (cardInfo.getDetailPic().contains("/storage/emulated/"))
//        displayPic(viewHolder.cover, cardInfo.getDetailPic());
        viewHolder.cover.setImageBitmap(BitmapUtil.getScaledBitmap(cardInfo.getDetailPic(), 280));
//        else
//        ImageLoader.getInstance().loadImage(cardInfo.getDetailPic(), coverLoadListener);

        viewHolder.title.setText(cardInfo.getTitle());
        //
//        Log.i(TAG, "getView: avatar:" + cardInfo.getUserInfo().getAvatar());
        if (cardInfo.getUserInfo().getAvatar().contains("/storage/emulated/"))
            ImageLoader.getInstance().displayImage("file://" + cardInfo.getUserInfo().getAvatar(), viewHolder.avatar);
        else
            ImageLoader.getInstance().loadImage(cardInfo.getUserInfo().getAvatar(), avatarLoadListener);
        //
        viewHolder.nickName.setText(cardInfo.getUserInfo().getNickname());
        viewHolder.extract.setText(cardInfo.getExtract());
        viewHolder.createDate.setText(ConvertTimeUtil.convertCurrentTime(cardInfo.getCreateDate()));
        return convertView;
    }


    /**
     * 显示图片
     *
     * @param imageView
     * @param url
     */
    private void displayPic(final ImageView imageView, String url) {
        final String imgUrl = url;

        final Thread trThread = new Thread(new Runnable() {
            @Override
            public void run() {
                imageView.setImageBitmap(BitmapUtil.getScaledBitmap(imgUrl, 300));

            }
        });

        trThread.run();
    }

    private ViewHolder viewHolder;

    private class ViewHolder {
        RoundImageView cover; //故事的详情图
        TextView title; //故事标题
        RoundImageView avatar; //用户头像
        TextView nickName; //用户昵称
        TextView extract; //故事摘要
        TextView createDate; //发布时间
    }
}
