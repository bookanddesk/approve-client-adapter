package com.hx.nc.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.hx.nc.bo.ACAEnums;
import com.hx.nc.bo.nc.NCTask;
import com.hx.nc.bo.oa.OATask;
import com.hx.nc.bo.oa.OATaskBaseParams;
import com.hx.nc.bo.oa.Pendings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

import static com.hx.nc.bo.Constant.*;

/**
 * @author XingJiajun
 * @Date 2018/12/26 21:07
 * @Description
 */
@Slf4j
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

        JsonNode result = callOARest(buildOATaskRequestUrl(),
                Pendings.newBuilder()
                        .setPendingList(oaTasks)
                        .build());
        checkOARestResult(result);
    }

    @Async
    public void updateTask(String taskId, ACAEnums.action action) {
        JsonNode result = callOARest(buildOATaskUpdateRequestUrl(),
                OATaskBaseParams.newBuilder()
                        .setRegisterCode(properties.getRegisterCode())
                        .setTaskId(taskId)
                        .setState(action.taskNextState())
                        .setSubState(action.taskNextSubState())
                        .build());
        checkOARestResult(result);
    }

    private String buildOATaskRequestUrl() {
        return new StringBuilder(properties.getOaIP())
                .append(OA_REST_URI_RECEIVE_PENDING)
                .toString();
    }

    private String buildOATaskUpdateRequestUrl() {
        return new StringBuilder(properties.getOaIP())
                .append(OA_REST_URI_UPDATE_PENDING)
                .toString();
    }

    private String buildOATokenRequestUrl() {
        return new StringBuilder(properties.getOaIP())
                .append(OA_REST_URI_TOKEN)
                .toString();
    }

    private <T> JsonNode callOARest(String url, T t) {
        return getResult(rest.exchange(url, HttpMethod.POST, buildHttpEntity(t), JsonNode.class));
    }

    private <T> HttpEntity<T> buildHttpEntity(T t) {
        return new HttpEntity<>(t, buildHeaders());
    }

    private MultiValueMap<String, String> buildHeaders() {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(OA_REST_HEADER_TOKEN, getToken());
        return headers;
    }

    private String getToken() {
        return getResult(rest.getForEntity(buildOATokenRequestUrl(), String.class,
                properties.getOaUser(), properties.getOaPwd()));
    }

    private <T> T getResult(ResponseEntity<T> responseEntity) {
        if (HttpStatus.OK != responseEntity.getStatusCode()) {
            log.error("oa response error:" + responseEntity);
            throw new RuntimeException("oa response error:" + responseEntity);
        }
        return responseEntity.getBody();
    }

    private void checkOARestResult(JsonNode result) {
        log.info(result.toString());
        if (!JsonResultService.getBoolValue(result, OA_REST_RESPONSE_PROP)) {
            log.error("OARestResult fail:" + result.get(OA_REST_RESPONSE_ERROR_MSG).toString());
        }
    }


}
