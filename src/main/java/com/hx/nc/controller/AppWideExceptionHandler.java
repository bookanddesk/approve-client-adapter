package com.hx.nc.controller;

import com.hx.nc.bo.JsonResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author XingJiajun
 * @Date 2018/12/26 21:44
 * @Description
 */
@ControllerAdvice
public class AppWideExceptionHandler {
    @ExceptionHandler(Exception.class)
    public JsonResult handle(Exception e) {
        return JsonResult.failResult(e.getMessage());
    }
}
