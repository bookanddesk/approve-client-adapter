package com.hx.nc.data.wrap;

import com.hx.nc.data.annotation.Element;
import lombok.Data;

import java.util.List;

/**
 * @author XingJiajun
 * @Date 2019/1/11 14:10
 * @Description
 */
@Data
public class NCApproveHistoryDataAdapter {
    @Element(name = "flowhistory", type = Element.ElementType.List)
    private List<NCFlowHistoryData> flowhistory;
    @Element(name = "approvehistorylinelist", type = Element.ElementType.List)
    private List<NCApproveHistoryData> approvehistorylinelist;
}
