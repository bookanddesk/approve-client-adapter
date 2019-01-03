package com.hx.nc.bo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author XingJiajun
 * @Date 2018/12/29 18:04
 * @Description
 */
@Data
public class NCBillDetailParams extends NCTaskBaseParams {
    @NotBlank(message = "billId参数不能为空！")
    private String billId;
    @NotBlank(message = "billtype参数不能为空！")
    private String billtype;
}
