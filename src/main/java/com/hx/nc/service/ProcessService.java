package com.hx.nc.service;

import com.hx.nc.bo.NCTask;
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

    public String getNCBillDetailData() {
        String userId = getParameter(NC_PARAM_USER_ID),
                groupId = getParameter(NC_PARAM_GROUP_ID),
                taskId = getParameter(NC_PARAM_TASK_ID),
                billId = getParameter(NC_PARAM_BILL_ID),
                billType = getParameter(NC_PARAM_BILL_TYPE);
        if (StringUtils.isAllEmpty(userId, groupId, taskId, billId, billType)) {
            throw new IllegalArgumentException("one or more param is empty:[" +
                    StringUtils.join_(userId, groupId, taskId, billId, billType) + "]");
        }

        return ncService.getNCBillDetail(userId, groupId, taskId, billId, billType);
    }

    public String getNCApproveDetailData() {
        String userId = getParameter(NC_PARAM_USER_ID),
                groupId = getParameter(NC_PARAM_GROUP_ID),
                taskId = getParameter(NC_PARAM_TASK_ID),
                billId = getParameter(NC_PARAM_BILL_ID),
                billType = getParameter(NC_PARAM_BILL_TYPE);
        if (StringUtils.isAllEmpty(userId, groupId, taskId, billId, billType)) {
            throw new IllegalArgumentException("one or more param is empty:[" +
                    StringUtils.join_(userId, groupId, taskId, billId, billType)+ "]");
        }

        return ncService.getNCApproveDetail(userId, groupId, taskId, billId, billType);
    }

    public String action() {
        String userId = getParameter(NC_PARAM_USER_ID),
                groupId = getParameter(NC_PARAM_GROUP_ID),
                taskId = getParameter(NC_PARAM_TASK_ID),
                action = getParameter(NC_PARAM_ACTION),
                msg = getParameter(NC_PARAM_APPROVE_MESSAGE);
        if (StringUtils.isAllEmpty(userId, groupId, taskId, action, msg)) {
            throw new IllegalArgumentException("one or more param is empty:[" +
                    StringUtils.join_(userId, groupId, taskId, action, msg)+ "]");
        }

        if (!StringUtils.equalsAny(action, NC_PARAM_ACTIONS)) {
            throw new IllegalArgumentException("action param illegal!");
        }

        String result = ncService.ncAction(userId, groupId, taskId, action, Optional.ofNullable(msg).orElse(action));
        if (NC_PARAM_ACTIONS[0].equals(action)) {
            String billId = getParameter(NC_PARAM_BILL_ID),
                    billType = getParameter(NC_PARAM_BILL_TYPE),
                    billtypename = getParameter(NC_PARAM_BILL_TYPE_NAME);

            oaService.updateTask(NCTask.newBuilder()
                    .setBillId(billId)
                    .setBillType(billType)
                    .setTaskid(taskId)
                    .setDate(null)
                    .setCuserId(userId)
                    .setTitle(billtypename)
                    .build());
        }

        return result;
    }

    public String getAttachment() {
        String userId = getParameter(NC_PARAM_USER_ID),
                groupId = getParameter(NC_PARAM_GROUP_ID),
                taskId = getParameter(NC_PARAM_TASK_ID);
        if (StringUtils.isAllEmpty(userId, groupId, taskId)) {
            throw new IllegalArgumentException("one or more param is empty:[" +
                    StringUtils.join_(userId, groupId, taskId)+ "]");
        }

        return ncService.getAttachList(userId, groupId, taskId);
    }




}
