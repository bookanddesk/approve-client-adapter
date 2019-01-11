package com.hx.nc.service;

import com.hx.nc.bo.nc.NCTask;
import com.hx.nc.utils.DateTimeUtils;
import com.hx.nc.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;

/**
 * @author XingJiajun
 * @Date 2018/12/26 21:03
 * @Description
 */
@Slf4j
@Component
public class PollingTask {

    @Autowired
    private NCService ncService;
    @Autowired
    private CacheService cacheService;
    @Autowired
    private OAService oaService;
    @Autowired
    private NCProperties properties;


//    @Scheduled(fixedRate = Constant.LAST_POLL_DURATION)
    public void ncTaskPolling() {
        List<NCTask> ncTask = ncService.getNCTaskList(getLastPollDate());
        if (ncTask == null || ncTask.size() == 0) {
            return;
        }
        oaService.sendTask(ncTask);
    }

    public String getLastPollDate() {
        String lastPollDate = getCachedLastPollDate();
        if (lastPollDate == null) {
            lastPollDate = getFiledLastPollDate();
            if (lastPollDate == null) {
                lastPollDate = getDefaultPollDate();
            }
        }
        recordPollDate(DateTimeUtils.now());
        log.info("pollTaskAt>> " + lastPollDate);
        return lastPollDate;
    }

    private String getCachedLastPollDate() {
        return cacheService.getLastPollDate();
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


    @PostConstruct
    public void initLastPollDate() {
        cacheService.cachePollDate(FileUtils.getLastPollDateFromJsonFile(properties.getFilePath()));
    }

    @PreDestroy
    public void saveLastPollDate() {
        FileUtils.recordLastPollDateToJsonFile(properties.getFilePath(), cacheService.getLastPollDate());
    }

}
