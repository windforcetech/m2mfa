package com.m2micro.m2mfa.pad.controller;


import com.m2micro.framework.authorization.Authorize;
import com.m2micro.framework.commons.annotation.UserOperationLog;
import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.m2mfa.base.entity.BaseDefect;
import com.m2micro.m2mfa.pad.model.*;
import com.m2micro.m2mfa.pad.service.PadCrossingStationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
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
    public ResponseMessage<List<WipRecModel>> pullIn(String processId,String barcode){
        return ResponseMessage.ok(padCrossingStationService.pullIn(processId,barcode));
    }


    @RequestMapping("/pullOut")
    @ApiOperation(value="pad 出站（确认出站）")
    @UserOperationLog("pad 出站（确认出站）")
    public ResponseMessage pullOut(@RequestBody OutStationModel obj){
        padCrossingStationService.pullOut(obj);
        ResponseMessage<Object> ok = ResponseMessage.ok();
        ok.setMessage("出站成功！");
        return ok;
    }


    @RequestMapping("/getCrossStationDefectModel")
    @ApiOperation(value="pad 不良输入（不良输入）")
    @UserOperationLog("pad 不良输入（不良输入）")
    public ResponseMessage<CrossStationDefectModel> getCrossStationDefectModel(CrossStationDefectPara crossStationDefectPara){
        return ResponseMessage.ok(padCrossingStationService.getCrossStationDefectModel(crossStationDefectPara));
    }

    @RequestMapping("/getAllDefectProcess")
    @ApiOperation(value="pad 不良工序（不良工序）")
    @UserOperationLog("pad 不良工序（不良工序）")
    public ResponseMessage<List<CrossStationProcess>> getAllDefectProcess(CrossStationDefectPara crossStationDefectPara){
        return ResponseMessage.ok(padCrossingStationService.getAllDefectProcess(crossStationDefectPara));
    }

    @RequestMapping("/findAllBaseDefect")
    @ApiOperation(value="pad 不良现象（不良现象）")
    @UserOperationLog("pad 不良现象（不良现象）")
    public ResponseMessage<List<BaseDefect>> findAllBaseDefect(){
        return ResponseMessage.ok(padCrossingStationService.findAllBaseDefect());
    }

}
