package com.hx.nc.service;

import com.hx.nc.bo.ACAEnums;
import com.hx.nc.bo.Constant;
import com.hx.nc.utils.StringUtils;

/**
 * @author XingJiajun
 * @Date 2019/4/9 9:45
 * @Description
 */
abstract class CacheKeyBuilder {

    private String buildCacheKey(String groupId, ACAEnums.OATaskState taskState) {
        return StringUtils.join_(Constant.LAST_POLL_DATE_TIME, taskState.name(), groupId);
    }

    String todoPollTimeKey(String groupId) {
        return buildCacheKey(groupId, ACAEnums.OATaskState.todo);
    }

    String donePollTimeKey(String groupId) {
        return buildCacheKey(groupId, ACAEnums.OATaskState.down);
    }

}
