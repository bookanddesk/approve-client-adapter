package com.hx.nc.data.wrap.response;

import com.hx.nc.data.annotation.Element;
import com.hx.nc.data.wrap.NCDataResultBase;
import lombok.Data;

/**
 * @author XingJiajun
 * @Date 2019/1/3 15:49
 * @Description
 */
@Data
public class NCResponse<T extends NCDataResultBase> {
    private String msg;
    private String code;
    private String actionid;
    private String callback;
    @Element(name = "resultctx", type = Element.ElementType.Complex)
    private T dataResult;
}
