package com.hx.nc.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.common.base.Preconditions;
import com.hx.nc.bo.Constant;
import com.hx.nc.bo.nc.NCTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.hx.nc.bo.Constant.*;

/**
 * @author XingJiajun
 * @Date 2018/12/27 15:53
 * @Description
 */
@Component
public class NCDataProcessService {

    @Autowired
    private NCProperties properties;

    private JsonNode toJson(String string) {
        Preconditions.checkNotNull(string);
        return JsonResultService.createNode(string);
    }

    private List toList(String string) {
        return JsonResultService.toObject(toJson(string).toString(), List.class);
    }

    private JsonNode toJson(Object object) {
        return toJson((String) object);
    }

    public List<Map<String, Object>> resolveTaskList(String result) {
        List<Map<String, Object>> list = toList(result);
        if (list == null || list.size() == 0) {
            throw new RuntimeException();
        }
        Map<String, Object> dataMap = list.get(0);
        if (!Constant.ZERO_STRING_VALUE.equals(dataMap.get(Constant.NC_RESPONSE_FLAG))) {
            throw new RuntimeException(
                    Optional.ofNullable(dataMap.get(Constant.NC_RESPONSE_DES).toString())
                            .orElse(""));
        }
        List<Map<String, Object>> tasks =
                (List<Map<String, Object>>) dataMap.get(Constant.NC_RESPONSE_PROP_TASK_STRUCT_LIST);

        return tasks;
    }

    public List<NCTask> resolveNCTaskList(String result) {
        JsonNode jsonNode = toJson(result);
        JsonNode node = jsonNode.get(0);
        if (!Constant.ZERO_STRING_VALUE.equals(JsonResultService.getValue(node, Constant.NC_RESPONSE_FLAG))) {
            throw new RuntimeException(
                    Optional.ofNullable(JsonResultService.getValue(node, Constant.NC_RESPONSE_DES))
                            .orElse(""));
        }
        ArrayNode arrayNode = JsonResultService.getArrayNode(node, Constant.NC_RESPONSE_PROP_TASK_STRUCT_LIST);
        if (arrayNode == null) {
            return null;
        }
        List<NCTask> ncTasks = new ArrayList<>(arrayNode.size());
        Iterator<JsonNode> iterator = arrayNode.iterator();
        while (iterator.hasNext()) {
            JsonNode next = iterator.next();
            NCTask task = JsonResultService.toObject(next.toString(), NCTask.class);
            task.setMUrl(buildMUrl(task));
            ncTasks.add(task);
        }
        return ncTasks;
    }

    private String buildMUrl(NCTask task) {
        return new StringBuilder(properties.getHost())
                .append(NC_DETAIL_URL_MOBILE)
                .append(NC_PARAM_USER_ID).append("=").append(task.getCuserId())
                .append("&")
                .append(NC_PARAM_GROUP_ID).append("=").append(properties.getGroupid())
                .append("&")
                .append(NC_PARAM_TASK_ID).append("=").append(task.getTaskid())
                .append("&")
                .append(NC_PARAM_BILL_ID).append("=").append(task.getBillId())
                .append("&")
                .append(NC_PARAM_PK_BILL_TYPE).append("=").append(task.getBillType())
                .toString();
    }

}
