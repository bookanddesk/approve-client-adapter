package com.hx.nc.controller;

import com.hx.nc.bo.JsonResult;
import com.hx.nc.data.dao.OARestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author XingJiajun
 * @Date 2019/1/18 14:11
 * @Description
 */
@RestController
@RequestMapping("repo")
public class RepoController extends BaseController {

    private OARestRepository oaRestRepository;

    @Autowired
    public RepoController(OARestRepository oaRestRepository) {
        this.oaRestRepository = oaRestRepository;
    }

    @GetMapping("/oaRest")
    public JsonResult oaRest() {
        return buildSuccess(oaRestRepository.findAll(getPageRequest("restAt")));
    }

}
