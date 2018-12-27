package com.hx.nc.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.TimeUnit;

/**
 * 使用Guava实现的本地缓存
 *
 * @param <K> key
 * @param <V> value
 */
public class LocalCache<K, V> {

    private Cache<K, V> build;

    public LocalCache() {
        this(1000, 100000, 2, TimeUnit.HOURS);
    }

    /**
     * @param initCapacity 初始大小
     * @param maximumSize  最大大小
     * @param time         过期时间
     * @param timeunit     时间单位
     */
    public LocalCache(int initCapacity, int maximumSize, int time, TimeUnit timeunit) {
        build = CacheBuilder.newBuilder()
                            .initialCapacity(initCapacity)
                            .maximumSize(maximumSize)
                            .expireAfterWrite(time, timeunit)
                            .build();
    }

    /**
     * 根据key获取缓存值
     *
     * @param key key
     * @return null if not exists or expired
     */
    public V get(K key) {
        return build.getIfPresent(key);
    }

    /**
     * 放入缓存
     *
     * @param key   key
     * @param value value
     */
    public void put(K key, V value) {
        build.put(key, value);
    }


}
