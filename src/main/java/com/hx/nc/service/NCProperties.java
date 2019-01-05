package com.hx.nc.service;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author XingJiajun
 * @Date 2018/12/27 13:55
 * @Description
 */
@Component
@Data
@ConfigurationProperties(prefix = "hx.nc")
public class NCProperties {
    private String host;//部署服务器地址
    private String ip;//nc服务地址
    private String userid;//nc管理员用户
    private String groupid;//nc企业
    private String oaIP;//oa服务地址
    private String registerCode;//oa注册系统编码 应用编号
    private String oaUser;//oa 用户名
    private String oaPwd;//oa 密码
    private String filePath;
}
