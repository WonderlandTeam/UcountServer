package cn.edu.nju.wonderland.ucountserver.util;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by green-cherry on 2017/8/23.
 */
public class DateHelper {

    /**
     * 标准日期格式器
     */
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * 标准日期-时间格式器
     */
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 标准年份-月份格式器
     */
    public static final DateTimeFormatter YEAR_MONTH_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");


    /**
     * 将数据库时间转化为年月的形式
     *
     * @param time      数据库时间
     * @return          年月格式字符串
     */
    public static String toMonthByTimeStamp(Timestamp time) {
        return time.toLocalDateTime().format(YEAR_MONTH_FORMATTER);
    }


    /**
     * 将数据库时间转化为年月日的形式
     *
     * @param time      数据库时间
     * @return          日期格式字符串
     */
    public static String toDateByTimeStamp(Timestamp time) {
        return time.toLocalDateTime().format(DATE_FORMATTER);
    }

    /**
     * 将数据库时间转化为年月日时分秒的形式
     *
     * @param time      数据库时间
     * @return          日期时间格式字符串
     */
    public static String toTimeByTimeStamp(Timestamp time) {
        return time.toLocalDateTime().format(DATE_TIME_FORMATTER);
    }

    /**
     * 将年月转化为数据库时间
     *
     * @param time      年月格式字符串
     * @return          数据库时间
     */
    public static Timestamp toTimestampByMonth(String time) {
        time += "-01 00:00:00";
        return Timestamp.valueOf(time);
    }

    /**
     * 获得当前的时间，格式为日期时间格式
     */
    public static String getToday() {
        LocalDateTime localDateTime = LocalDateTime.now();
        return localDateTime.format(DATE_TIME_FORMATTER);
    }

    /**
     * 获得当前的日期，格式为日期格式
     */
    public static String getTodayDate(){
        LocalDateTime localDateTime = LocalDateTime.now();
        return localDateTime.format(DATE_FORMATTER);
    }

    /**
     * 获得昨天的时间，格式为日期时间格式
     */
    public static String getYesterday(){
        Date date=Date.valueOf(getTodayDate());
        long i=date.getTime();
        i-=1000;
        date=new Date(i);
        return date.toString()+" 23:59:59";
    }

}
