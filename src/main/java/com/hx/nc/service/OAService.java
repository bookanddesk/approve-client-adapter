package com.hx.nc.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import com.hx.nc.bo.ACAEnums;
import com.hx.nc.bo.nc.NCTask;
import com.hx.nc.bo.oa.*;
import com.hx.nc.data.entity.OARestRecord;
import com.hx.nc.utils.StringUtils;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.hx.nc.bo.Constants.*;

/**
 * @author XingJiajun
 * @Date 2018/12/26 21:07
 * @Description
 */
@Slf4j
@Service
public class OAService {

    private final NCProperties properties;
    private final RestTemplate rest;
    private final MeterRegistry meterRegistry;
    private final RepoService repoService;
    private final ThreadPoolTaskExecutor applicationTaskExecutor;
    private final LoadingCache<String, String> token;

    @Autowired
    public OAService(NCProperties properties, RestTemplate rest, MeterRegistry meterRegistry, RepoService repoService, ThreadPoolTaskExecutor applicationTaskExecutor) {
        this.properties = properties;
        this.rest = rest;
        this.meterRegistry = meterRegistry;
        this.repoService = repoService;
        this.applicationTaskExecutor = applicationTaskExecutor;
        this.token = CacheBuilder.newBuilder()
                .expireAfterWrite(12, TimeUnit.MINUTES)
                .maximumSize(1)
                .build(new CacheLoader<String, String>() {
                    @Override
                    public String load(String s) {
                        return getToken();
                    }
                });
    }

    public void sendTask(List<NCTask> list) {
        Map<String, OAUser> oaUser = null;
        try {
            oaUser = getOAUser(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<OATask> oaTasks;
        if (CollectionUtils.isEmpty(oaUser)) {
            oaTasks = list.stream()
                    .map(OATask::fromNCTask)
                    .peek(this::countOATasks)
                    .collect(Collectors.toList());
        } else {
            oaTasks = OATask.fromNCTask(list, oaUser);
        }

        log.info("sendOATask>> " + JsonResultService.toJson(oaTasks));
        OARestResult result = null;
        try {
            result = callOARest(buildOATaskRequestUrl(),
                    Pendings.newBuilder()
                            .setPendingList(oaTasks)
                            .build());
        } catch (Exception e) {
            log.error("sendOATask error>> " + e.getMessage(), e);
            result = afterException(e);
        } finally {
            saveOATaskRecord(StringUtils.substring(
                    oaTasks.stream()
                            .map(OATask::getTaskId)
                            .collect(Collectors.joining(",", "taskIds[", "]")),
                    0, 255),
                    OARestRecord.Type.sendTask,
                    result);
        }
    }

    private Map<String, OAUser> getOAUser(List<NCTask> ncTasks) {
        Map<String, OAUser> userMap = null;
        Set<String> ncUserIds = new HashSet<>();
        ncTasks.forEach(x -> {
            ncUserIds.add(x.getCuserId());
            ncUserIds.add(x.getSenderMan());
        });

//        List<OAUser> oaUsers = repoService.findAllOAUserById(ncUserIds);
//        if (!CollectionUtils.isEmpty(oaUsers)) {
//            userMap = oaUsers.stream()
//                    .peek(x -> ncUserIds.remove(x.getNcId()))
//                    .collect(Collectors.toMap(OAUser::getNcId, Function.identity()));
//        }

        if (!ncUserIds.isEmpty()) {
            Map<String, OAUser> oaUserInfo = getOAUserInfo(Lists.newArrayList(ncUserIds));
            if (!CollectionUtils.isEmpty(oaUserInfo)) {
//                repoService.saveOAUserInfo(oaUserInfo.values());
//                if (userMap == null) {
                    userMap = oaUserInfo;
//                } else {
//                    userMap.putAll(oaUserInfo);
//                }
            }
        }
        return userMap;
    }

    private void countOATasks(OATask oaTask) {
        meterRegistry.counter(ACA, ACA_METRICS_OA_TASKS, oaTask.getTaskId()).increment();
    }

    @Async
    public void updateTask(String taskId, ACAEnums.action action) {
        OARestResult result = null;
        try {
            result = doOATaskUpdateRequest(taskId, action);
        } catch (Exception e) {
            log.error("updateOATask error>> " + e.getMessage());
            result = afterException(e);
        } finally {
            saveOATaskRecord("taskId[" + taskId + "],action[" + action.getValue() + "]",
                    OARestRecord.Type.updateTask, result);
        }
    }

    void updateTask(List<String> taskIds) {
        Stream<CompletableFuture<Void>> completableFutureStream = taskIds.stream()
                .map(x -> CompletableFuture.runAsync(() ->
                                doOATaskUpdateRequest(x, ACAEnums.action.agree),
                        applicationTaskExecutor));
        CompletableFuture.allOf(completableFutureStream.toArray(CompletableFuture[]::new))
                .join();
    }

    private OARestResult doOATaskUpdateRequest(String taskId, ACAEnums.action action) {
        log.info(Thread.currentThread().getName() + "---updateTask--" + taskId
                + "---" + action);
        return callOARest(buildOATaskUpdateRequestUrl(),
                OATaskBaseParams.newBuilder()
                        .setRegisterCode(properties.getRegisterCode())
                        .setTaskId(taskId)
                        .setState(action.taskNextState())
                        .setSubState(action.taskNextSubState())
                        .build());
    }

    public Map<String, OAUser> getOAUserInfo(List<String> ncUserIds) {
        JsonNode result = getResult(
                rest.exchange(buildOAUserRequestUrl(),
                        HttpMethod.POST,
                        buildHttpEntity(OAUserReqParams.builder().userzj(ncUserIds).build()),
                        JsonNode.class));
        if (result != null && result.size() > 0) {
            return resolveOAUser(result.get(0));
        }
        return null;
    }

    private Map<String, OAUser> resolveOAUser(JsonNode jsonNode) {
        Map<String, OAUser> userMap = new HashMap<>(jsonNode.size(), 1);
        Iterator<Map.Entry<String, JsonNode>> fields = jsonNode.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> next = fields.next();
            String ncId = next.getKey();
            JsonNode oaValue = next.getValue();
            userMap.put(ncId,
                    OAUser.builder()
                            .ncId(ncId)
                            .name(JsonResultService.getValue(oaValue, "name"))
                            .code(JsonResultService.getValue(oaValue, "code"))
                            .build());
        }
        return userMap;
    }


    private String buildOATaskRequestUrl() {
        return properties.getOaIP() +
                OA_REST_URI_RECEIVE_PENDING;
    }

    private String buildOATaskUpdateRequestUrl() {
        return properties.getOaIP() +
                OA_REST_URI_UPDATE_PENDING;
    }

    private String buildOATokenRequestUrl() {
        return properties.getOaIP() +
                OA_REST_URI_TOKEN;
    }

    private String buildOAUserRequestUrl() {
        return properties.getOaIP() +
                OA_REST_URI_UPDATE_USER_INFO;
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
        headers.add(OA_REST_HEADER_TOKEN, token.getUnchecked(OA_REST_HEADER_TOKEN));
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
        repoService.saveOARestRecord(OARestRecord.builder()
                .params(StringUtils.substring(params, 0, 255))
                .type(type)
                .result(StringUtils.substring(oaRestResult.getErrorMsgs()
                        .toString(), 0, 255))
                .success((short) (oaRestResult.isSuccess() ? 0 : 1))
                .build());
    }

    private OARestResult afterException(Exception e) {
        return OARestResult.builder()
                .success(false)
                .errorMsg(
                        OARestErrorMSg.builder()
                                .errorDetail(e.getMessage())
                                .build())
                .build();
    }

}
