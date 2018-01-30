package com.ifuture.adonline.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具
 *
 * @author leiyi
 */
public class DateTools {

    public static final String FORMAT_YMD = "yyyy-MM-dd";
    public static final String FORMAT_YM = "yyyy-MM";
    public static final String FORMAT_Y = "yyyy";

    public static final String FORMAT_YMDHMS = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_YMDHM = "yyyy-MM-dd HH:mm";
    public static final String FORMAT_YMDH = "yyyy-MM-dd HH";

    public static final String FORMAT_HMS = "HH:mm:ss";
    public static final String FORMAT_HM = "HH:mm";
    public static final String FORMAT_H = "HH";

    public static final long ONE_DAY_TIMES = 24 * 60 * 60 * 1000;

    /**
     * 得到当天日期 格式化：yyyy-MM-dd
     */
    public static String todayStr() {
        return todayStr(FORMAT_YMD);
    }

    /**
     * 得到当天日期
     */
    public static String todayStr(String format) {
        return dateToStr(new Date(), format);
    }

    /**
     * 得到当天日期 格式化：yyyy-MM-dd
     */
    public static Date todayDate() {
        return todayDate(FORMAT_YMD);
    }

    /**
     * 得到当天日期
     */
    public static Date todayDate(String format) {
        return dateToDate(new Date(), format);
    }

    /**
     * 日期转换字符串 格式化：yyyy-MM-dd
     */
    public static String dateToStr(Date date) {
        return dateToStr(date, FORMAT_YMD);
    }

    /**
     * 日期转换字符串
     */
    public static String dateToStr(Date date, String format) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat fmt = new SimpleDateFormat(format);
        return fmt.format(date);
    }

    /**
     * 字符串转换日期 格式化：yyyy-MM-dd
     */
    public static Date strToDate(String sdate) {
        return strToDate(sdate, FORMAT_YMD);
    }

    /**
     * 字符串转换日期
     */
    public static Date strToDate(String sdate, String format) {
        if (isEmpty(sdate)) {
            return null;
        }
        SimpleDateFormat fmt = new SimpleDateFormat(format);
        try {
            return fmt.parse(sdate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 日期转换日期 格式化：yyyy-MM-dd
     */
    public static Date dateToDate(Date date) {
        return dateToDate(date, FORMAT_YMD);
    }

    /**
     * 日期转换日期
     */
    public static Date dateToDate(Date date, String format) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat fmt = new SimpleDateFormat(format);
        try {
            return fmt.parse(fmt.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 计算两个日期之间的天数
     */
    public static int between(String beginDate, String endDate) {
        if (isEmpty(beginDate) || isEmpty(endDate)) {
            return 0;
        }
        return (int) ((strToDate(endDate).getTime() - strToDate(beginDate)
            .getTime()) / ONE_DAY_TIMES + 1);
    }

    /**
     * 计算两个日期之间的天数
     */
    public static int between(Date beginDate, Date endDate) {
        if (beginDate == null || endDate == null) {
            return 0;
        }
        return (int) ((dateToDate(endDate).getTime() - dateToDate(beginDate)
            .getTime()) / ONE_DAY_TIMES + 1);
    }

    /**
     * 计算两个日期之间的相差月份数
     */
    public static int betweenMonths(Date beginDate, Date endDate) {
        int days = 1;// 两个日期之前的月份数
        Calendar beginCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();

        beginCalendar.setTime(getMonthFirstDay(beginDate));
        endCalendar.setTime(getMonthFirstDay(endDate));
        // 计算月份数
        while (beginCalendar.before(endCalendar)) {
            days++;
            beginCalendar.add(Calendar.MONTH, 1);
        }
        return days;
    }

    /**
     * 计算两个日期之间的相差月份数
     */
    public static int betweenMonths(String beginDate, String endDate) {
        int days = 1;// 两个日期之前的月份数
        Calendar beginCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();

        beginCalendar.setTime(getMonthFirstDay(beginDate));
        endCalendar.setTime(getMonthFirstDay(endDate));
        // 计算月份数
        while (beginCalendar.before(endCalendar)) {
            days++;
            beginCalendar.add(Calendar.MONTH, 1);
        }
        return days;
    }

    public static String getDateBetween(String t, Date beginDate, Date endDate) {
        int day = between(beginDate, endDate);
        return t + dateToStr(beginDate) + " 至 " + dateToStr(endDate) + " 共("
            + day + ")天";
    }

    public static String getDateBetween(String t, String beginDate,
        String endDate) {
        int day = between(strToDate(beginDate), strToDate(endDate));
        return t + beginDate + " 至 " + endDate + " 共(" + day + ")天";
    }

    public static String getDateBetweenErr(String d1, String d2) {
        if (d1.compareTo(d2) > 0) {
            return "开始日期不能大于结束日期！";
        }
        if (d2.compareTo(todayStr()) > 0) {
            return "结束日期不能大于当天日期!";
        }
        return "1";
    }

    public static String getDateBetweenErr(Date d1, Date d2) {
        return getDateBetweenErr(dateToStr(d1), dateToStr(d2));
    }

    /**
     * 根据日期得到年份第一天
     */
    public static Date getYearFirstDay(Date date) {
        return dateToDate(date, "yyyy-01-01");
    }

    /**
     * 根据日期得到年份第一天
     */
    public static Date getYearFirstDay(String date) {
        return strToDate(date.substring(0, 4) + "-01-01");
    }

    /**
     * 根据日期得到年份第一天
     */
    public static String getYearFirstDayStr(Date date) {
        return dateToStr(date, "yyyy-01-01");
    }

    /**
     * 根据日期得到年份第一天
     */
    public static String getYearFirstDayStr(String date) {
        return date.substring(0, 4) + "-01-01";
    }

    /**
     * 根据日期得到年份最后一天
     */
    public static Date getYearLastDay(Date date) {
        return dateToDate(date, "yyyy-12-31");
    }

    /**
     * 根据日期得到年份最后一天
     */
    public static Date getYearLastDay(String date) {
        return strToDate(date.substring(0, 4) + "-12-31");
    }

    /**
     * 根据日期得到年份最后一天
     */
    public static String getYearLastDayStr(Date date) {
        return dateToStr(date, "yyyy-12-31");
    }

    /**
     * 根据日期得到年份最后一天
     */
    public static String getYearLastDayStr(String date) {
        return date.substring(0, 4) + "-12-31";
    }

    /**
     * 根据日期得到月份第一天
     */
    public static Date getMonthFirstDay(Date date) {
        return dateToDate(date, "yyyy-MM-01");
    }

    /**
     * 根据日期得到月份第一天
     */
    public static Date getMonthFirstDay(String date) {
        return strToDate(date, "yyyy-MM-01");
    }

    /**
     * 根据日期得到月份第一天
     */
    public static String getMonthFirstDayStr(Date date) {
        return dateToStr(date, "yyyy-MM-01");
    }

    /**
     * 根据日期得到月份第一天
     */
    public static String getMonthFirstDayStr(String date) {
        return date.substring(0, 7) + "-01";
    }

    /**
     * 根据日期得到月份最后一天
     */
    public static Date getMonthLastDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getMonthFirstDay(date));
        cal.add(Calendar.MONTH, 1);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return cal.getTime();
    }

    /**
     * 根据日期得到月份最后一天
     */
    public static Date getMonthLastDay(String date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getMonthFirstDay(date));
        cal.add(Calendar.MONTH, 1);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return cal.getTime();
    }

    /**
     * 根据日期得到月份最后一天
     */
    public static String getMonthLastDayStr(Date date) {
        return dateToStr(getMonthLastDay(date));
    }

    /**
     * 根据日期得到月份最后一天
     */
    public static String getMonthLastDayStr(String date) {
        return dateToStr(getMonthLastDay(date));
    }

    /**
     * 根据日期得到上月份第一天
     */
    public static Date getPrMonthFirstDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getMonthFirstDay(date));
        cal.add(Calendar.MONTH, -1);
        return cal.getTime();
    }

    /**
     * 根据日期得到上月份第一天
     */
    public static Date getPrMonthFirstDay(String date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getMonthFirstDay(date));
        cal.add(Calendar.MONTH, -1);
        return cal.getTime();
    }

    /**
     * 根据日期得到上月份第一天
     */
    public static String getPrMonthFirstDayStr(Date date) {
        return dateToStr(getPrMonthFirstDay(date));
    }

    /**
     * 根据日期得到上月份第一天
     */
    public static String getPrMonthFirstDayStr(String date) {
        return dateToStr(getPrMonthFirstDay(date));
    }

    /**
     * 根据日期得到上月份最后一天
     */
    public static Date getPrMonthLastDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getMonthFirstDay(date));
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return cal.getTime();
    }

    /**
     * 根据日期得到上月份最后一天
     */
    public static Date getPrMonthLastDay(String date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getMonthFirstDay(date));
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return cal.getTime();
    }

    /**
     * 根据日期得到上月份最后一天
     */
    public static String getPrMonthLastDayStr(Date date) {
        return dateToStr(getPrMonthLastDay(date));
    }

    /**
     * 根据日期得到上月份最后一天
     */
    public static String getPrMonthLastDayStr(String date) {
        return dateToStr(getPrMonthLastDay(date));
    }

    /**
     * 根据日期得到上一年本月份第一天
     *
     * 去年同期第一天
     */
    public static Date getPrYearMonthFirstDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getMonthFirstDay(date));
        cal.add(Calendar.MONTH, -12);
        return cal.getTime();
    }

    /**
     * 根据日期得到上一年本月份第一天
     *
     * 去年同期第一天
     */
    public static Date getPrYearMonthFirstDay(String date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getMonthFirstDay(date));
        cal.add(Calendar.MONTH, -12);
        return cal.getTime();
    }

    /**
     * 根据日期得到上一年本月份第一天
     *
     * 去年同期第一天
     */
    public static String getPrYearMonthFirstDayStr(Date date) {
        return dateToStr(getPrYearMonthFirstDay(date));
    }

    /**
     * 根据日期得到上一年本月份第一天
     *
     * 去年同期第一天
     */
    public static String getPrYearMonthFirstDayStr(String date) {
        return dateToStr(getPrYearMonthFirstDay(date));
    }

    /**
     * 根据日期得到上一年本月份最后一天
     *
     * 去年同期最后一天
     */
    public static Date getPrYearMonthLastDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getMonthFirstDay(date));
        cal.add(Calendar.MONTH, -11);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return cal.getTime();
    }

    /**
     * 根据日期得到上一年本月份最后一天
     *
     * 去年同期最后一天
     */
    public static Date getPrYearMonthLastDay(String date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getMonthFirstDay(date));
        cal.add(Calendar.MONTH, -11);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return cal.getTime();
    }

    /**
     * 根据日期得到上一年本月份最后一天
     *
     * 去年同期最后一天
     */
    public static String getPrYearMonthLastDayStr(Date date) {
        return dateToStr(getPrYearMonthLastDay(date));
    }

    /**
     * 根据日期得到上一年本月份最后一天
     *
     * 去年同期最后一天
     */
    public static String getPrYearMonthLastDayStr(String date) {
        return dateToStr(getPrYearMonthLastDay(date));
    }

    /**
     * 上一期的第一天
     */
    public static Date getPrMonthFirstDay(Date date1, Date date2) {
        int mos = betweenMonths(date1, date2);
        Calendar cal = Calendar.getInstance();
        cal.setTime(getMonthFirstDay(date1));
        cal.add(Calendar.MONTH, -1 * mos);
        return cal.getTime();
    }

    /**
     * 上一期的第一天
     */
    public static Date getPrMonthFirstDay(String date1, String date2) {
        int mos = betweenMonths(date1, date2);
        Calendar cal = Calendar.getInstance();
        cal.setTime(getMonthFirstDay(date1));
        cal.add(Calendar.MONTH, -1 * mos);
        return cal.getTime();
    }

    /**
     * 上一期的第一天
     */
    public static String getPrMonthFirstDayStr(Date date1, Date date2) {
        return dateToStr(getPrMonthFirstDay(date1, date2));
    }

    /**
     * 上一期的第一天
     */
    public static String getPrMonthFirstDayStr(String date1, String date2) {
        return dateToStr(getPrMonthFirstDay(strToDate(date1), strToDate(date2)));
    }

    /**
     * 日期加上天数
     */
    public static Date addDay(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, days);
        return cal.getTime();
    }

    /**
     * 日期加上天数
     */
    public static Date addDay(String date, int days) {
        return addDay(strToDate(date), days);
    }

    /**
     * 日期加上天数
     */
    public static String addDayStr(Date date, int days) {
        return dateToStr(addDay(date, days));
    }

    /**
     * 日期加上天数
     */
    public static String addDayStr(String date, int days) {
        return dateToStr(addDay(date, days));
    }

    /**
     * 日期加上月份数
     */
    public static Date addMonth(Date date, int months) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, months);
        return cal.getTime();
    }

    /**
     * 日期加上月份数
     */
    public static Date addMonth(String date, int months) {
        return addMonth(strToDate(date), months);
    }

    /**
     * 日期加上月份数
     */
    public static String addMonthStr(Date date, int months) {
        return dateToStr(addMonth(date, months));
    }

    /**
     * 日期加上月份数
     */
    public static String addMonthStr(String date, int months) {
        return dateToStr(addMonth(strToDate(date), months));
    }

    public static boolean isEmpty(String s) {
        if (s == null || s.equals("")) {
            return true;
        }
        return false;
    }
}
