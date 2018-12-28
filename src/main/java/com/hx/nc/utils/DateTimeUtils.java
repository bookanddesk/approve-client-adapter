package com.hx.nc.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author XingJiajun
 * @Date 2018/12/28 9:53
 * @Description
 */
public class DateTimeUtils {

    public static Date now() {
        return Date.from(Instant.now());
    }

    public String fmtTime(LocalTime time) {
        return time.format(DateTimeFormatter.ISO_LOCAL_TIME);
    }

    public String fmtDateTime(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }



}
