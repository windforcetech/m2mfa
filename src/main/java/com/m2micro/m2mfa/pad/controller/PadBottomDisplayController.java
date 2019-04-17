package com.m2micro.m2mfa.pad.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.framework.commons.annotation.UserOperationLog;
import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.m2mfa.pad.model.MoDescInfoModel;
import com.m2micro.m2mfa.pad.model.StationInfoModel;
import com.m2micro.m2mfa.pad.service.PadBottomDisplayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/getMoDescInfo")
    @ApiOperation(value="工单状态相关信息")
    @UserOperationLog("工单状态相关信息")
    public ResponseMessage<MoDescInfoModel> getMoDescInfo(String scheduleId){
        return ResponseMessage.ok(padBottomDisplayService.getMoDescInfo(scheduleId));
    }

    @GetMapping("/getStationInfo")
    @ApiOperation(value="获取工位作业信息及进度")
    @UserOperationLog("获取工位作业信息及进度")
    public ResponseMessage<StationInfoModel> getStationInfo(String scheduleId,String stationId,String processId){
        return ResponseMessage.ok(padBottomDisplayService.getStationInfo(scheduleId, stationId,processId));
    }
}
