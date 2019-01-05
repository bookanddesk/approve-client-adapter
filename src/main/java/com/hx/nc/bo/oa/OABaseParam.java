package com.hx.nc.bo.oa;

import lombok.Data;

import java.io.Serializable;

/**
 * @author XingJiajun
 * @Date 2019/1/5 10:11
 * @Description
 */
@Data
public abstract class OABaseParam implements Serializable {
    private String registerCode;//系统注册编码
}
