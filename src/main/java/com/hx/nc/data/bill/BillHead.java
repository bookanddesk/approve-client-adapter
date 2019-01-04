package com.hx.nc.data.bill;

import com.hx.nc.data.annotation.Element;
import lombok.Data;

import java.util.List;

/**
 * @author XingJiajun
 * @Date 2019/1/3 15:12
 * @Description
 */
@Data
public class BillHead {

    @Element(name = "head",
            type = Element.ElementType.List,
            source = Element.ElementSource.Parent)
    private List<BillHeadTab> billTabs;

}
