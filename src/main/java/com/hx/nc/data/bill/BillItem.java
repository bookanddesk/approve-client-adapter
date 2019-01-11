package com.hx.nc.data.bill;

import com.hx.nc.data.annotation.Element;
import lombok.Data;

/**
 * @author XingJiajun
 * @Date 2019/1/3 10:54
 * @Description
 */
@Data
public class BillItem {
    @Element(nameRule = "com.hx.nc.data.name.BillItemNameRule")
    private String showName;

    @Element(nameRule = "com.hx.nc.data.name.BillItemNameRule")
    private String showValue;

    @Element(nameRule = "com.hx.nc.data.name.BillItemNameRule")
    private String showValueId;

    @Element(nameRule = "com.hx.nc.data.name.BillItemNameRule",
            source = Element.ElementSource.BaseFieldName)
    private String sourceFieldName;

    private Boolean digest;

}
