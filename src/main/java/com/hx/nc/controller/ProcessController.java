package com.hx.nc.controller;

import com.hx.nc.bo.JsonResult;
import com.hx.nc.service.NCService;
import com.hx.nc.service.ProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @author XingJiajun
 * @Date 2018/12/12 11:19
 * @Description
 */
@RestController
@RequestMapping("/process")
@CrossOrigin("*")
public class ProcessController extends BaseController {

    @Autowired
    private ProcessService processService;
    @Autowired
    private NCService ncService;

    /**
     * 查询代办
     *
     * @return
     */
    @GetMapping("/listTodo")
    public JsonResult listTodo() {
        return buildSuccess(ncService.getNCTask(new Date()));
    }

    /**
     * 获取单据详情
     *
     * @return
     */
    @GetMapping("/getApply")
    public JsonResult getApply() {
        return buildSuccess(processService.getNCBillDetailData());
    }

    /**
     * 获取审批详情
     *
     * @return
     */
    @GetMapping("/getApprove")
    public JsonResult getApprove() {
        return buildSuccess(processService.getNCApproveDetailData());
    }

    /**
     * 同意
     *
     * @return
     */
    @GetMapping("/audit")
    public JsonResult audit() {
        return buildSuccess(processService.action());
    }

    /**
     * 不同意
     *
     * @return
     */
    @GetMapping("/rejectTask")
    public JsonResult rejectTask() {
        return buildSuccess(processService.action());
    }

    /**
     * 获取附件
     * @return
     */
    @GetMapping("/attach")
    public JsonResult attach() {
        return buildSuccess(processService.getAttachment());
    }

    @ExceptionHandler(Exception.class)
    public JsonResult handle(Exception e) {
        return JsonResult.failResult(e.getMessage());
    }

}
