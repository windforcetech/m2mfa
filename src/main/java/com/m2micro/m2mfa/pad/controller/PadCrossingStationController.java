package com.m2micro.m2mfa.pad.controller;


import com.m2micro.framework.authorization.Authorize;
import com.m2micro.framework.commons.annotation.UserOperationLog;
import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.m2mfa.pad.model.CrossingStationModel;
import com.m2micro.m2mfa.pad.model.CrossingStationPara;
import com.m2micro.m2mfa.pad.model.OutStationModel;
import com.m2micro.m2mfa.pad.model.WipRecModel;
import com.m2micro.m2mfa.pad.service.PadCrossingStationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/pad/padCrossingStationController")
@Api(value="pad过站  前端控制器",description = "过站")
//@Authorize(Authorize.authorizeType.AllowAll)
public class PadCrossingStationController {
    @Autowired
    PadCrossingStationService padCrossingStationService;

    @RequestMapping("/getCrossingStationInfo")
    @ApiOperation(value="pad获取过站信息（确认）")
    @UserOperationLog("pad获取过站信息（确认）")
    public ResponseMessage<CrossingStationModel> getCrossingStationInfo(CrossingStationPara obj){
        return padCrossingStationService.getCrossingStationInfo(obj);
    }


    @RequestMapping("/pullIn")
    @ApiOperation(value="pad 进站（代进站处理）")
    @UserOperationLog("pad 进站（代进站处理）")
    public ResponseMessage<List<WipRecModel>> pullIn(String processId){
        return ResponseMessage.ok(padCrossingStationService.pullIn(processId));
    }


    @RequestMapping("/pullOut")
    @ApiOperation(value="pad 出站（确认出站）")
    @UserOperationLog("pad 出站（确认出站）")
    public ResponseMessage pullOut(OutStationModel obj){
        padCrossingStationService.pullOut(obj);
        return ResponseMessage.ok();
    }

}
