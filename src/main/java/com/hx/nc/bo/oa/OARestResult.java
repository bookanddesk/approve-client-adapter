package com.hx.nc.bo.oa;

import lombok.Data;

import java.util.List;

/**
 * @author XingJiajun
 * @Date 2019/1/18 13:06
 * @Description
 */
@Data
public class OARestResult {
    private boolean success;
    private List<OARestErrorMSg> errorMsgs;
}
