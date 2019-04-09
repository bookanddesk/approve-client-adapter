package com.hx.nc.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.collect.Maps;
import com.hx.nc.bo.Constant;
import com.hx.nc.utils.DateTimeUtils;
import com.hx.nc.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * @author XingJiajun
 * @Date 2018/12/27 15:23
 * @Description
 */
@Component
public class PollTimeService extends CacheKeyBuilder {

    private final NCProperties properties;
    private final RepoService repoService;
    private final LocalCache taskPollTimeCache;

    @Autowired
    public PollTimeService(NCProperties properties, RepoService repoService) {
        this.properties = properties;
        this.repoService = repoService;
        taskPollTimeCache = new LocalCache(this::loadPollTime);
    }

    private String getCachedLastPollDate(String key) {
        return taskPollTimeCache.get(key);
    }

    void cachePollDate(String key, String date) {
        if (date != null)
            taskPollTimeCache.put(key, date);
    }

    String getLastPollDate(String key) {
        String lastPollDate = getCachedLastPollDate(key);
        if (lastPollDate == null) {
            lastPollDate = getH2LastPollDate(key);
            if (lastPollDate == null) {
                lastPollDate = getFiledLastPollDate(key);
                if (lastPollDate == null) {
                    lastPollDate = getDefaultPollDate();
                }
            }
        }
        cachePollDate(key, DateTimeUtils.now());
        return lastPollDate;
    }

    private String loadPollTime(String key) {
        String lastPollDate = getH2LastPollDate(key);
        if (lastPollDate == null) {
            lastPollDate = getFiledLastPollDate(key);
        }
        return lastPollDate;
    }

    private String getH2LastPollDate(String key) {
        if (key.contains(Constant.UNDERLINE)) {
            key = key.split(Constant.UNDERLINE)[2];
        }
        return repoService.getLastPollingRecord(key);
    }

    private String getFiledLastPollDate(String key) {
        JsonNode node = JsonResultService.createNode(getFileContent());
        return JsonResultService.getValue(node, key);
    }

    private String getFileContent() {
        return FileUtils.getLastPollDateFromJsonFile(properties.getFilePath());
    }

    private String getDefaultPollDate() {
        return DateTimeUtils.defaultPollDateTime();
    }

    @PostConstruct
    public void initLastPollDate() {
        JsonNode node = JsonResultService.createNode(getFileContent());
        properties.getNc65Properties().keySet().forEach(
                x -> initPollTime(node, new String[]{todoPollTimeKey(x), donePollTimeKey(x)})
        );
    }

    @PreDestroy
    public void saveLastPollDate() {
        Map<String, String> map = Maps.newHashMap();
        properties.getNc65Properties().keySet().forEach(
                x -> savePollTime(map, new String[]{todoPollTimeKey(x), donePollTimeKey(x)})
        );
        FileUtils.recordLastPollDateToJsonFile(properties.getFilePath(), JsonResultService.toJson(map));
    }

    private void savePollTime(Map<String, String> map, String[] keys) {
        for (String k : keys) {
            map.put(k, getLastPollDate(k));
        }
    }

    private void initPollTime(JsonNode node, String[] keys) {
        for (String k : keys) {
            cachePollDate(k, JsonResultService.getValue(node, k));
        }
    }


    private class LocalCache {

        private final Cache<String, String> stringCache;

        /**
         * @param initCapacity 初始大小
         * @param maximumSize  最大大小
         * @param time         过期时间
         * @param timeunit     时间单位
         */
        private LocalCache(int initCapacity, int maximumSize, int time, TimeUnit timeunit, Function<String, String> function) {
            CacheBuilder<Object, Object> cacheBuilder = CacheBuilder.newBuilder();

            if (initCapacity > 0) {
                cacheBuilder.initialCapacity(initCapacity);
            }

            if (maximumSize > 0) {
                cacheBuilder.maximumSize(maximumSize);
            }

            if (time > 0) {
                cacheBuilder.expireAfterWrite(time, timeunit);
            }

            if (function != null) {
                stringCache = cacheBuilder.build(new CacheLoader<String, String>() {
                    @Override
                    public String load(String s) {
                        return function.apply(s);
                    }
                });
            } else {
                stringCache = cacheBuilder.build();
            }

        }

        LocalCache(Function<String, String> function) {
            this(6, 10, 5, TimeUnit.MINUTES, function);
        }

        /**
         * 根据key获取缓存值
         *
         * @param key key
         * @return null if not exists or expired
         */
        private String get(String key) {
            return stringCache.getIfPresent(key);
        }

        /**
         * 放入缓存
         *
         * @param key   key
         * @param value value
         */
        private void put(String key, String value) {
            stringCache.put(key, value);
        }

    }


}
