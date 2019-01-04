package com.hx.nc.data.bill;

import com.hx.nc.data.annotation.Element;
import lombok.Data;

import java.util.List;

/**
 * @author XingJiajun
 * @Date 2019/1/3 15:15
 * @Description
 */
@Data
public class BillTailContent {

    @Element(name = "billItemData", type = Element.ElementType.List)
    private List<BillItem> items;

    @Element(name = "group", type = Element.ElementType.List)
    private List<BillHeadTab> group;

}
