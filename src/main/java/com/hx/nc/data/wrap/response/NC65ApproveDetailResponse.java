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
public class NC65ApproveDetailResponse extends NCBaseResponse {
    @Element(name = "flowhistory", type = Element.ElementType.List)
    private List<NCFlowHistoryData> flowhistory;
    @Element(name = "approvehistorylinelist", type = Element.ElementType.List)
    private List<NCApproveHistoryData> approvehistorylinelist;
}
