package com.m2micro.m2mfa.pad.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.m2mfa.pad.service.PadBottomDisplayService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: liaotao
 * @Date: 2019/2/19 11:50
 * @Description:
 */
@RestController
@RequestMapping("/pad/padBottomDisplay")
@Api(value="pad底部栏显示  前端控制器")
@Authorize(Authorize.authorizeType.AllowAll)
public class PadBottomDisplayController {
    @Autowired
    PadBottomDisplayService padBottomDisplayService;

}
