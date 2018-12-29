package com.hx.nc.controller;

import com.hx.nc.bo.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.support.RequestContext;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;

/**
 * @author XingJiajun
 * @Date 2018/12/12 11:18
 * @Description
 */
public abstract class BaseController {

    @Autowired
    protected HttpServletRequest request;

    protected String getMsg(HttpServletRequest request, String msgKey) {
        return getMsg(request, msgKey, null);
    }

    protected String getMsg(HttpServletRequest request, String msgKey, Object[] objects) {
        RequestContext requestContext = new RequestContext(request);
        String message = requestContext.getMessage(msgKey, objects);
        return message != null ? message : msgKey;
    }

    protected void handleValidateError(BindingResult bindingResult) {
        if (bindingResult != null && bindingResult.hasErrors()) {
            throw new IllegalArgumentException(bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(",")));
        }
    }

    protected JsonResult buildSuccess() {
        return JsonResult.successResult();
    }

    protected <T> JsonResult buildSuccess(T t) {
        return JsonResult.successResult(t);
    }

    protected JsonResult buildFail(String msg) {
        return JsonResult.failResult(msg);
    }

    protected <T> JsonResult buildFail(String msg, T t) {
        return JsonResult.failResult(msg, t);
    }


    protected String getParameter(String param) {
        return request.getParameter(param);
    }
}
