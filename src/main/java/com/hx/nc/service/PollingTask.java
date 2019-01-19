package com.hx.nc.service;

import com.hx.nc.bo.Constant;
import com.hx.nc.bo.nc.NCTask;
import com.hx.nc.data.entity.PollingRecord;
import com.hx.nc.utils.DateTimeUtils;
import com.hx.nc.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.Optional;

/**
 * @author XingJiajun
 * @Date 2018/12/26 21:03
 * @Description
 */
@Slf4j
@Component
@Profile("hx64")
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


    @Scheduled(fixedRate = Constant.LAST_POLL_DURATION)
    public void ncTaskPolling() {
        String lastPollDate = getLastPollDate();
        List<NCTask> ncTask = ncService.getNCTaskList(lastPollDate);
        int taskCount = ncTask != null ? ncTask.size() : 0;
        recordH2Polling(lastPollDate, taskCount);
        if (taskCount == 0) {
            return;
        }
        oaService.sendTask(ncTask);
    }

    public String getLastPollDate() {
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
        log.info("pollTaskAt>> " + lastPollDate);
        return lastPollDate;
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

    private void recordH2Polling(String lastPollDate, int taskCount) {
        repoService.savePollingRecord(PollingRecord.builder()
                .pollAt(lastPollDate)
                .taskCount(taskCount)
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
