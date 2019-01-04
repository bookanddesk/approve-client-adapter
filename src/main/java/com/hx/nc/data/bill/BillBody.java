package com.hx.nc.data.bill;

import com.hx.nc.data.annotation.Element;
import lombok.Data;

import java.util.List;

/**
 * @author XingJiajun
 * @Date 2019/1/3 15:14
 * @Description
 */
@Data
public class BillBody {

    @Element(name = "body", type = Element.ElementType.List)
    private List<BillBodyTab> billBodyTabs;

}
