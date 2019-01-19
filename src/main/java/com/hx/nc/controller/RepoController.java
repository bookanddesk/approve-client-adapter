package com.hx.nc.controller;

import com.hx.nc.bo.JsonResult;
import com.hx.nc.data.entity.OARestRecord;
import com.hx.nc.data.entity.PollingRecord;
import com.hx.nc.service.RepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

/**
 * @author XingJiajun
 * @Date 2019/1/18 14:11
 * @Description
 */
@Controller
@RequestMapping("repo")
public class RepoController extends BaseController {

    private RepoService repoService;

    @Autowired
    public RepoController(RepoService repoService) {
        this.repoService = repoService;
    }

    @GetMapping("/oaRest")
    public @ResponseBody
    JsonResult OARest() {
        return buildSuccess(repoService.pageOARestRecords(getPageRequest("restAt")));
    }

    @GetMapping("/oaRestRecords")
    public String OARestRecords(WebRequest request) {
        Page<OARestRecord> restAt = repoService.pageOARestRecords(getPageRequest("restAt"));
        request.setAttribute("oaRestRecords", restAt, WebRequest.SCOPE_REQUEST);
        return "oaRestRecord";
    }

    @GetMapping("/pollingRecords")
    public String pollingRecords(WebRequest request) {
        Page<PollingRecord> restAt = repoService.pagePollingRecords(getPageRequest("pollAt"));
        request.setAttribute("pollingRecords", restAt, WebRequest.SCOPE_REQUEST);
        return "pollingRecord";
    }

}
