package com.hx.nc.data.wrap.response;

import com.hx.nc.data.annotation.Element;
import com.hx.nc.data.wrap.NCAttachData;
import lombok.Data;

import java.util.List;

/**
 * @author XingJiajun
 * @Date 2019/1/11 15:51
 * @Description
 */
@Data
public class NCAttachmentResponse extends NCBaseResponse{
    @Element(name = "attachstructlist", type = Element.ElementType.List)
    private List<NCAttachData> dataResult;
}
