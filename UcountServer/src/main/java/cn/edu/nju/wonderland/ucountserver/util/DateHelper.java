package cn.edu.nju.wonderland.ucountserver.util;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by green-cherry on 2017/8/23.
 */
public class DateHelper {



    /**
     * 将数据库时间转化为年月的形式
     * @param time
     * @return
     */
    public static String toMonthByTimeStamp(Timestamp time){
        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("yyyy-MM");
        return time.toLocalDateTime().format(monthFormatter);
    }


    /**
     * 将数据库时间转化为年月日的形式
     * @param time
     * @return
     */
    public static String toDateByTimeStamp(Timestamp time){
        DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return time.toLocalDateTime().format(DATE_FORMATTER);
    }

    /**
     * 将数据库时间转化为年月日时分秒的形式
     * @param time
     * @return
     */
    public static String toTimeByTimeStamp(Timestamp time){

        DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return time.toLocalDateTime().format(DATE_TIME_FORMATTER);
    }

    /**
     * 将年月转化为数据库时间
     * @param time
     * @return
     */
    public static Timestamp toTimestampByMonth(String time){
        time+="-1 00:00:00";
        return Timestamp.valueOf(time);
    }

    /**
     * 获得今天的时间
     * @return
     */
    public static String getToday(){
        DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime=LocalDateTime.now();
        return localDateTime.format(DATE_TIME_FORMATTER);
    }
}
