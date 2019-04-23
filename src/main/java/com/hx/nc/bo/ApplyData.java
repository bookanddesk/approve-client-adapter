package com.hx.nc.bo;

import lombok.Builder;
import lombok.Data;
import yonyou.bpm.rest.response.historic.HistoricProcessInstanceResponse;

import java.util.Date;

/**
 * @author XingJiajun
 * @Date 2019/4/23 14:08
 * @Description
 */
@Data
public class ApplyData {
    private HistoricProcessInstanceResponse inst;
    private Date copyToEndTime;
    private String currentUserId;
    private String nodeFormID;

    @Builder
    public ApplyData(HistoricProcessInstanceResponse inst, Date copyToEndTime, String currentUserId, String nodeFormID) {
        this.inst = inst;
        this.copyToEndTime = copyToEndTime;
        this.currentUserId = currentUserId;
        this.nodeFormID = nodeFormID;
    }
}
