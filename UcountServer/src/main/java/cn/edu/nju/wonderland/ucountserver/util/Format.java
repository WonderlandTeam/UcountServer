package cn.edu.nju.wonderland.ucountserver.util;

import java.time.format.DateTimeFormatter;

public class Format {

    /**
     * 标准日期格式器
     */
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * 标准日期-时间格式器
     */
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

}
