package com.hx.nc.service;

import com.hx.nc.bo.Constant;
import com.hx.nc.bo.NCTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
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
    private OAService oaService;

//    @Scheduled(fixedRate = Constant.POLL_RATE)
    public void ncTaskPolling() {
        List<NCTask> ncTask = ncService.getNCTaskList(getLastPollDate());
        if (ncTask == null && ncTask.size() == 0) {
            return;
        }
        oaService.sendTask(ncTask);
    }

    private Date getLastPollDate() {
        return cacheService.getLastPollDate();
    }


}
