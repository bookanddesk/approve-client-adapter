package com.hx.nc.data.wrap.response;

import com.hx.nc.data.annotation.Element;
import com.hx.nc.data.wrap.NCApproveHistoryData;
import com.hx.nc.data.wrap.NCApproveHistoryDataAdapter;
import com.hx.nc.data.wrap.NCFlowHistoryData;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author XingJiajun
 * @Date 2019/1/3 15:52
 * @Description
 */
@Data
public class NCApproveDetailResponse extends NCBaseResponse {
    private String billname;
    private String psnid;
    private String billtypename;
    private Date submitdate;
    private String makername;
    @Element(name = "approvehistorylinelist", type = Element.ElementType.List)
    private List<NCApproveHistoryDataAdapter> approvehistorylinelist;
}
