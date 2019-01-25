package com.hx.nc.bo.oa;

import lombok.*;

/**
 * @author XingJiajun
 * @Date 2019/1/18 13:06
 * @Description
 */
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@ToString(onlyExplicitlyIncluded = true)
public class OARestErrorMSg {
    private String errorCode;
    private String errorType;
    @ToString.Include(name = "err")
    private String errorDetail;

    @Builder
    public OARestErrorMSg(String errorDetail) {
        this.errorDetail = errorDetail;
    }
}
