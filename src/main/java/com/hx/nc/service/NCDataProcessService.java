package com.hx.nc.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.hx.nc.bo.Constants;
import com.hx.nc.bo.nc.NCTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.hx.nc.bo.Constants.*;

/**
 * @author XingJiajun
 * @Date 2018/12/27 15:53
 * @Description
 */
@Component
public class NCDataProcessService extends AbstractNCDataProcessService {

    private final NCProperties properties;

    @Autowired
    public NCDataProcessService(NCProperties properties) {
        this.properties = properties;
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
        if (!Constants.ZERO_STRING_VALUE.equals(dataMap.get(Constants.NC_RESPONSE_FLAG))) {
            throw new RuntimeException(
                    Optional.ofNullable(dataMap.get(Constants.NC_RESPONSE_DES).toString())
                            .orElse(""));
        }
        List<Map<String, Object>> tasks =
                (List<Map<String, Object>>) dataMap.get(Constants.NC_RESPONSE_PROP_TASK_STRUCT_LIST);

        return tasks;
    }

    List<NCTask> resolveNCTaskList(String result, String groupId) {
        ArrayNode arrayNode = resolveTaskNode(result);
        if (arrayNode == null) {
            return null;
        }
        List<NCTask> ncTasks = new ArrayList<>(arrayNode.size());
        for (JsonNode next : arrayNode) {
            NCTask task = JsonResultService.toObject(next.toString(), NCTask.class);
            task.setGroupId(groupId);
            task.setMUrl(buildMUrl(task));
            ncTasks.add(task);
        }
        return ncTasks;
    }

    List<String> resolveNCTaskIdList(String result) {
        ArrayNode arrayNode = resolveTaskNode(result);
        if (arrayNode == null) {
            return null;
        }
        return StreamSupport.stream(arrayNode.spliterator(), false)
                .map(x -> JsonResultService.getValue(x, "taskid"))
                .collect(Collectors.toList());
    }

    private ArrayNode resolveTaskNode(String result) {
        JsonNode jsonNode = getNCDataNode(result);
        checkNCData(jsonNode);
        return JsonResultService.getArrayNode(jsonNode, Constants.NC_RESPONSE_PROP_TASK_STRUCT_LIST);
    }

    private String buildMUrl(NCTask task) {
        return properties.getHost() +
                NC_DETAIL_URL_MOBILE +
                NC_PARAM_USER_ID + "=" + task.getCuserId() +
                "&" +
                NC_PARAM_GROUP_ID + "=" + task.getGroupId() +
                "&" +
                NC_PARAM_TASK_ID + "=" + task.getTaskid() +
                "&" +
                NC_PARAM_BILL_ID + "=" + task.getBillId() +
                "&" +
                NC_PARAM_PK_BILL_TYPE + "=" + task.getBillType();
    }

}
