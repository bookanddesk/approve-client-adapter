package com.hx.nc.service;

import com.hx.nc.bo.Constant;
import com.hx.nc.cache.LocalCache;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.Date;

/**
 * @author XingJiajun
 * @Date 2018/12/27 15:23
 * @Description
 */
@Component
public class CacheService {

    private static final LocalCache<String, String> pollTimeCache = new LocalCache<>();
    private static final LocalCache<String, String> ncOAUserIDCache = new LocalCache<>();

    public String getLastPollDate() {
        return pollTimeCache.get(Constant.LAST_POLL_DATE_TIME);
    }

    public void cachePollDate(String date) {
        if (date != null)
            pollTimeCache.put(Constant.LAST_POLL_DATE_TIME, date);
    }

    public String getOAUserIdByNCId(String ncUserId) {
        return ncOAUserIDCache.get(ncUserId);
    }

    public void cacheOAUserId4NCId(String ncUserId, String oaUserId) {
        ncOAUserIDCache.put(ncUserId, oaUserId);
    }

}
