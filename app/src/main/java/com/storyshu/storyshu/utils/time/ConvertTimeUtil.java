package com.storyshu.storyshu.utils.time;

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


//        Calendar c = Calendar.getInstance();
//        //发布故事的时间
//        c.setTime(date);
//        int year = c.get(Calendar.YEAR);
//        int month = c.get(Calendar.MONTH);
//        int day = c.get(Calendar.DAY_OF_MONTH);
//        int hour = c.get(Calendar.HOUR_OF_DAY);
//        int minute = c.get(Calendar.MINUTE);
//
//        //当前的时间
//        c.setTime(currentDate);
//        int cyear = c.get(Calendar.YEAR);
//        int cmonth = c.get(Calendar.MONTH);
//        int cday = c.get(Calendar.DAY_OF_MONTH);
//        int chour = c.get(Calendar.HOUR_OF_DAY);
//        int cminute = c.get(Calendar.MINUTE);

        return current;
    }
}
