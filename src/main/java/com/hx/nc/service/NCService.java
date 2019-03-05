package com.hx.nc.service;

import com.hx.nc.bo.Constant;
import com.hx.nc.bo.nc.NCTask;
import com.hx.nc.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
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


    public List<NCTask> getNCTaskList(String lastDate) {
        return ncDataProcessService.resolveNCTaskList(
                rest.postForObject(buildNCTaskRequestUrl(lastDate),
                        null,
                        String.class));
    }

    public List<String> getNCDoneTaskList(String lastDate) {
        return ncDataProcessService.resolveNCTaskIdList(
                rest.postForObject(buildNCDoneTaskRequestUrl(lastDate),
                        null,
                        String.class));
    }

    public String getNCBillDetail(String userId, String groupId,
                                  String taskId, String billId, String billType) {
        return rest.postForObject(
                buildNCBillDetailRequestUrl(userId, groupId, taskId, billId, billType, NC_SERVLET_BILL_DETAIL),
                null,
                String.class);
    }

    public String getNCApproveDetail(String userId, String groupId,
                                     String taskId, String billId, String billType) {
        return rest.postForObject(
                buildNCBillDetailRequestUrl(userId, groupId, taskId, billId, billType, NC_SERVLET_APPROVE_DETAIL),
                null,
                String.class);
    }

    public String ncAction(String userId, String groupId,
                           String taskId, String action,
                           String approveMsg, String cUserIds) {
        String result = rest.postForObject(
                buildNCActionRequestUrl(userId, groupId, taskId, action, approveMsg, cUserIds),
                null,
                String.class);
        return result;
    }

    public String ncAssignUserList(String userId, String groupId,
                                   String taskId, String billId, String action) {
        return rest.postForObject(
                buildNCAssignUserListRequestUrl(userId, groupId, taskId, billId, action, NC_SERVLET_ASSIGN_USER_LIST),
                null,
                String.class);
    }

    public String getAttachList(String userId, String groupId, String taskId) {
        return rest.postForObject(
                buildNCAttachRequestUrl(userId, groupId, taskId),
                null,
                String.class);
    }

    private String buildNCTaskRequestUrl(String lastDate) {
        return new StringBuilder(ncProperties.getIp())
                .append(NC_SERVLET_TASK)
                .append("?")
                .append(commonParams())
                .append("&")
                .append(NC_PARAM_LAST_DATE).append("=").append(lastDate)
                .toString();
    }

    private String buildNCDoneTaskRequestUrl(String lastDate) {
        return buildNCTaskRequestUrl(lastDate) + "&" + Constant.NC_PARAM_DONE_TASK_QUERY_PARAM;
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

    private String buildNCAssignUserListRequestUrl(String userId, String groupId,
                                                   String taskId, String billId, String action,
                                                   String servlet) {
        return new StringBuilder(ncProperties.getIp())
                .append(servlet)
                .append("?")
                .append(commonParams(userId, groupId, taskId))
                .append("&")
                .append(NC_PARAM_BILL_ID).append("=").append(billId)
                .append("&")
                .append(NC_PARAM_ACTION).append("=").append(action)
                .toString();
    }

    private String buildNCActionRequestUrl(String userId, String groupId,
                                           String taskId, String action,
                                           String approveMsg, String cUserIds) {
        StringBuilder append = new StringBuilder(ncProperties.getIp())
                .append(NC_SERVLET_ACTION)
                .append("?")
                .append(commonParams(userId, groupId, taskId))
                .append("&")
                .append(NC_PARAM_ACTION).append("=").append(action)
                .append("&")
                .append(NC_PARAM_APPROVE_MESSAGE).append("=").append(approveMsg);
        if (StringUtils.isNotEmpty(cUserIds)) {
            append.append("&")
                    .append(NC_PARAM_C_USER_IDS).append("=").append(approveMsg);
        }

        return append.toString();
    }

    private String buildNCAttachRequestUrl(String userId, String groupId, String taskId) {
        return new StringBuilder(ncProperties.getIp())
                .append(NC_SERVLET_ATTACH_LIST)
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
}
