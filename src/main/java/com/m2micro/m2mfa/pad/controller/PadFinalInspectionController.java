package com.m2micro.m2mfa.pad.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.framework.commons.annotation.UserOperationLog;
import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.m2mfa.pad.model.PadPara;
import com.m2micro.m2mfa.pad.model.StopWorkPara;
import com.m2micro.m2mfa.pad.service.PadFinalInspectionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: liaotao
 * @Date: 2019/1/2 13:38
 * @Description:
 */

@RestController
@RequestMapping("/pad/padFinalInspection")
@Api(value="pad终检  前端控制器")
@Authorize(Authorize.authorizeType.AllowAll)
public class PadFinalInspectionController {
    @Autowired
    PadFinalInspectionService padFinalInspectionService;

    /**
     * 上工
     */
    @RequestMapping("/startWork")
    @ApiOperation(value="pad终检上工")
    @UserOperationLog("pad终检上工")
    public ResponseMessage startWork(PadPara obj){
        return ResponseMessage.ok(padFinalInspectionService.startWork(obj));
    }

    /**
     * 下工
     */
    @RequestMapping("/stopWork")
    @ApiOperation(value="pad终检下工")
    @UserOperationLog("pad终检下工")
    public ResponseMessage stopWork(StopWorkPara obj){
        return ResponseMessage.ok(padFinalInspectionService.stopWork(obj));
    }

    /**
     * 结束作业
     */
    @RequestMapping("/finishHomework")
    @ApiOperation(value="pad终检结束作业")
    @UserOperationLog("pad终检结束作业")
    public ResponseMessage finishHomework(Object obj){
        return ResponseMessage.ok(padFinalInspectionService.finishHomework(obj));
    }
    /**
     * 不良品数
     */
    @RequestMapping("/defectiveProducts")
    @ApiOperation(value="pad终检不良品数")
    @UserOperationLog("pad终检不良品数")
    public ResponseMessage defectiveProducts (Object obj){
        return ResponseMessage.ok(padFinalInspectionService.defectiveProducts(obj));
    }
    /**
     * 提报异常
     */
    @RequestMapping("/reportingAnomalies")
    @ApiOperation(value="pad终检提报异常")
    @UserOperationLog("pad终检提报异常")
    public ResponseMessage reportingAnomalies(Object obj){
        return ResponseMessage.ok(padFinalInspectionService.reportingAnomalies(obj));
    }
    /**
     * 作业输入
     */
    @RequestMapping("/jobInput")
    @ApiOperation(value="pad终检作业输入")
    @UserOperationLog("pad终检作业输入")
    public ResponseMessage jobInput(Object obj){
        return ResponseMessage.ok(padFinalInspectionService.jobInput(obj));
    }

    /**
     * 作业指导
     */
    @RequestMapping("/homeworkGuidance")
    @ApiOperation(value="pad终检作业指导")
    @UserOperationLog("pad终检作业指导")
    public ResponseMessage homeworkGuidance(Object obj){
        return ResponseMessage.ok(padFinalInspectionService.homeworkGuidance(obj));
    }

    /**
     * 操作历史
     */
    @RequestMapping("/operationHistory")
    @ApiOperation(value="pad终检操作历史")
    @UserOperationLog("pad终检操作历史")
    public ResponseMessage operationHistory (Object obj){
        return ResponseMessage.ok(padFinalInspectionService.operationHistory(obj));
    }
}
