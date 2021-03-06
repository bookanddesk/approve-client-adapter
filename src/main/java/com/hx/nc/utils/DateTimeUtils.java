package com.hx.nc.utils;

import com.hx.nc.bo.Constants;

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
    private static final String FMT_DATETIME = "yyyy-MM-dd HH:mm:ss";

//    public static Date now() {
//        return Date.from(Instant.now());
//    }

//    public String fmtTime(LocalTime time) {
//        return time.format(DateTimeFormatter.ISO_LOCAL_TIME);
//    }

    public static String fmtDateTime(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern(FMT_DATETIME));
    }

    public static String defaultPollDateTime() {
        return fmtDateTime(LocalDateTime.now().minusMinutes(Constants.LAST_POLL_DURATION_MINUTES));
    }
    public static String defaultPollingRecordBefore() {
        return fmtDateTime(LocalDateTime.now().minusDays(1));
    }

    public static String now() {
        return fmtDateTime(LocalDateTime.now());
    }



}
