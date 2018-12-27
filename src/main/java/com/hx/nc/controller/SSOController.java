package com.hx.nc.controller;

import com.hx.nc.service.SSOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

/**
 * @author XingJiajun
 * @Date 2018/12/12 11:20
 * @Description
 */
@Controller
@RequestMapping("/sso")
public class SSOController extends BaseController {

    @Autowired
    private SSOService ssoService;

    @GetMapping("/detail")
    public ModelAndView detailPage() {
        return new ModelAndView(new RedirectView(ssoService.getDetailRedirectUrl()));
    }

}
