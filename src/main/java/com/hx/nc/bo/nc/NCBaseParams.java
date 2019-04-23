package com.hx.nc.bo.nc;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author XingJiajun
 * @Date 2018/12/29 18:02
 * @Description
 */
@Data
abstract class NCBaseParams implements Serializable {
    private static final long serialVersionUID = -3811145631837878794L;
    @NotBlank(message = "userid参数不能为空！")
    private String userid;
    @NotBlank(message = "groupid参数不能为空！")
    private String groupid;
}
