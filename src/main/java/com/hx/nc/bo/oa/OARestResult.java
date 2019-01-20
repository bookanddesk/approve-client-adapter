package com.hx.nc.bo.oa;

import lombok.*;

import java.util.List;

/**
 * @author XingJiajun
 * @Date 2019/1/18 13:06
 * @Description
 */
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
public class OARestResult {
    private boolean success;
    private List<OARestErrorMSg> errorMsgs;

    @Builder
    public OARestResult(boolean success, @Singular List<OARestErrorMSg> errorMsgs) {
        this.success = success;
        this.errorMsgs = errorMsgs;
    }
}
