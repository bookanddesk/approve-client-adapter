package com.hx.nc.service;

import com.hx.nc.bo.ACAEnums.OATaskState;
import com.hx.nc.bo.Constant;
import com.hx.nc.bo.nc.NCTask;
import com.hx.nc.data.entity.NCPollingRecord;
import com.hx.nc.utils.DateTimeUtils;
import com.hx.nc.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author XingJiajun
 * @Date 2018/12/26 21:03
 * @Description
 */
@Slf4j
@Component
//@Profile({"hx64", "prod"})
public class PollingTask extends CacheKeyBuilder{

    private final NCService ncService;
    private final PollTimeService pollTimeService;
    private final OAService oaService;
    private final NCProperties properties;
    private final RepoService repoService;

    @Autowired
    public PollingTask(NCService ncService, PollTimeService pollTimeService,
                       OAService oaService, NCProperties properties, RepoService repoService) {
        this.ncService = ncService;
        this.pollTimeService = pollTimeService;
        this.oaService = oaService;
        this.properties = properties;
        this.repoService = repoService;
    }


    @Scheduled(initialDelay = Constant.POLL_DELAY_ONE_MINUTES, fixedRate = Constant.LAST_POLL_DURATION_MILLIS)
    public void ncTaskPolling() {
        properties.getNc65Properties().keySet().forEach(
                x -> ncTaskPolling(
                        pollTimeService.getLastPollDate(todoPollTimeKey(x)), x));
    }

    private void ncTaskPolling(String lastPollDate, String groupId) {
        log.info("pollTaskAt>> " + lastPollDate + " groupId>> " + groupId);
        List<NCTask> ncTask = null;
        String ncResult = "taskIds[]";
        try {
            ncTask = ncService.getNCTaskList(lastPollDate, groupId);
        } catch (Exception e) {
            log.error("getNCTaskList error>> " + e.getMessage(), e);
            ncResult = e.getMessage();
            pollTimeService.cachePollDate(todoPollTimeKey(groupId), lastPollDate);
        }

        int taskCount = ncTask != null ? ncTask.size() : 0;
        if (taskCount > 0) {
            ncResult = ncTask.stream()
                    .map(NCTask::getTaskid)
                    .collect(Collectors.joining(",", "taskIds[", "]"));
            oaService.sendTask(ncTask);
        }

        recordH2Polling(lastPollDate, taskCount, ncResult, groupId);
    }

    @Scheduled(initialDelay = Constant.POLL_DELAY_TWO_MINUTES, fixedRate = Constant.LAST_POLL_DURATION_MILLIS)
    public void ncDoneTaskPolling() {
        properties.getNc65Properties().keySet().forEach(
                x -> ncDoneTaskPolling(
                        pollTimeService.getLastPollDate(donePollTimeKey(x)), x));
    }

    private void ncDoneTaskPolling(String lastPollDate, String groupId) {
        log.info("poll-Done-Task-At>> " + lastPollDate + " groupId>> " + groupId);
        List<String> ncTaskIds = null;
        try {
            ncTaskIds = ncService.getNCDoneTaskList(lastPollDate, groupId);
        } catch (Exception e) {
            log.error("getNCDoneTaskList error>> " + e.getMessage(), e);
            pollTimeService.cachePollDate(donePollTimeKey(groupId), lastPollDate);
        }

        int taskCount = ncTaskIds != null ? ncTaskIds.size() : 0;
        if (taskCount > 0) {
            oaService.updateTask(ncTaskIds);
        }
    }

    public void pushTask(List<String> taskIds, String lastDate, String groupId) {
        List<NCTask> ncTaskList = ncService.getNCTaskList(lastDate, groupId);

        if (CollectionUtils.isEmpty(ncTaskList)) {
            return;
        }

        if (taskIds != null && taskIds.size() > 0) {
            ncTaskList = ncTaskList.stream()
                    .filter(x -> taskIds.contains(x.getTaskid()))
                    .collect(Collectors.toList());
        }

        oaService.sendTask(ncTaskList);
    }

    private void recordH2Polling(String lastPollDate, int taskCount,
                                 String ncResult, String groupId) {
        repoService.savePollingRecord(NCPollingRecord.builder()
                .pollAt(lastPollDate)
                .taskCount(taskCount)
                .ncResult(StringUtils.substring(ncResult,0, 255))
                .nextTime(pollTimeService.getLastPollDate(todoPollTimeKey(groupId)))
                .groupId(groupId)
                .build());
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void deletePollingRecord() {
        repoService.deleteRedundantPollingRecords(DateTimeUtils.defaultPollingRecordBefore());
    }
}
