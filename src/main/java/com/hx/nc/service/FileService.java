package com.hx.nc.service;

import com.hx.nc.utils.FileUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author XingJiajun
 * @Date 2018/12/28 21:52
 * @Description
 */
@Component
public class FileService {

    public String getPollDateTime() {
        return FileUtils.getLastPollDateFromJsonFile();
    }

    @Async
    public void setPollDateTime(String dateTime) {
        FileUtils.recordLastPollDateToJsonFile(dateTime);
    }

}
