package com.hx.nc.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author XingJiajun
 * @Date 2018/12/26 21:03
 * @Description
 */
@Component
public class PollingTask {



    @Scheduled(cron = "0 */30 * * * ?")
    public void ncTaskPolling() {
    }

}
