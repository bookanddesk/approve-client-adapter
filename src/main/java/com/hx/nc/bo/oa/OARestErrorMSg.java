package com.hx.nc.bo.oa;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author XingJiajun
 * @Date 2019/1/18 13:06
 * @Description
 */
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
public class OARestErrorMSg {
    private String errorCode;
    private String errorType;
    private String errorDetail;

    @Builder
    public OARestErrorMSg(String errorDetail) {
        this.errorDetail = errorDetail;
    }
}
