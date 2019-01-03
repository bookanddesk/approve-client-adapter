package com.hx.nc.bo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author XingJiajun
 * @Date 2018/12/29 18:02
 * @Description
 */
@Data
public abstract class NCBaseParams implements Serializable {
    @NotBlank(message = "userid参数不能为空！")
    private String userid;
    @NotBlank(message = "groupid参数不能为空！")
    private String groupid;
}
