package com.hx.nc.service;

import com.hx.nc.bo.Constant;
import com.hx.nc.bo.nc.NCTask;
import com.hx.nc.data.entity.NCPollingRecord;
import com.hx.nc.utils.DateTimeUtils;
import com.hx.nc.utils.FileUtils;
import com.hx.nc.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author XingJiajun
 * @Date 2018/12/26 21:03
 * @Description
 */
@Slf4j
@Component
//@Profile({"hx64", "prod"})
public class PollingTask {

    @Autowired
    private NCService ncService;
    @Autowired
    private CacheService cacheService;
    @Autowired
    private OAService oaService;
    @Autowired
    private NCProperties properties;
    @Autowired
    private RepoService repoService;


    @Scheduled(initialDelay = Constant.POLL_DELAY_ONE_MINUTES, fixedRate = Constant.LAST_POLL_DURATION_MILLIS)
    public void ncTaskPolling() {
        String lastPollDate = getLastPollDate();
        log.info("pollTaskAt>> " + lastPollDate);
        List<NCTask> ncTask = null;
        String ncResult = "taskIds[]";

        try {
            ncTask = ncService.getNCTaskList(lastPollDate);
        } catch (Exception e) {
            log.error("getNCTaskList error>> " + e.getMessage(), e);
            ncResult = e.getMessage();
            recordPollDate(lastPollDate);
        }

        int taskCount = ncTask != null ? ncTask.size() : 0;
        if (taskCount > 0) {
            ncResult = ncTask.stream()
                    .map(NCTask::getTaskid)
                    .collect(Collectors.joining(",", "taskIds[", "]"));
            oaService.sendTask(ncTask);
        }

        recordH2Polling(lastPollDate, taskCount, ncResult);
    }

    @Scheduled(initialDelay = Constant.POLL_DELAY_TWO_MINUTES, fixedRate = Constant.LAST_POLL_DURATION_MILLIS)
    public void ncDoneTaskPolling() {
        String lastPollDate = getLastDoneTaskDate();
        log.info("poll-Done-Task-At>> " + lastPollDate);
        List<String> ncTaskIds = null;
        try {
            ncTaskIds = ncService.getNCDoneTaskList(lastPollDate);
        } catch (Exception e) {
            log.error("getNCDoneTaskList error>> " + e.getMessage(), e);
            cacheService.cacheDoneTaskPollDate(lastPollDate);
        }

        int taskCount = ncTaskIds != null ? ncTaskIds.size() : 0;
        if (taskCount > 0) {
            oaService.updateTask(ncTaskIds);
        }

    }

    public void pushTask(List<String> taskIds, String lastDate) {
        List<NCTask> ncTaskList = ncService.getNCTaskList(lastDate);

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

    private String getLastPollDate() {
        String lastPollDate = getCachedLastPollDate();
        if (lastPollDate == null) {
            lastPollDate = getH2LastPollDate();
            if (lastPollDate == null) {
                lastPollDate = getFiledLastPollDate();
                if (lastPollDate == null) {
                    lastPollDate = getDefaultPollDate();
                }
            }
        }
        recordPollDate(DateTimeUtils.now());
        return lastPollDate;
    }

    private String getLastDoneTaskDate() {
        String lastDate = cacheService.getDoneTaskPollDate();
        if (lastDate == null) {
            lastDate = getFiledLastPollDate();
            if (lastDate == null) {
                lastDate = getDefaultPollDate();
            }
        }
        cacheService.cacheDoneTaskPollDate(DateTimeUtils.now());
        return lastDate;
    }

    private String getCachedLastPollDate() {
        return cacheService.getLastPollDate();
    }

    private String getH2LastPollDate() {
        return Optional.ofNullable(repoService.getLastPollingRecord())
                .orElse(null);
    }

    private String getFiledLastPollDate() {
        return FileUtils.getLastPollDateFromJsonFile(properties.getFilePath());
    }

    private String getDefaultPollDate() {
        return DateTimeUtils.defaultPollDateTime();
    }

    private void recordPollDate(String dateTime) {
        cacheService.cachePollDate(dateTime);
    }

    private void recordH2Polling(String lastPollDate, int taskCount, String ncResult) {
        repoService.savePollingRecord(NCPollingRecord.builder()
                .pollAt(lastPollDate)
                .taskCount(taskCount)
                .ncResult(StringUtils.substring(ncResult,0, 255))
                .nextTime(getCachedLastPollDate())
                .build());
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void deletePollingRecord() {
        repoService.deleteRedundantPollingRecords(DateTimeUtils.defaultPollingRecordBefore());
    }

    @PostConstruct
    public void initLastPollDate() {
        cacheService.cachePollDate(FileUtils.getLastPollDateFromJsonFile(properties.getFilePath()));
    }

    @PreDestroy
    public void saveLastPollDate() {
        FileUtils.recordLastPollDateToJsonFile(properties.getFilePath(), cacheService.getLastPollDate());
    }

}
