//package com.hx.nc.service;
//
//import com.hx.nc.cache.LocalCache;
//import org.springframework.stereotype.Component;
//
//import java.util.Date;
//
///**
// * @author XingJiajun
// * @Date 2018/12/27 15:23
// * @Description
// */
//@Component
//public class CacheService {
//
//    private static final String CACHE_KEY_LST_PO_DT = "ck_lst_po_dt";
//    private static final LocalCache<String, Date> pollTimeCache = new LocalCache<>();
//    private static final LocalCache<String, String> ncOAUserIDCache = new LocalCache<>();
//
//    public Date getLastPollDate() {
//        return pollTimeCache.get(CACHE_KEY_LST_PO_DT);
//    }
//
//    public void cachePollDate(Date date) {
//        if (date != null)
//            pollTimeCache.put(CACHE_KEY_LST_PO_DT, date);
//    }
//
//    public String getOAUserIdByNCId(String ncUserId) {
//        return ncOAUserIDCache.get(ncUserId);
//    }
//
//    public void cacheOAUserId4NCId(String ncUserId, String oaUserId) {
//        ncOAUserIDCache.put(ncUserId, oaUserId);
//    }
//
//}
