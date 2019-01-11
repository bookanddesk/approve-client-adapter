package com.hx.nc.data.wrap;

import com.hx.nc.data.annotation.Element;
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
    @Element(name = "isPerson", type = Element.ElementType.List)
    private List<String> isPerson;
}
