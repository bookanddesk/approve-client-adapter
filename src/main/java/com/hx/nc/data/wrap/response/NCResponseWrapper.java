package com.hx.nc.data.wrap.response;

import com.fasterxml.jackson.databind.JsonNode;
import com.hx.nc.data.convert.Json2ObjectConvertor;
import com.hx.nc.data.wrap.NCDataResultBase;

/**
 * @author XingJiajun
 * @Date 2019/1/3 15:54
 * @Description
 */
public class NCResponseWrapper {

    private JsonNode node;

    public NCResponseWrapper(JsonNode node) {
        this.node = node;
    }

    public <T extends NCBaseResponse> T wrap(Class<T> tClass) {
        return Json2ObjectConvertor.convert(tClass, node);
    }

}
