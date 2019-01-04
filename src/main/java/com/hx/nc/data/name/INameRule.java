package com.hx.nc.data.name;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author XingJiajun
 * @Date 2019/1/3 10:58
 * @Description
 */
public interface INameRule {
    String getName(JsonNode node, String name);

    String getBaseName(JsonNode node, boolean sub);

}
