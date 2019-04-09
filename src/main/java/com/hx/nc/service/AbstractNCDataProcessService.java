package com.hx.nc.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Preconditions;
import com.hx.nc.bo.Constant;

import java.util.Optional;

/**
 * @author XingJiajun
 * @Date 2019/1/11 10:23
 * @Description
 */
public class AbstractNCDataProcessService implements INCDataProcessService{
    @Override
    public JsonNode getNCDataNode(String jsonStr) {
        return toJson(jsonStr).get(0);
    }

    @Override
    public void checkNCData(JsonNode jsonNode) {
        if (!Constant.ZERO_STRING_VALUE.equals(
                JsonResultService.getValue(jsonNode, Constant.NC_RESPONSE_FLAG))) {
            throw new RuntimeException(
                    Optional.ofNullable(JsonResultService.getValue(jsonNode, Constant.NC_RESPONSE_DES))
                            .orElse("NC Data Result error!"));
        }
    }

    JsonNode toJson(String string) {
        Preconditions.checkNotNull(string);
        return JsonResultService.createNode(string);
    }

}
