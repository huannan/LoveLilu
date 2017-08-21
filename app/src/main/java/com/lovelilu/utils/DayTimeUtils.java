package com.lovelilu.utils;

import android.content.Context;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by huannan on 2016/8/24.
 */
public class DayTimeUtils {

    public static final String TAG = DayTimeUtils.class.getSimpleName();

    public static String getToday() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());

        String sYear = String.valueOf(cal.get(Calendar.YEAR));
        String sMonth = String.valueOf(cal.get(Calendar.MONTH) + 1);
        String sDay = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));

        return sYear + "#" + sMonth + "#" + sDay;
    }

    public static int getYear() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        return cal.get(Calendar.YEAR);
    }

    public static int getMon() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        return cal.get(Calendar.MONTH) + 1;
    }

    public static String getDate() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());

        String sYear = String.valueOf(cal.get(Calendar.YEAR));
        String sMonth = String.valueOf(cal.get(Calendar.MONTH) + 1);
        String sDay = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));

        String week = getWeek(cal.get(Calendar.DAY_OF_WEEK));

        return sYear + "年" + sMonth + "月" + sDay + "日" + " " + week;
    }

    public static long getDays(Context context) {

        int[] d = PreferenceUtils.getInstance(context).getDateTimes();

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());

        String sYear = String.valueOf(cal.get(Calendar.YEAR));
        String sMonth = String.valueOf(cal.get(Calendar.MONTH) + 1);
        String sDay = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));

        Log.e(TAG, "initday" + d[0] + "-" + d[1] + "-" + d[2]);
        Log.e(TAG, "today" + sYear + "-" + sMonth + "-" + sDay);

        // 将时间设置成系统的时间
        cal.set(Integer.parseInt(sYear), Integer.parseInt(sMonth), Integer.parseInt(sDay));
        // 将sysTime转换成毫秒
        long sysTime = cal.getTimeInMillis();
        // 将时间设置到用户输入的时间
        cal.set(d[0], d[1], d[2]);
        // 将userTime转换成毫秒
        long userTime = cal.getTimeInMillis();
        // 计算两个时间相隔的天数
        long diffDay = (userTime - sysTime) / (1000 * 60 * 60 * 24);
        return -diffDay;
    }

    public static String getWeek(int dayOfWeek) {
        String week = "";
        switch (dayOfWeek) {

            case Calendar.SUNDAY:
                week = "星期天";
                break;
            case Calendar.MONDAY:
                week = "星期一";
                break;
            case Calendar.TUESDAY:
                week = "星期二";
                break;
            case Calendar.WEDNESDAY:
                week = "星期三";
                break;
            case Calendar.THURSDAY:
                week = "星期四";
                break;
            case Calendar.FRIDAY:
                week = "星期五";
                break;
            case Calendar.SATURDAY:
                week = "星期六";
                break;
        }
        return week;
    }


    public static int getDaysBetween(long timeInMillis) {

        Calendar d1 = Calendar.getInstance();//传入的时间
        Calendar d2 = Calendar.getInstance();//今天

        //判断是否是未来
        boolean isFuture = timeInMillis >= d2.getTimeInMillis();
        d1.setTimeInMillis(timeInMillis);

        //正数代表未来，负数代表过去
        return getDaysBetween(d1, d2);
    }


    public static int getDaysBetween(Calendar d1, Calendar d2) {
        boolean past = false;

        if (d1.after(d2)) {
            Calendar swap = d1;
            d1 = d2;
            d2 = swap;
            past = true;
        } else {
            past = false;
        }

        int days = d2.get(Calendar.DAY_OF_YEAR) - d1.get(Calendar.DAY_OF_YEAR);
        int y2 = d2.get(Calendar.YEAR);
        if (d1.get(Calendar.YEAR) != y2) {
            d1 = (Calendar) d1.clone();
            do {
                days += d1.getActualMaximum(Calendar.DAY_OF_YEAR);
                d1.add(Calendar.YEAR, 1);
            } while (d1.get(Calendar.YEAR) != y2);
        }
        return past ? days : 0 - days;
    }

    /**
     * 得到纪念日的天数，如果过去了，需要加年份重新计算
     *
     * @param timeInMillis 纪念日的时间
     * @return
     */
    public static int getAnniversariesDaysBetween(long timeInMillis) {
        long repeatDateTime = dealRepeatDateTime(timeInMillis, false);
        return getDaysBetween(repeatDateTime);
    }


    /**
     * 处理重复周期时间
     *
     * @return
     */
    public static long dealRepeatDateTime(long timeInMillis, boolean toNextRemind) {
        long nextRemindTime;
        //周期提醒时间
        String nowDate = getCurrentDateTimeString("yyyy-MM-dd");
        String targetDate = convertMillisToDateString(timeInMillis, "yyyy-MM-dd");
        String[] nowDates = nowDate.split("-");
        String[] targetDates = targetDate.split("-");

        boolean isPassed = getMillisecondByDateTime(nowDate) > timeInMillis;
        int plusYear = 0;
        if (isPassed) {
            if (Integer.parseInt(nowDates[1]) < Integer.parseInt(targetDates[1])) {
                plusYear = (Integer.parseInt(targetDates[0]) - Integer.parseInt(nowDates[0]));
            } else if (Integer.parseInt(nowDates[1]) == Integer.parseInt(targetDates[1])) {
                if (Integer.parseInt(nowDates[2]) <= Integer.parseInt(targetDates[2])) {
                    plusYear = Integer.parseInt(nowDates[0]) - (Integer.parseInt(targetDates[0]));
                } else {
                    plusYear = Integer.parseInt(nowDates[0]) - (Integer.parseInt(targetDates[0])) + 1;
                }
            } else {
                plusYear = Integer.parseInt(nowDates[0]) - (Integer.parseInt(targetDates[0])) + 1;
            }
        }
        nextRemindTime = isPassed ? getModifyYearString(timeInMillis, "yyyy-MM-dd", Math.abs(plusYear + (toNextRemind ? 1 : 0))) : timeInMillis;
        return nextRemindTime;
    }


    public static long getMillisecondByDateTime(String str) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        long millionSeconds = 0;
        try {
            millionSeconds = sdf.parse(str).getTime();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return millionSeconds;
    }

    public static String getCurrentDateTimeString(String format) {
        SimpleDateFormat timeFormater = new SimpleDateFormat(format);// 日志内容的时间格式
        return timeFormater.format(new java.util.Date().getTime());
    }

    public static String convertMillisToDateString(long millis, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(new Date(millis));
    }

    public static long getModifyYearString(long dateStr, String format, int plusYearAmount) {
        Date date = convertToDate(convertMillisToDateString(dateStr, "yyyy-MM-dd HH:mm:ss:sss"));
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, plusYearAmount);
        Date resultDate = cal.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        String resultDateStr = dateFormat.format(resultDate);
        return getMillisecondByDateTime(resultDateStr);
    }

    public static Date convertToDate(String dateStr) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date;
    }
}
