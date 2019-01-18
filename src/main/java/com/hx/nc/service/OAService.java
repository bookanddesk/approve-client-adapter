package com.hx.nc.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.hx.nc.bo.ACAEnums;
import com.hx.nc.bo.nc.NCTask;
import com.hx.nc.bo.oa.OARestResult;
import com.hx.nc.bo.oa.OATask;
import com.hx.nc.bo.oa.OATaskBaseParams;
import com.hx.nc.bo.oa.Pendings;
import com.hx.nc.data.dao.OARestRepository;
import com.hx.nc.data.entity.OARestRecord;
import io.micrometer.core.instrument.MeterRegistry;
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
    @Autowired
    private MeterRegistry meterRegistry;
    @Autowired
    private OARestRepository oaRestRepository;


    public void sendTask(List<NCTask> list) {
        List<OATask> oaTasks = list.stream()
                .map(OATask::fromNCTask)
                .peek(this::countOATasks)
                .collect(Collectors.toList());
        log.info("sendOATask>> " + JsonResultService.toJson(oaTasks));
        OARestResult result = callOARest(buildOATaskRequestUrl(),
                Pendings.newBuilder()
                        .setPendingList(oaTasks)
                        .build());

        saveOATaskRecord("taskId: " + oaTasks.stream()
                        .map(OATask::getTaskId)
                        .collect(Collectors.joining(","))
                        .substring(0, 255),
                OARestRecord.Type.sendTask,
                result);
    }

    private void countOATasks(OATask oaTask) {
        meterRegistry.counter(ACA, ACA_METRICS_OA_TASKS, oaTask.getTaskId()).increment();
    }

    @Async
    public void updateTask(String taskId, ACAEnums.action action) {
        OARestResult result = callOARest(buildOATaskUpdateRequestUrl(),
                OATaskBaseParams.newBuilder()
                        .setRegisterCode(properties.getRegisterCode())
                        .setTaskId(taskId)
                        .setState(action.taskNextState())
                        .setSubState(action.taskNextSubState())
                        .build());

        saveOATaskRecord("taskId[" + taskId + "],action[" + action.getValue() + "]",
                OARestRecord.Type.updateTask, result);
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

    private <T> OARestResult callOARest(String url, T t) {
        JsonNode result = getResult(rest.exchange(url, HttpMethod.POST, buildHttpEntity(t), JsonNode.class));
        return checkOARestResult(result);
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

    private OARestResult checkOARestResult(JsonNode result) {
        String string = result.toString();
        log.info("OARestResult>> " + string);
        OARestResult oaRestResult = JsonResultService.toObject(string, OARestResult.class);
        if (!oaRestResult.isSuccess()) {
            log.error("OARestResult fail:" + oaRestResult.toString());
        }
        return oaRestResult;
    }

    private void saveOATaskRecord(String params, OARestRecord.Type type,
                                  OARestResult oaRestResult) {
        try {
            oaRestRepository.save(OARestRecord.builder()
                    .params(params)
                    .type(type)
                    .result(oaRestResult.getErrorMsgs()
                            .toString()
                            .substring(0, 255)
                    )
                    .success((short) (oaRestResult.isSuccess() ? 0 : 1))
                    .build());
        } finally {
        }
    }


}
