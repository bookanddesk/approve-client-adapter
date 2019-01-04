package com.hx.nc.data.wrap.response;

import com.hx.nc.data.wrap.NCBillData;
import com.hx.nc.data.wrap.NCTaskBillData;
import lombok.Data;

/**
 * @author XingJiajun
 * @Date 2019/1/3 16:09
 * @Description
 */
@Data
public class NCTaskBillResponse extends NCBillResponse<NCBillData> {
    private String billtypename;
}
