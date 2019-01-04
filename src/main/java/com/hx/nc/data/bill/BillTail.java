package com.hx.nc.data.bill;

import com.hx.nc.data.annotation.Element;
import lombok.Data;

import java.util.List;

/**
 * @author XingJiajun
 * @Date 2019/1/3 15:19
 * @Description
 */
@Data
public class BillTail {

    @Element(name = "tail",
            type = Element.ElementType.List,
            source = Element.ElementSource.Parent)
    private List<BillTailTab> billTailTab;

}
