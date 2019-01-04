package com.hx.nc.data.wrap;

import com.hx.nc.data.annotation.Element;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author XingJiajun
 * @Date 2019/1/3 15:36
 * @Description
 */
@Data
public class NCFlowHistoryData extends PojoMapSupport {
    private String unittype;
    private Date time;
    private String remindflag;
    private String mark;
    @Element(name = "personlist", type = Element.ElementType.List)
    private List<NCPersonData> personlist;
    @Element(name = "itemlist", type = Element.ElementType.List)
    private List<NCItemData> itemlist;
}
