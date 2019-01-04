package com.hx.nc.data.bill;

import com.hx.nc.data.annotation.Element;
import lombok.Data;

/**
 * @author XingJiajun
 * @Date 2019/1/3 15:18
 * @Description
 */
@Data
public class BillTailTab extends BillTabBase {

    @Element(name = "tabContent", type = Element.ElementType.Complex)
    private BillTailContent content;

}
