package com.hx.nc.bo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author XingJiajun
 * @Date 2018/12/29 18:02
 * @Description
 */
@Data
public class NCTaskBaseParams extends NCBaseParams {
    @NotBlank(message = "taskId参数不能为空！")
    private String taskId;
}
