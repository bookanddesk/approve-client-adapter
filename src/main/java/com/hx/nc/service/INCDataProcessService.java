package com.hx.nc.service;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author XingJiajun
 * @Date 2019/1/11 9:36
 * @Description
 */
public interface INCDataProcessService {
    JsonNode getNCDataNode(String jsonStr);

    void checkNCData(JsonNode jsonNode);

}
