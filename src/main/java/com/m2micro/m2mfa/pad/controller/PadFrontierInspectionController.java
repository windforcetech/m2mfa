package com.m2micro.m2mfa.pad.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.framework.commons.annotation.UserOperationLog;
import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.m2mfa.pad.model.PadPara;
import com.m2micro.m2mfa.pad.model.StopWorkPara;
import com.m2micro.m2mfa.pad.service.PadFrontierInspectionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: liaotao
 * @Date: 2019/1/2 13:37
 * @Description:
 */
@RestController
@RequestMapping("/pad/padFrontierInspection")
@Api(value="pad边检  前端控制器")
@Authorize(Authorize.authorizeType.AllowAll)
public class PadFrontierInspectionController {
    @Autowired
    PadFrontierInspectionService padFrontierInspectionService;

    /**
     * 上工
     */
    @RequestMapping("/startWork")
    @ApiOperation(value="pad边检上工")
    @UserOperationLog("pad边检上工")
    public ResponseMessage startWork(PadPara obj){
        return ResponseMessage.ok(padFrontierInspectionService.startWork(obj));
    }

    /**
     * 下工
     */
    @RequestMapping("/stopWork")
    @ApiOperation(value="pad边检下工")
    @UserOperationLog("pad边检下工")
    public ResponseMessage stopWork(StopWorkPara obj){
        return ResponseMessage.ok(padFrontierInspectionService.stopWork(obj));
    }

    /**
     * 结束作业
     */
    @RequestMapping("/finishHomework")
    @ApiOperation(value="pad边检结束作业")
    @UserOperationLog("pad边检结束作业")
    public ResponseMessage finishHomework(Object obj){
        return ResponseMessage.ok(padFrontierInspectionService.finishHomework(obj));
    }
    /**
     * 不良品数
     */
    @RequestMapping("/defectiveProducts")
    @ApiOperation(value="pad边检不良品数")
    @UserOperationLog("pad边检不良品数")
    public ResponseMessage defectiveProducts (Object obj){
        return ResponseMessage.ok(padFrontierInspectionService.defectiveProducts(obj));
    }
    /**
     * 提报异常
     */
    @RequestMapping("/reportingAnomalies")
    @ApiOperation(value="pad边检提报异常")
    @UserOperationLog("pad边检提报异常")
    public ResponseMessage reportingAnomalies(Object obj){
        return ResponseMessage.ok(padFrontierInspectionService.reportingAnomalies(obj));
    }
    /**
     * 作业输入
     */
    @RequestMapping("/jobInput")
    @ApiOperation(value="pad边检作业输入")
    @UserOperationLog("pad边检作业输入")
    public ResponseMessage jobInput(Object obj){
        return ResponseMessage.ok(padFrontierInspectionService.jobInput(obj));
    }

    /**
     * 作业指导
     */
    @RequestMapping("/homeworkGuidance")
    @ApiOperation(value="pad边检作业指导")
    @UserOperationLog("pad边检作业指导")
    public ResponseMessage homeworkGuidance(Object obj){
        return ResponseMessage.ok(padFrontierInspectionService.homeworkGuidance(obj));
    }

    /**
     * 操作历史
     */
    @RequestMapping("/operationHistory")
    @ApiOperation(value="pad边检操作历史")
    @UserOperationLog("pad边检操作历史")
    public ResponseMessage operationHistory (Object obj){
        return ResponseMessage.ok(padFrontierInspectionService.operationHistory(obj));
    }
}
