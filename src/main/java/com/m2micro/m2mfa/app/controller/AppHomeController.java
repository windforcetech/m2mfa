package com.m2micro.m2mfa.app.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.framework.commons.annotation.UserOperationLog;
import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.m2mfa.app.service.AppHomeService;
import com.m2micro.m2mfa.app.vo.HomePageData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName AppHomeController
 * @Description Mes APP 主页前端控制器
 * @Author admin
 * @Date 2019/6/26 11:18
 * @Version 1.0
 */
@RestController
@RequestMapping("/app")
@Api(value = "Mes APP 主页前端控制器", description = "Mes APP主页")
@Authorize
public class AppHomeController {

    @Autowired
    AppHomeService appHomeService;

    @GetMapping("/home")
    @ApiOperation(value = "获取APP主页数据")
    @UserOperationLog("获取APP主页数据")
    public ResponseMessage<HomePageData> home() {
        return ResponseMessage.ok(appHomeService.queryHomePageData());
    }

}
