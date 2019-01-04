package com.hx.nc.data.wrap;

import lombok.Data;

import java.util.List;

/**
 * @author XingJiajun
 * @Date 2019/1/3 15:35
 * @Description
 */
@Data
public class NCPersonData extends PojoMapSupport {
    private String id;
    private String name;
    private List<String> isPerson;
}
