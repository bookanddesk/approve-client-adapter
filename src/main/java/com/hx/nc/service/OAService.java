package com.hx.nc.service;

import com.hx.nc.bo.Constant;
import com.hx.nc.bo.NCTask;
import com.hx.nc.bo.OATask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author XingJiajun
 * @Date 2018/12/26 21:07
 * @Description
 */
@Service
public class OAService {

    @Autowired
    private NCProperties properties;
    @Autowired
    private RestTemplate rest;


    public void sendTask(List<NCTask> list) {
        List<OATask> oaTasks = list.stream()
                .map(OATask::fromNCTask)
                .collect(Collectors.toList());

        String result = rest.postForObject(buildOATaskRequestUrl(),
                new HashMap() {{
                    put("pendingList", oaTasks);
                }},
                String.class);
        System.out.println(result);
    }

    public void updateTask(NCTask task) {
        OATask oaTask = OATask.fromNCTask(task);
        oaTask.setState(Constant.ONE_STRING_VALUE);
        String result = rest.postForObject(buildOATaskUpdateRequestUrl(),
                oaTask,
                String.class);
        System.out.println(result);
    }

    private String buildOATaskRequestUrl() {
        return new StringBuilder(properties.getOaIP())
                .append(Constant.OA_REST_URI_RECEIVE_PENDING)
                .toString();
    }

    private String buildOATaskUpdateRequestUrl() {
        return new StringBuilder(properties.getOaIP())
                .append(Constant.OA_REST_URI_UPDATE_PENDING)
                .toString();
    }



}
