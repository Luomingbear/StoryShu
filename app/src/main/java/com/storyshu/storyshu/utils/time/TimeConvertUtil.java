package com.storyshu.storyshu.utils.time;

import android.content.Context;
import android.text.TextUtils;

import com.storyshu.storyshu.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间转化工具
 * Created by bear on 2016/12/24.
 */

public class TimeConvertUtil {
    public static String convertCurrentTime(Context context, Date date) {
        String current;
        Date currentDate = new Date(System.currentTimeMillis());

        //时间差 /1000变成秒为单位
        long diff = (currentDate.getTime() - date.getTime()) / 1000;

        /**
         * 超过一周显示具体的日期
         * 一周以内一天以上显示多少添前
         * 。
         */
        if (diff >= 7 * 24 * 60 * 60) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
            current = format.format(date);

        } else if (diff < 60 * 60) { //分钟数
            current = context.getResources().getString(R.string.before_minute, diff / 60 + 1);
        } else if (diff < 60 * 60 * 24 && diff >= 60 * 60) { //小时数
            current = context.getResources().getString(R.string.before_hour, diff / 60 / 60);
        } else if (diff < 60 * 60 * 24 * 7 && diff >= 60 * 60 * 24) { //天数
            current = context.getResources().getString(R.string.before_day, diff / 60 / 60 / 24);
        } else {
            SimpleDateFormat format = new SimpleDateFormat("(yyyy.MM.dd");
            current = format.format(date);
        }
        return current;
    }

    public static String convertCurrentTime(Context context, String dateText) {
        if (TextUtils.isEmpty(dateText))
            return "Empty!";

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = dateFormat.parse(dateText);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (date == null)
            return "";

        String current;
        Date currentDate = new Date(System.currentTimeMillis());

        //时间差 /1000变成秒为单位
        long diff = (currentDate.getTime() - date.getTime()) / 1000;

        /**
         * 超过一周显示具体的日期
         * 一周以内一天以上显示多少添前
         * 。
         * 。
         * 。
         */
        if (diff >= 7 * 24 * 60 * 60) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
            current = format.format(date);

        } else if (diff < 60 * 60) { //分钟数
            current = context.getResources().getString(R.string.before_minute, diff / 60 + 1);
        } else if (diff < 60 * 60 * 24 && diff >= 60 * 60) { //小时数
            current = context.getResources().getString(R.string.before_hour, diff / 60 / 60);
        } else if (diff < 60 * 60 * 24 * 7 && diff >= 60 * 60 * 24) { //天数
            current = context.getResources().getString(R.string.before_day, diff / 60 / 60 / 24);
        } else {
            SimpleDateFormat format = new SimpleDateFormat("(yyyy.MM.dd");
            current = format.format(date);
        }
        return current;
    }

    /**
     * 将date转化为字符串形式
     *
     * @param date
     * @return
     */
    public static String convert2TimeText(Object date) {
        String current;
        //HH表示24小时制
        //hh表示12小时制
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        current = simpleDateFormat.format(date);
        return current;
    }

    /**
     * 剩余时间
     *
     * @param context
     * @param createTime
     * @param lifeTime
     * @return
     */
    public static String destroyTime(Context context, String createTime, int lifeTime) {
        String destroyTime;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = dateFormat.parse(createTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (date == null)
            return "";

        Date currentDate = new Date(System.currentTimeMillis());
        //时间差 /1000变成秒为单位
        long diff = (date.getTime() + lifeTime * 60 * 60 * 1000 - currentDate.getTime()) / 1000;
        //小于一天就显示小时
        if (diff < 60 * 60 * 24) {
            destroyTime = context.getResources().getString(R.string.left_hour, diff / 60 / 60);
        } else {
            destroyTime = context.getResources().getString(R.string.left_day, diff / 60 / 60 / 24);
        }
        return destroyTime;
    }
}
