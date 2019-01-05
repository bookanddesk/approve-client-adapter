package com.hx.nc.service;

import com.hx.nc.bo.ACAEnums;
import com.hx.nc.bo.Constant;
import com.hx.nc.bo.nc.NCActionParams;
import com.hx.nc.bo.nc.NCBillDetailParams;
import com.hx.nc.bo.nc.NCTaskBaseParams;
import com.hx.nc.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.hx.nc.bo.Constant.NC_RESPONSE_FLAG;

/**
 * @author XingJiajun
 * @Date 2018/12/26 9:58
 * @Description
 */
@Service
public class ProcessService extends BaseService {

    @Autowired
    private NCService ncService;
    @Autowired
    private OAService oaService;

    public String getNCBillDetailData(NCBillDetailParams params) {
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

    public String getAttachment(NCTaskBaseParams params) {
        return ncService.getAttachList(params.getUserid(), params.getGroupid(),
                params.getTaskId());
    }

    private boolean actionSuccess(String result) {
        if (StringUtils.isNotEmpty(result)) {
            return Constant.ZERO_STRING_VALUE.equals(
                    JsonResultService.getValue(
                            JsonResultService.createNode(result), NC_RESPONSE_FLAG));
        }
        return false;
    }



}
