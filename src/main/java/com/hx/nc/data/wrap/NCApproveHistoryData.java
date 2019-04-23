package com.hx.nc.data.wrap;

import com.hx.nc.data.annotation.Element;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author XingJiajun
 * @Date 2019/1/3 15:44
 * @Description
 */
@Data
public class NCApproveHistoryData extends PojoMapSupport{
    private String note;
    private String approvedid;
    private String psnid;
    private String count;
    private Date handledate;
    private String action;
    private String handlername;
    private String mark;
    @Element(name = "attachstructlist", type = Element.ElementType.List)
    private List<NCAttachData> attachstructlist;
}
