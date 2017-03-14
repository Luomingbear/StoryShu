package com.storyshu.storyshu.utils.time;

import android.text.TextUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间转化工具
 * Created by bear on 2016/12/24.
 */

public class ConvertTimeUtil {
    public static String convertCurrentTime(Date date) {
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
            current = diff / 60 + 1 + "分钟前";
        } else if (diff < 60 * 60 * 24 && diff >= 60 * 60) { //小时数
            current = diff / 60 / 60 + "小时前";
        } else if (diff < 60 * 60 * 24 * 7 && diff >= 60 * 60 * 24) { //天数
            current = diff / 60 / 60 / 24 + "天前";
        } else {
            SimpleDateFormat format = new SimpleDateFormat("(yyyy.MM.dd");
            current = format.format(date);
        }
        return current;
    }

    public static String convertCurrentTime(String dateText) {
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
            current = diff / 60 + 1 + "分钟前";
        } else if (diff < 60 * 60 * 24 && diff >= 60 * 60) { //小时数
            current = diff / 60 / 60 + "小时前";
        } else if (diff < 60 * 60 * 24 * 7 && diff >= 60 * 60 * 24) { //天数
            current = diff / 60 / 60 / 24 + "天前";
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

    ;
}
