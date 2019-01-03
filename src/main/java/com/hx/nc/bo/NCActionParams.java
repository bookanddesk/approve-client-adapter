package com.hx.nc.bo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author XingJiajun
 * @Date 2018/12/29 18:04
 * @Description
 */
@Data
public class NCActionParams extends NCTaskBaseParams {
    @NotNull(message = "action参数不能为空！")
    @Pattern(regexp = "agree|reject|disagree", message = "action参数不合法！")
    private String action;
    private String approveMessage;
    private String billId;
    private String billtype;
    private String billtypename;
    private String cuserids;
}
