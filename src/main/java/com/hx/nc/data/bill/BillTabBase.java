package com.hx.nc.data.bill;

import com.hx.nc.data.annotation.Element;
import lombok.Data;

/**
 * @author XingJiajun
 * @Date 2019/1/3 15:03
 * @Description
 */
@Data
public class BillTabBase {

    @Element(name = "tabCode")
    private String code;

    @Element(name = "tabName")
    private String name;

}
