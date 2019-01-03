package com.hx.nc.service;

import com.hx.nc.bo.*;
import com.hx.nc.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.hx.nc.bo.Constant.*;

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

        if (actionSuccess(result) && NC_PARAM_ACTIONS_AGREE.equals(params.getAction())) {
            oaService.updateTask(NCTask.newBuilder()
                    .setBillId(params.getBillId())
                    .setBillType(params.getBilltype())
                    .setTaskid(params.getTaskId())
                    .setDate(null)
                    .setCuserId(params.getUserid())
                    .setTitle(params.getBilltypename())
                    .build());
        }

        return result;
    }

    public String getAttachment(NCTaskBaseParams params) {
        return ncService.getAttachList(params.getUserid(), params.getGroupid(),
                params.getTaskId());
    }

    private boolean actionSuccess(String result) {
        if (StringUtils.isNotEmpty(result)) {
            return Constant.zero_string_value.equals(
                    JsonResultService.getValue(
                            JsonResultService.createNode(result), NC_RESPONSE_FLAG));
        }
        return false;
    }


}
