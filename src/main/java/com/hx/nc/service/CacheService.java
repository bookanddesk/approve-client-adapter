package com.hx.nc.service;

import com.hx.nc.bo.Constant;
import com.hx.nc.cache.LocalCache;
import org.springframework.stereotype.Component;

/**
 * @author XingJiajun
 * @Date 2018/12/27 15:23
 * @Description
 */
@Component
public class CacheService {

    private static final LocalCache<String, String> taskPollTimeCache = new LocalCache<>();
    private static final LocalCache<String, String> doneTaskPollTimeCache = new LocalCache<>();
//    private static final LocalCache<String, String> ncOAUserIDCache = new LocalCache<>();

    String getLastPollDate() {
        return taskPollTimeCache.get(Constant.LAST_POLL_DATE_TIME);
    }

    void cachePollDate(String date) {
        if (date != null)
            taskPollTimeCache.put(Constant.LAST_POLL_DATE_TIME, date);
    }

    String getDoneTaskPollDate() {
        return doneTaskPollTimeCache.get(Constant.LAST_DONE_TASK_POLL_DATE_TIME);
    }

    void cacheDoneTaskPollDate(String date) {
        if (date != null) {
            doneTaskPollTimeCache.put(Constant.LAST_DONE_TASK_POLL_DATE_TIME, date);
        }
    }

//    public String getOAUserIdByNCId(String ncUserId) {
//        return ncOAUserIDCache.get(ncUserId);
//    }
//
//    public void cacheOAUserId4NCId(String ncUserId, String oaUserId) {
//        ncOAUserIDCache.put(ncUserId, oaUserId);
//    }

}
