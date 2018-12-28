package com.hx.nc.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author XingJiajun
 * @Date 2018/12/28 9:53
 * @Description
 */
public class DateTimeUtils {

    /**
     * 日期格式：yyyy-MM-dd HH:mm:ss
     */
    public static final String FMT_DATETIME = "yyyy-MM-dd HH:mm:ss";

//    public static Date now() {
//        return Date.from(Instant.now());
//    }

//    public String fmtTime(LocalTime time) {
//        return time.format(DateTimeFormatter.ISO_LOCAL_TIME);
//    }

    public static String fmtDateTime(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern(FMT_DATETIME));
    }

    public static String halfHourBefore() {
        return fmtDateTime(LocalDateTime.now().minusMinutes(30));
    }

    public static String now() {
        return fmtDateTime(LocalDateTime.now());
    }



}
