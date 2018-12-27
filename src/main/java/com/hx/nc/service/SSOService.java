package com.hx.nc.service;

import com.hx.nc.bo.Constant;
import com.hx.nc.utils.StringUtils;
import org.springframework.stereotype.Service;

import static com.hx.nc.bo.Constant.*;

/**
 * @author XingJiajun
 * @Date 2018/12/26 9:15
 * @Description
 */
@Service
public class SSOService extends BaseService {

    public String getDetailRedirectUrl() {
        String userId = getParameter(NC_PARAM_USER_ID),
                groupId = getParameter(NC_PARAM_GROUP_ID),
                taskId = getParameter(NC_PARAM_TASK_ID),
                billId = getParameter(NC_PARAM_BILL_ID),
                billType = getParameter(NC_PARAM_BILL_TYPE);
        if (StringUtils.isAllEmpty(userId, groupId, taskId, billId, billType)) {
            throw new IllegalArgumentException("one or more param is empty:[" +
                    StringUtils.join_(userId, groupId, taskId, billId, billType));
        }

        String redirectUrl = new StringBuilder()
                .append(NC_PARAM_USER_ID).append("=").append(userId)
                .append(NC_PARAM_GROUP_ID).append("=").append(groupId)
                .append(NC_PARAM_TASK_ID).append("=").append(taskId)
                .append(NC_PARAM_BILL_ID).append("=").append(billId)
                .append(NC_PARAM_PK_BILL_TYPE).append("=").append(billType)
                .toString();

        return redirectUrl;
    }

}
