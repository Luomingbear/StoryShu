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

public class TimeUtils {
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
            SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
            current = format.format(date);
        }
        return current;
    }

//    /**
//     * 将date转化为字符串形式
//     *
//     * @param date
//     * @return
//     */
//    public static String convert2TimeText(Object date) {
//        String current;
//        //HH表示24小时制
//        //hh表示12小时制
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        current = simpleDateFormat.format(date);
//        return current;
//    }

    /**
     * 获取日期
     *
     * @param dateText
     * @return
     */
    public static String getDate(String dateText) {
        if (TextUtils.isEmpty(dateText))
            return "Empty!";

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = dateFormat.parse(dateText);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat format = new SimpleDateFormat("MM.dd");
        String current = format.format(date);
        return current;
    }

    /**
     * 获取月
     *
     * @param dateText
     * @return
     */
    public static String getMonth(String dateText) {
        if (TextUtils.isEmpty(dateText))
            return "Empty!";

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = dateFormat.parse(dateText);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat format = new SimpleDateFormat("M");
        String current = format.format(date);
        return current;
    }

    /**
     * 获取日
     *
     * @param dateText
     * @return
     */
    public static String getDay(String dateText) {
        if (TextUtils.isEmpty(dateText))
            return "Empty!";

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = dateFormat.parse(dateText);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat format = new SimpleDateFormat("d");
        String current = format.format(date);
        return current;
    }


    /**
     * 获取时间
     *
     * @param dateText
     * @return
     */
    public static String getTime(String dateText) {
        if (TextUtils.isEmpty(dateText))
            return "Empty!";

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = dateFormat.parse(dateText);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        String current = format.format(date);
        return current;
    }
//
//    /**
//     * 剩余时间
//     *
//     * @param context
//     * @param createTime
//     * @param lifeTime   单位分钟
//     * @return
//     */
//    public static String convertDestroyTime(Context context, String createTime, int lifeTime) {
//        String destroyTime;
//        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date date = null;
//        try {
//            date = dateFormat.parse(createTime);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        if (date == null)
//            return "";
//
//        Date currentDate = new Date(System.currentTimeMillis());
//        //时间差 /1000变成秒为单位
//        longs diff = (date.getTime() + lifeTime * 60 * 1000 - currentDate.getTime()) / 1000;
//        //小于一天就显示小时
//        if (diff < 60 * 60 * 24) {
//            destroyTime = context.getResources().getString(R.string.left_hour, diff / 60 / 60);
//        } else {
//            destroyTime = context.getResources().getString(R.string.left_day, diff / 60 / 60 / 24);
//        }
//        return destroyTime;
//    }


    /**
     * 距离故事消失还有多少时间
     *
     * @param context
     * @param destroyTime 故事消失的时间
     * @return
     */
    public static String convertDestroyTime(Context context, String destroyTime) {
        if (TextUtils.isEmpty(destroyTime))
            return "";

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = dateFormat.parse(destroyTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (date == null)
            return "";

        Date currentDate = new Date(System.currentTimeMillis());
        //时间差 /1000变成秒为单位
        long diff = (date.getTime() - currentDate.getTime()) / 1000;
        //小于一天就显示小时
        if (diff <= 0)
            destroyTime = context.getResources().getString(R.string.story_overdue);
        else if (diff < 60 * 60) {
            destroyTime = context.getResources().getString(R.string.left_minute, diff / 60);
        } else if (diff < 60 * 60 * 24) {
            destroyTime = context.getResources().getString(R.string.left_hour, diff / 60 / 60);
        } else {
            destroyTime = context.getResources().getString(R.string.left_day, diff / 60 / 60 / 24);
        }
        return destroyTime;
    }

    /**
     * 获得当前的时间，转化为yyyy-MM-dd HH:mm:ss格式
     *
     * @return 字符串的当前时间表示
     */
    public static String getCurrentTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        return dateFormat.format(date);
    }

    public static String getTimeId() {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date(System.currentTimeMillis());
        return dateFormat.format(date);
    }

    /**
     * 获得故事的销毁时间
     *
     * @param lifeMinute 故事的保质期 分钟
     * @return
     */
    public static String getDestroyTime(int lifeMinute) {
        long createTime = System.currentTimeMillis();
        long destroyTime = createTime + lifeMinute * 60 * 1000;
        Date date = new Date(destroyTime);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }

    /**
     * 将小时转化为生命期的表示,在创建故事的时候使用
     * 大于等于24小时用天表示
     *
     * @param context
     * @param minute
     * @return
     */
    public static String hour2lifeTime(Context context, int minute) {
        if (minute < 24 * 60)
            return minute / 60 + context.getString(R.string.hour_unit);
        else {
            return minute / 24 / 60 + context.getString(R.string.day_unit);
        }
    }

    /**
     * 是否过期
     *
     * @param destroyTime
     * @return
     */
    public static boolean isOutOfDate(String destroyTime) {
        if (TextUtils.isEmpty(destroyTime))
            return false;

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = dateFormat.parse(destroyTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (date == null)
            return true;

        if (date.compareTo(new Date(System.currentTimeMillis())) <= 0)
            return true;
        else return false;
    }
}
