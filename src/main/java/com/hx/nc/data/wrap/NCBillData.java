package com.hx.nc.data.wrap;

import com.hx.nc.data.annotation.Element;
import com.hx.nc.data.bill.BillBody;
import com.hx.nc.data.bill.BillHead;
import com.hx.nc.data.bill.BillTail;
import lombok.Data;

/**
 * @author XingJiajun
 * @Date 2019/1/3 16:04
 * @Description
 */
@Data
public class NCBillData {

    @Element(name = "head",
            type = Element.ElementType.Complex,
            source = Element.ElementSource.Parent)
    private BillHead head;

    @Element(name = "body",
            type = Element.ElementType.Complex,
            source = Element.ElementSource.Parent)
    private BillBody body;

    @Element(name = "tail",
            type = Element.ElementType.Complex,
            source = Element.ElementSource.Parent)
    private BillTail tail;

}
