package com.hx.nc.bo.oa;

import lombok.Data;

/**
 * @author XingJiajun
 * @Date 2019/1/18 13:06
 * @Description
 */
@Data
public class OARestErrorMSg {
    private String errorCode;
    private String errorType;
    private String errorDetail;
}
