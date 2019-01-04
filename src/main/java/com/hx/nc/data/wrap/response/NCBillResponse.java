package com.hx.nc.data.wrap.response;

import com.hx.nc.data.annotation.Element;
import com.hx.nc.data.wrap.NCDataResultBase;
import lombok.Data;

/**
 * @author XingJiajun
 * @Date 2019/1/4 15:29
 * @Description
 */
@Data
public class NCBillResponse<T extends NCDataResultBase> extends NCBaseResponse{
    @Element(name = "taskbill", type = Element.ElementType.Complex)
    private T dataResult;
}
