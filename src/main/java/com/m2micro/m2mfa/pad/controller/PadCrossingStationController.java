package com.m2micro.m2mfa.pad.controller;


import com.m2micro.framework.authorization.Authorize;
import com.m2micro.framework.commons.annotation.UserOperationLog;
import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.m2mfa.pad.model.CrossingStationModel;
import com.m2micro.m2mfa.pad.model.CrossingStationPara;
import com.m2micro.m2mfa.pad.service.PadCrossingStationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pad/padCrossingStationController")
@Api(value="pad过站  前端控制器")
@Authorize(Authorize.authorizeType.AllowAll)
public class PadCrossingStationController {
    @Autowired
    PadCrossingStationService padCrossingStationService;

    @RequestMapping("/getCrossingStationInfo")
    @ApiOperation(value="pad获取过站信息")
    @UserOperationLog("pad获取过站信息")
    public ResponseMessage<CrossingStationModel> getCrossingStationInfo(CrossingStationPara obj){
        return padCrossingStationService.getCrossingStationInfo(obj);
    }

}
