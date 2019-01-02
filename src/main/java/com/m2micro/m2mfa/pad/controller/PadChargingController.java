package com.m2micro.m2mfa.pad.controller;

import com.m2micro.framework.commons.annotation.UserOperationLog;
import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.m2mfa.pad.service.PadChargingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: liaotao
 * @Date: 2018/12/28 14:44
 * @Description: 加料
 */
@RestController
@RequestMapping("/pad/padCharging")
@Api(value="pad加料  前端控制器")
public class PadChargingController {
    @Autowired
    PadChargingService padChargingService;

    /**
     * 上工
     */
    @RequestMapping("/startWork")
    @ApiOperation(value="pad加料上工")
    @UserOperationLog("pad加料上工")
    public ResponseMessage startWork(Object obj){
        return ResponseMessage.ok(padChargingService.startWork(obj));
    }

    /**
     * 下工
     */
    @RequestMapping("/stopWork")
    @ApiOperation(value="pad加料下工")
    @UserOperationLog("pad加料下工")
    public ResponseMessage stopWork(Object obj){
        return ResponseMessage.ok(padChargingService.stopWork(obj));
    }

    /**
     * 结束作业
     */
    @RequestMapping("/finishHomework")
    @ApiOperation(value="pad加料结束作业")
    @UserOperationLog("pad加料结束作业")
    public ResponseMessage finishHomework(Object obj){
        return ResponseMessage.ok(padChargingService.finishHomework(obj));
    }
    /**
     * 不良品数
     */
    @RequestMapping("/defectiveProducts")
    @ApiOperation(value="pad加料不良品数")
    @UserOperationLog("pad加料不良品数")
    public ResponseMessage defectiveProducts (Object obj){
        return ResponseMessage.ok(padChargingService.defectiveProducts(obj));
    }
    /**
     * 提报异常
     */
    @RequestMapping("/reportingAnomalies")
    @ApiOperation(value="pad加料提报异常")
    @UserOperationLog("pad加料提报异常")
    public ResponseMessage reportingAnomalies(Object obj){
        return ResponseMessage.ok(padChargingService.reportingAnomalies(obj));
    }
    /**
     * 作业输入
     */
    @RequestMapping("/jobInput")
    @ApiOperation(value="pad加料作业输入")
    @UserOperationLog("pad加料作业输入")
    public ResponseMessage jobInput(Object obj){
        return ResponseMessage.ok(padChargingService.jobInput(obj));
    }

    /**
     * 作业指导
     */
    @RequestMapping("/homeworkGuidance")
    @ApiOperation(value="pad加料作业指导")
    @UserOperationLog("pad加料作业指导")
    public ResponseMessage homeworkGuidance(Object obj){
        return ResponseMessage.ok(padChargingService.homeworkGuidance(obj));
    }

    /**
     * 操作历史
     */
    @RequestMapping("/operationHistory")
    @ApiOperation(value="pad加料操作历史")
    @UserOperationLog("pad加料操作历史")
    public ResponseMessage operationHistory (Object obj){
        return ResponseMessage.ok(padChargingService.operationHistory(obj));
    }
}
