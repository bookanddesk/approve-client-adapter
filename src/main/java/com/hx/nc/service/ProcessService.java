package com.hx.nc.service;

import com.google.common.base.Charsets;
import com.hx.nc.bo.ACAEnums;
import com.hx.nc.bo.Constant;
import com.hx.nc.bo.nc.NCActionParams;
import com.hx.nc.bo.nc.NCBillDetailParams;
import com.hx.nc.bo.nc.NCFileDataParams;
import com.hx.nc.bo.nc.NCTaskBaseParams;
import com.hx.nc.data.bpm.Attachment;
import com.hx.nc.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import yonyou.bpm.rest.response.historic.HistoricProcessInstanceResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.hx.nc.bo.Constant.CONTENT_DISPOSITION_TYPE_FORM_DATA;
import static com.hx.nc.bo.Constant.NC_RESPONSE_FLAG;

/**
 * @author XingJiajun
 * @Date 2018/12/26 9:58
 * @Description
 */
@Service
public class ProcessService extends BaseService {

    private final NCService ncService;
    private final OAService oaService;
    private final BPMDataConvertService bpmDataConvertService;

    @Autowired
    public ProcessService(NCService ncService, OAService oaService, BPMDataConvertService bpmDataConvertService) {
        this.ncService = ncService;
        this.oaService = oaService;
        this.bpmDataConvertService = bpmDataConvertService;
    }

    private String getNCBillDetailData(NCBillDetailParams params) {
        return ncService.getNCBillDetail(params.getUserid(), params.getGroupid(),
                params.getTaskId(), params.getBillId(), params.getBilltype());
    }

    public String getNCApproveDetailData(NCBillDetailParams params) {
        return ncService.getNCApproveDetail(params.getUserid(), params.getGroupid(),
                params.getTaskId(), params.getBillId(), params.getBilltype());
    }

    public String getNCAssignUserList(NCActionParams params) {
        return ncService.ncAssignUserList(params.getUserid(), params.getGroupid(),
                params.getTaskId(), params.getBillId(), params.getAction());
    }

    public String action(NCActionParams params) {

        String result = ncService.ncAction(params.getUserid(), params.getGroupid(),
                params.getTaskId(), params.getAction(),
                Optional.ofNullable(params.getApproveMessage()).orElse(params.getAction()),
                params.getCuserids());

        if (actionSuccess(result)) {
            oaService.updateTask(params.getTaskId(), ACAEnums.action.valueOf(params.getAction()));
        }

        return result;
    }

    private String getAttachment(NCTaskBaseParams params) {
        return ncService.getAttachList(params.getUserid(), params.getGroupid(),
                params.getTaskId());
    }

    private boolean actionSuccess(String result) {
        if (StringUtils.isNotEmpty(result)) {
            return Constant.ZERO_STRING_VALUE.equals(
                    JsonResultService.getValue(
                            JsonResultService.createNode(result).get(0), NC_RESPONSE_FLAG));
        }
        return false;
    }

    public Map<String, Object> getApply(NCBillDetailParams params) {
        HistoricProcessInstanceResponse instResp =
                bpmDataConvertService.resolve2BpmApproveDetail(getNCBillDetailData(params), params);

        bpmDataConvertService.packHistoricProcessInstanceResponseWithNCApproveDetail(
                getNCApproveDetailData(params), instResp
        );

//        instResp.setHistoricTasks(
//                bpmDataConvertService.resolve2BPMHisTasks(getNCApproveDetailData(params), params));

        return new HashMap(){{
            put("inst", instResp);
            put("copyToEndTime", null);
            put("currentUserId", params.getUserid());
            put("nodeFormID", null);
        }};
    }

    public List<Attachment> queryInstAttachmentList(NCTaskBaseParams params) {
        return bpmDataConvertService.resolve2BPMAttachments(getAttachment(params));
    }

    public ResponseEntity<byte[]> download(NCFileDataParams params) {
        String fileData = ncService.getAttachFileData(params.getUserid(), params.getGroupid(), params.getFileId());
        byte[] bytes = bpmDataConvertService.resolveNCFileData(fileData);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        httpHeaders.setContentDisposition(
                ContentDisposition.builder(CONTENT_DISPOSITION_TYPE_FORM_DATA)
                        .filename(params.getFilename(), Charsets.UTF_8)
                        .build());
        return new ResponseEntity<>(bytes, httpHeaders, HttpStatus.CREATED);
    }

}
