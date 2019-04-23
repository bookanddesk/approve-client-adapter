package com.hx.nc.data.wrap;

import com.hx.nc.data.annotation.Element;
import lombok.Data;

import java.util.List;

/**
 * @author XingJiajun
 * @Date 2019/4/23 9:30
 * @Description 单据附件
 */
@Data
public class NCAttachGroupData {
    private String attachmentgroupname;
    @Element(name = "attachmentgrouplist", type = Element.ElementType.List)
    private List<NCAttachData> attachmentgrouplist;
}
