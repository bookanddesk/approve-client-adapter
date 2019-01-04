package com.hx.nc.data.wrap;

import com.hx.nc.data.annotation.Element;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author XingJiajun
 * @Date 2019/1/3 15:29
 * @Description
 */
@Data
public class NCApproveDetailData extends NCDataResultBase{
    private String billname;
    private String psnid;
    private String billtypename;
    private Date submitdate;
    private String makername;
    @Element(name = "flowhistory", type = Element.ElementType.List)
    private List<NCFlowHistoryData> flowhistory;
    @Element(name = "approvehistorylinelist", type = Element.ElementType.List)
    private List<NCApproveHistoryData> approvehistorylinelist;
}
