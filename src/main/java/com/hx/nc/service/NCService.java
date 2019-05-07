package com.hx.nc.service;

import com.hx.nc.bo.Constants;
import com.hx.nc.bo.nc.NCTask;
import com.hx.nc.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

import static com.hx.nc.bo.Constants.*;

/**
 * @author XingJiajun
 * @Date 2018/12/27 14:32
 * @Description
 */
@Service
public class NCService {

    private final NCProperties ncProperties;
    private final NCDataProcessService ncDataProcessService;
    private final RestTemplate rest;

    @Autowired
    public NCService(NCProperties ncProperties, NCDataProcessService ncDataProcessService, RestTemplate rest) {
        this.ncProperties = ncProperties;
        this.ncDataProcessService = ncDataProcessService;
        this.rest = rest;
    }


    public List<NCTask> getNCTaskList(String lastDate, String groupId) {
        return ncDataProcessService.resolveNCTaskList(
                rest.postForObject(buildNCTaskRequestUrl(lastDate, groupId),
                        null,
                        String.class), groupId);
    }

    List<String> getNCDoneTaskList(String lastDate, String groupId) {
        return ncDataProcessService.resolveNCTaskIdList(
                rest.postForObject(buildNCDoneTaskRequestUrl(lastDate, groupId),
                        null,
                        String.class));
    }

    String getNCBillDetail(String userId, String groupId,
                           String taskId, String billId, String billType) {
        return rest.postForObject(
                buildNCBillDetailRequestUrl(userId, groupId, taskId, billId, billType, NC_SERVLET_BILL_DETAIL),
                null,
                String.class);
    }

    String getNCApproveDetail(String userId, String groupId,
                              String taskId, String billId, String billType) {
        return rest.postForObject(
                buildNCBillDetailRequestUrl(userId, groupId, taskId, billId, billType, NC_SERVLET_APPROVE_DETAIL),
                null,
                String.class);
    }

    String ncAction(String userId, String groupId,
                    String taskId, String action,
                    String approveMsg, String cUserIds) {
        return rest.postForObject(
                buildNCActionRequestUrl(userId, groupId, taskId, action, approveMsg, cUserIds),
                null,
                String.class);
    }

    String ncAssignUserList(String userId, String groupId,
                            String taskId, String billId, String action) {
        return rest.postForObject(
                buildNCAssignUserListRequestUrl(userId, groupId, taskId, billId, action),
                null,
                String.class);
    }

    String getAttachList(String userId, String groupId, String taskId) {
        return rest.postForObject(
                buildNCAttachRequestUrl(userId, groupId, taskId),
                null,
                String.class);
    }

    String getAttachFileData(String userId, String groupId, String fileId) {
        return rest.postForObject(
                buildFileDataRequestUrl(userId, groupId, fileId),
                null,
                String.class);
    }

    private String buildNCTaskRequestUrl(String lastDate, String groupId) {
        return ncIp(groupId) +
                NC_SERVLET_TASK +
                "?" +
                commonParams(null, groupId) +
                "&" +
                NC_PARAM_LAST_DATE + "=" + lastDate;
    }

    private String buildNCDoneTaskRequestUrl(String lastDate, String groupId) {
        return buildNCTaskRequestUrl(lastDate, groupId) + "&" + Constants.NC_PARAM_DONE_TASK_QUERY_PARAM;
    }

    private String buildNCBillDetailRequestUrl(String userId, String groupId,
                                               String taskId, String billId, String billType,
                                               String servlet) {
        return ncIp(groupId) +
                servlet +
                "?" +
                commonParams(userId, groupId, taskId) +
                "&" +
                NC_PARAM_BILL_ID + "=" + billId +
                "&" +
                NC_PARAM_PK_BILL_TYPE + "=" + billType;
    }

    private String buildNCAssignUserListRequestUrl(String userId, String groupId,
                                                   String taskId, String billId, String action) {
        return ncIp(groupId) +
                Constants.NC_SERVLET_ASSIGN_USER_LIST +
                "?" +
                commonParams(userId, groupId, taskId) +
                "&" +
                NC_PARAM_BILL_ID + "=" + billId +
                "&" +
                NC_PARAM_ACTION + "=" + action;
    }

    private String buildNCActionRequestUrl(String userId, String groupId,
                                           String taskId, String action,
                                           String approveMsg, String cUserIds) {
        StringBuilder append = new StringBuilder(ncIp(groupId))
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
        return ncIp(groupId) +
                NC_SERVLET_ATTACH_LIST +
                "?" +
                commonParams(userId, groupId) +
                "&" +
                NC_PARAM_TASK_ID + "=" + taskId;
    }

    private String buildFileDataRequestUrl(String userId, String groupId, String fileId) {
        return ncIp(groupId) +
                NC_SERVLET_FILE_DATA +
                "?" +
                commonParams(userId, groupId) +
                "&" +
                NC_PARAM_FILE_ID + "=" + fileId;
    }

    private String commonParams(String userId, String groupId) {
        return NC_PARAM_USER_ID + "=" +
                Optional.ofNullable(userId).orElse(ncProperties.getUserid()) +
                "&" +
                NC_PARAM_GROUP_ID + "=" +
                Optional.ofNullable(groupId).orElse(ncProperties.getGroupid());
    }

    private String commonParams(String userId, String groupId, String taskId) {
        return commonParams(userId, groupId) +
                "&" +
                NC_PARAM_TASK_ID + "=" + taskId;
    }
    
    private String ncIp(String groupId) {
        return ncProperties.getNcIp(groupId);
    }
        
}
