package com.hx.nc.service;

import com.hx.nc.bo.nc.NCBillDetailParams;
import com.hx.nc.data.bpm.Attachment;
import yonyou.bpm.rest.response.historic.HistoricProcessInstanceResponse;
import yonyou.bpm.rest.response.historic.HistoricTaskInstanceResponse;

import java.util.List;

/**
 * @author XingJiajun
 * @Date 2019/1/11 15:39
 * @Description
 */
public interface IBPMDataConvertService {
    HistoricProcessInstanceResponse resolve2BpmApproveDetail(String jsonStr, NCBillDetailParams params);

    List<HistoricTaskInstanceResponse> resolve2BPMHisTasks(String jsonStr, NCBillDetailParams params);

    List<Attachment> resolve2BPMAttachments(String jsonStr);

    List<Attachment> resolveAttachFromApproveDetail(String jsonStr);

    byte[] resolveNCFileData(String jsonStr);
}
