package com.hx.nc.bo.oa;

import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * @author XingJiajun
 * @Date 2019/3/30 11:02
 * @Description
 */
@Data
public class OAUserInfo {
    private Map<String, OAUser> map;
}
