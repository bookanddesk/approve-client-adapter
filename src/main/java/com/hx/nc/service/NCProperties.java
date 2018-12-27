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
    private String ip;
    private String userid;
    private String groupid;
}
