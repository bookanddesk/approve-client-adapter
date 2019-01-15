package com.hx.nc.data.Fields;

import lombok.Data;

/**
 * @author XingJiajun
 * @Date 2019/1/14 20:24
 * @Description
 */
@Data
public class DateField {
    private String day;
    private String daysMonth;
    private String enMonth;
    private String enWeek;
    private String leapYear;
    private String localDay;
    private String localMonth;
    private String localYear;
    private String millis;
    private String month;
    private String strDay;
    private String strMonth;
    private String week;
    private String weekOfYear;
    private String year;

    @Override
    public String toString() {
        return year + "-" + month + "-" + day;
    }
}
