package com.m2micro.m2mfa.version.controller;

import com.m2micro.framework.commons.annotation.UserOperationLog;
import com.m2micro.framework.commons.model.ResponseMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @Auther: liaotao
 * @Date: 2019/1/16 10:48
 * @Description:
 */
@RestController
@RequestMapping("/version/version")
@Api(value="获取版本 前端控制器")
public class VersionController {

    @Value("${m2mfa.version}")
    private String version;

    @RequestMapping("/getVersion")
    @ApiOperation(value="获取版本")
    @UserOperationLog("获取版本")
    public ResponseMessage<String> getVersion(){
        return ResponseMessage.ok(version);
    }
}
