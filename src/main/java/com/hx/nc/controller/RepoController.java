package com.hx.nc.controller;

import com.hx.nc.bo.JsonResult;
import com.hx.nc.data.dao.OARestRepository;
import com.hx.nc.data.entity.OARestRecord;
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

    private OARestRepository oaRestRepository;

    @Autowired
    public RepoController(OARestRepository oaRestRepository) {
        this.oaRestRepository = oaRestRepository;
    }

    @GetMapping("/oaRest")
    public @ResponseBody
    JsonResult OARest() {
        return buildSuccess(oaRestRepository.findAll(getPageRequest("restAt")));
    }

    @GetMapping("/oaRestRecords")
    public String OARestRecords(WebRequest request) {
        Page<OARestRecord> restAt = oaRestRepository.findAll(getPageRequest("restAt"));
        request.setAttribute("oaRestRecords", restAt, WebRequest.SCOPE_REQUEST);
        return "oaRestRecord";
    }

}
