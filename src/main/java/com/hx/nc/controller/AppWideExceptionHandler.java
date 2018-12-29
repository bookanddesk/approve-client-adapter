package com.hx.nc.controller;

import com.hx.nc.bo.JsonResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author XingJiajun
 * @Date 2018/12/26 21:44
 * @Description
 */
@ControllerAdvice(basePackages = "com.hx.nc.controller", annotations = Controller.class)
public class AppWideExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public JsonResult handle(Exception e) {
        return JsonResult.failResult(e.getMessage());
    }
}
