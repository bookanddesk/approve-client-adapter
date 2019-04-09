package com.hx.nc.service;

import com.google.common.collect.*;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

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

    private String nc65phIp;
    private String nc65phGroupId;
    private String nc65phRegisterCode;
    private String nc65dsIp;
    private String nc65dsGroupId;
    private String nc65dsRegisterCode;

    private ListMultimap<String, String> nc65Properties;

    @PostConstruct
    public void initMultimap() {
        nc65Properties = MultimapBuilder.treeKeys().arrayListValues().build();
        nc65Properties.putAll(groupid, Lists.newArrayList(ip, registerCode));
        nc65Properties.putAll(nc65phGroupId, Lists.newArrayList(nc65phIp, nc65phRegisterCode));
        nc65Properties.putAll(nc65dsGroupId, Lists.newArrayList(nc65dsIp, nc65dsRegisterCode));
    }

    String getNcIp(String groupId) {
        return nc65Properties.get(groupId).get(0);
    }

    public String getNcRegisterCode(String groupId) {
        return nc65Properties.get(groupId).get(1);
    }

    public ListMultimap<String, String> getNc65Properties() {
        return nc65Properties;
    }
}
