package com.hx.nc.service;

import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

/**
 * @author XingJiajun
 * @Date 2018/12/12 11:21
 * @Description
 */
public abstract class BaseService {

    @Autowired
    protected HttpServletRequest request;

    protected String getParameter(String param) {
        return request.getParameter(param);
    }
}
