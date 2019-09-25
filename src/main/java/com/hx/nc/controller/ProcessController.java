package com.hx.nc.controller;

import com.hx.nc.bo.JsonResult;
import com.hx.nc.bo.nc.*;
import com.hx.nc.service.NCService;
import com.hx.nc.service.PollingTask;
import com.hx.nc.service.ProcessService;
import com.hx.nc.utils.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

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
    @Autowired
    private PollingTask pollingTask;

    /**
     * 查询代办
     *
     * @return
     */
    @GetMapping("/listTodo")
    public JsonResult listTodo(String lastDate, String groupId) {
        List<NCTask> ncTaskList = ncService.getNCTaskList(
                lastDate != null ? lastDate : "2018-12-10",
                groupId != null ? groupId : "0001V610000000000EEN");
        return buildSuccess(ncTaskList);
    }

    /**
     * 获取单据详情
     *
     * @return
     */
    @GetMapping("/getApply")
    public JsonResult getApply(@Valid NCBillDetailParams params, BindingResult bindingResult) {
        handleValidateError(bindingResult);
        return buildSuccess(processService.getApply(params));
    }

    /**
     * 获取审批详情
     *
     * @return
     */
    @GetMapping("/getApprove")
    public JsonResult getApprove(@Valid NCBillDetailParams params, BindingResult bindingResult) {
        handleValidateError(bindingResult);
        return buildSuccess(processService.getNCApproveDetailData(params));
    }

    @GetMapping("/assignCheck")
    public JsonResult assignCheck(@Valid NCActionParams params, BindingResult bindingResult) {
        handleValidateError(bindingResult);
        return buildSuccess(processService.getNCAssignUserList(params));
    }

    /**
     * 同意
     *
     * @return
     */
    @GetMapping("/audit")
    public JsonResult audit(@Valid NCActionParams params, BindingResult bindingResult) {
        handleValidateError(bindingResult);
        return buildSuccess(processService.action(params));
    }

    /**
     * 不同意
     *
     * @return
     */
    @GetMapping("/rejectTask")
    public JsonResult rejectTask(@Valid NCActionParams params, BindingResult bindingResult) {
        handleValidateError(bindingResult);
        return buildSuccess(processService.action(params));
    }

    /**
     * 获取附件
     * @return
     */
    @GetMapping("/getTaskAttachments")
    public JsonResult getTaskAttachments(@Valid NCTaskBaseParams params, BindingResult bindingResult) {
        handleValidateError(bindingResult);
        return buildSuccess(processService.queryInstAttachmentList(params));
    }


    @GetMapping("/pushTask")
    public JsonResult pushTask(String[] taskIds, String lastDate, String groupId) {
        pollingTask.pushTask(taskIds != null ? Arrays.asList(taskIds) : null,
                lastDate != null ? lastDate : DateTimeUtils.defaultPollDateTime(), groupId);
        return JsonResult.successResult();
    }

    /**
     * 附件下载
     * @param params
     * @param bindingResult
     * @return
     */
    @GetMapping("/download")
    public ResponseEntity<byte[]> download(@Valid NCFileDataParams params, BindingResult bindingResult) {
        handleValidateError(bindingResult);
        return processService.download(params);
    }

}
