package com.hx.nc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.hx.nc.bo.Constant.*;

/**
 * @author XingJiajun
 * @Date 2018/12/27 14:32
 * @Description
 */
@Service
public class NCService {

    @Autowired
    private NCProperties ncProperties;
    @Autowired
    private NCDataProcessService ncDataProcessService;
    @Autowired
    private RestTemplate rest;


    public List<Map<String, Object>> getNCTask(Date lastDate) {
        String result = rest.postForObject(buildNCTaskRequestUrl(lastDate), null, String.class);
        return ncDataProcessService.resolveTaskList(result);
    }

    public String getNCBillDetail(String userId, String groupId,
                                  String taskId, String billId, String billType) {
        return rest.postForObject(
                buildNCBillDetailRequestUrl(userId, groupId, taskId, billId, billType, SERVLET_BILL_DETAIL),
                null,
                String.class);
    }

    public String getNCApproveDetail(String userId, String groupId,
                                     String taskId, String billId, String billType) {
        return rest.postForObject(
                buildNCBillDetailRequestUrl(userId, groupId, taskId, billId, billType, SERVLET_APPROVE_DETAIL),
                null,
                String.class);
    }

    public String ncAction(String userId, String groupId,
                           String taskId, String action, String approveMsg) {
        return rest.postForObject(
                buildNCActionRequestUrl(userId, groupId, taskId, action, approveMsg),
                null,
                String.class);
    }

    public String getAttachList(String userId, String groupId, String taskId) {
        return rest.postForObject(
                buildNCAttachRequestUrl(userId, groupId, taskId),
                null,
                String.class);
    }

    private String buildNCTaskRequestUrl(Date lastDate) {
        return new StringBuilder(ncProperties.getIp())
                .append(SERVLET_TASK)
                .append("?")
                .append(commonParams())
                .append("&lastDate=").append(getLastDate(lastDate))
                .toString();
    }

    private String buildNCBillDetailRequestUrl(String userId, String groupId,
                                               String taskId, String billId, String billType,
                                               String servlet) {
        return new StringBuilder(ncProperties.getIp())
                .append(servlet)
                .append("?")
                .append(commonParams(userId, groupId, taskId))
                .append("&")
                .append(NC_PARAM_BILL_ID).append("=").append(billId)
                .append("&")
                .append(NC_PARAM_PK_BILL_TYPE).append("=").append(billType)
                .toString();
    }

    private String buildNCActionRequestUrl(String userId, String groupId,
                                               String taskId, String action, String approveMsg) {
        return new StringBuilder(ncProperties.getIp())
                .append(SERVLET_ACTION)
                .append("?")
                .append(commonParams(userId, groupId, taskId))
                .append("&")
                .append(NC_PARAM_ACTION).append("=").append(action)
                .append("&")
                .append(NC_PARAM_APPROVE_MESSAGE).append("=").append(approveMsg)
                .toString();
    }

    private String buildNCAttachRequestUrl(String userId, String groupId, String taskId) {
        return new StringBuilder(ncProperties.getIp())
                .append(SERVLET_ATTACH_LIST)
                .append("?")
                .append(commonParams(userId, groupId))
                .append("&")
                .append(NC_PARAM_TASK_ID).append("=").append(taskId)
                .toString();
    }

    private String commonParams() {
        return commonParams(null, null);
    }

    private String commonParams(String userId, String groupId) {
        return new StringBuilder()
                .append(NC_PARAM_USER_ID).append("=")
                .append(Optional.ofNullable(userId).orElse(ncProperties.getUserid()))
                .append("&")
                .append(NC_PARAM_GROUP_ID).append("=")
                .append(Optional.ofNullable(groupId).orElse(ncProperties.getGroupid()))
                .toString();
    }

    private String commonParams(String userId, String groupId, String taskId) {
        return new StringBuilder(commonParams(userId, groupId))
                .append("&")
                .append(NC_PARAM_TASK_ID).append("=").append(taskId)
                .toString();
    }

    private String getLastDate(Date lstDate) {
        return "2018-12-10";
    }

}
