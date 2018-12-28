package com.hx.nc.service;

import com.hx.nc.bo.NCTask;
import com.hx.nc.utils.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author XingJiajun
 * @Date 2018/12/26 21:03
 * @Description
 */
@Component
public class PollingTask {

    @Autowired
    private NCService ncService;
    @Autowired
    private CacheService cacheService;
    @Autowired
    private FileService fileService;
    @Autowired
    private OAService oaService;

//    @Scheduled(fixedRate = Constant.POLL_RATE)
    public void ncTaskPolling() {
        List<NCTask> ncTask = ncService.getNCTaskList(getLastPollDate());
        if (ncTask == null && ncTask.size() == 0) {
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
        recordPollDate(lastPollDate);
        return lastPollDate;
    }

    private String getCachedLastPollDate() {
        return cacheService.getLastPollDate();
    }

    private String getFiledLastPollDate() {
        return fileService.getPollDateTime();
    }

    private String getDefaultPollDate() {
        return DateTimeUtils.halfHourBefore();
    }

    private void recordPollDate(String dateTime) {
        cacheService.cachePollDate(dateTime);
        fileService.setPollDateTime(dateTime);
    }

}
