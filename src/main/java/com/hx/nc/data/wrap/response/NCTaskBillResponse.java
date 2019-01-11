package com.hx.nc.data.wrap.response;

import com.hx.nc.data.annotation.Element;
import com.hx.nc.data.wrap.NCBillData;
import com.hx.nc.data.wrap.NCTaskBillData;
import lombok.Data;

/**
 * @author XingJiajun
 * @Date 2019/1/3 16:09
 * @Description
 */
@Data
public class NCTaskBillResponse extends NCBaseResponse {
    private String billtypename;
    @Element(name = "taskbill", type = Element.ElementType.Complex)
    private NCBillData dataResult;
}
