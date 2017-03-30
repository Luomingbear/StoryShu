package com.storyshu.storyshu.model;

import android.content.Context;

import com.storyshu.storyshu.info.CommentInfo;
import com.storyshu.storyshu.utils.time.TimeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 评论的数据管理
 * Created by bear on 2017/3/30.
 */

public class CommentModel {
    private Context mContext;
    private OnCommentsGotListener onComentsGotListener;

    public void setOnComentsGotListener(OnCommentsGotListener onComentsGotListener) {
        this.onComentsGotListener = onComentsGotListener;
    }

    public interface OnCommentsGotListener {
        /**
         * 热门的评论获取成功
         *
         * @param commentList
         */
        void onHotCommentsGot(List<CommentInfo> commentList);

        /**
         * 获取的最新的评论
         *
         * @param commentList
         */
        void onNewCommentsGot(List<CommentInfo> commentList);

        /**
         * 评论获取成功的回调
         *
         * @param commentList
         */
        void onCommentsGot(List<CommentInfo> commentList);
    }

    /**
     * 获取评论总数的接口
     */
    public interface OnCommentSizeListener {
        void onCommentSizeGot(int size);
    }

    public CommentModel(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 获取热门的评论
     */
    public void getHotComments(int count) {
        // TODO: 2017/3/30 获取网络的评论数据
        List<CommentInfo> mCommentInfoList = new ArrayList<>();

        for (int i = 1; i <= count; i++) {
            CommentInfo commentInfo = new CommentInfo();
            commentInfo.setNickname("赵日天");
            commentInfo.setAvatar("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3022635415,3979946006&fm=23&gp=0.jpg");
            commentInfo.setCreateTime(TimeUtils.getCurrentTime());
            commentInfo.setOpposeNum(23);
            commentInfo.setLikeNum(66);
            commentInfo.setTags(i + "#");
            commentInfo.setComment("我赵日天表示不服,你怕了吗？");
            mCommentInfoList.add(commentInfo);
        }

        if (onComentsGotListener != null)
            onComentsGotListener.onHotCommentsGot(mCommentInfoList);
    }


    /**
     * 获取最新的评论
     */
    public void getCommentsByTime() {
        List<CommentInfo> mCommentInfoList = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            CommentInfo commentInfo = new CommentInfo();
            commentInfo.setNickname("吃不饱的长颈鹿");
            commentInfo.setAvatar("http://img.hb.aicdn.com/d460cddab6bc8631e97d2a2a50a58a0436cdbc36b2f5-KjSbzk_fw658");
            commentInfo.setCreateTime(TimeUtils.getCurrentTime());
            commentInfo.setOpposeNum(23);
            commentInfo.setLikeNum(66);
            commentInfo.setTags(i + "#");
            commentInfo.setComment("有趣");
            mCommentInfoList.add(commentInfo);
        }

        if (onComentsGotListener != null)
            onComentsGotListener.onNewCommentsGot(mCommentInfoList);
    }

    /**
     * 获取评论，前面的三条是按热度获取的，后面的是按时间排列，最新的排列在前面
     */
    public void getComments() {
        List<CommentInfo> mCommentInfoList = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            CommentInfo commentInfo = new CommentInfo();
            commentInfo.setNickname("五哥");
            commentInfo.setAvatar("http://img.hb.aicdn.com/d460cddab6bc8631e97d2a2a50a58a0436cdbc36b2f5-KjSbzk_fw658");
            commentInfo.setCreateTime(TimeUtils.getCurrentTime());
            commentInfo.setOpposeNum(23);
            commentInfo.setLikeNum(66);
            commentInfo.setTags(i + "#");
            commentInfo.setComment("有趣");
            mCommentInfoList.add(commentInfo);
        }

        mCommentInfoList.get(0).setAvatar("http://img.hb.aicdn.com/fc7451e5969803ddb9cec8e43f142c227febce2c9ccc-no6NIh_fw658");
        mCommentInfoList.get(0).setNickname("小神经");
        mCommentInfoList.get(0).setComment("可以的，贼溜");

        mCommentInfoList.get(1).setAvatar("http://img.hb.aicdn.com/ea63b3a2c054aee1e0ba4cef222bd8fc7369f2b8ae097-7Q8N5h_fw658");
        mCommentInfoList.get(1).setNickname("田野君");
        mCommentInfoList.get(1).setComment("笑死");

        mCommentInfoList.get(2).setAvatar("http://img.hb.aicdn.com/0744277efcda6015e9efd01b229e21d7703fa1ea3deaa-ffQN2B_fw658");
        mCommentInfoList.get(2).setNickname("普菲达");
        mCommentInfoList.get(2).setComment("我喜欢你的套路");

        mCommentInfoList.get(3).setAvatar("http://img.hb.aicdn.com/a1bb8ec1aa4a2491d17a3a33cd464be55a46858411045b-Gj2i6V_fw658");
        mCommentInfoList.get(3).setNickname("Boomtyl");
        mCommentInfoList.get(3).setComment("哈哈哈哈哈");

        if (onComentsGotListener != null)
            onComentsGotListener.onCommentsGot(mCommentInfoList);
    }

    /**
     * 获取该故事目前一共的评论数量
     */
    public void getCommentsNum(OnCommentSizeListener onCommentSizeListener) {
        // TODO: 2017/3/30 获取评论的总数
        if (onCommentSizeListener != null) {
            onCommentSizeListener.onCommentSizeGot(66);
        }
    }
}
