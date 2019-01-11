package com.hx.nc.data.wrap;

import com.hx.nc.data.annotation.Element;
import lombok.Data;

/**
 * @author XingJiajun
 * @Date 2019/1/3 16:07
 * @Description
 */
@Data
public class NCTaskBillData {

    @Element(name = "taskbill", type = Element.ElementType.Complex)
    private NCBillData dataResult;

}
