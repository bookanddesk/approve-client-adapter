package com.hx.nc.data.convert;

import yonyou.bpm.rest.ex.util.DateUtil;

import java.util.Date;

/**
 * @author XingJiajun
 * @Date 2019/1/11 14:54
 * @Description
 */
public class DateSwap extends Date {
    public DateSwap(Date date) {
        super(date.getTime());
    }

    @Override
    public String toString() {
        return DateUtil.toDateString(this);
    }
}
