package com.hx.nc.data.bill;

import com.hx.nc.data.annotation.Element;
import lombok.Data;

import java.util.List;

/**
 * @author XingJiajun
 * @Date 2019/1/3 15:05
 * @Description
 */
@Data
public class BillHeadContent {

    @Element(name = "billItemData", type = Element.ElementType.List)
    private List<BillItem> items;

}
