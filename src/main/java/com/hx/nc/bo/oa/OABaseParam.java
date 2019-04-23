package com.hx.nc.bo.oa;

import lombok.Data;

import java.io.Serializable;

/**
 * @author XingJiajun
 * @Date 2019/1/5 10:11
 * @Description
 */
@Data
abstract class OABaseParam implements Serializable {
    private static final long serialVersionUID = -7858183524585401449L;
    private String registerCode;//系统注册编码
}
