package com.m2micro.m2mfa.pad.controller;

import com.m2micro.framework.commons.annotation.UserOperationLog;
import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.m2mfa.pad.model.PadPara;
import com.m2micro.m2mfa.pad.service.PadMoldService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: liaotao
 * @Date: 2019/1/2 13:32
 * @Description:
 */
@RestController
@RequestMapping("/pad/padMold")
@Api(value="pad架模  前端控制器")
public class PadMoldController {
    @Autowired
    PadMoldService padMoldService;

    /**
     * 上工
     */
    @RequestMapping("/startWork")
    @ApiOperation(value="pad架模上工")
    @UserOperationLog("pad架模上工")
    public ResponseMessage startWork(PadPara obj){
        return ResponseMessage.ok(padMoldService.startWork(obj));
    }

    /**
     * 下工
     */
    @RequestMapping("/stopWork")
    @ApiOperation(value="pad架模下工")
    @UserOperationLog("pad架模下工")
    public ResponseMessage stopWork(Object obj){
        return ResponseMessage.ok(padMoldService.stopWork(obj));
    }

    /**
     * 结束作业
     */
    @RequestMapping("/finishHomework")
    @ApiOperation(value="pad架模结束作业")
    @UserOperationLog("pad架模结束作业")
    public ResponseMessage finishHomework(Object obj){
        return ResponseMessage.ok(padMoldService.finishHomework(obj));
    }
    /**
     * 不良品数
     */
    @RequestMapping("/defectiveProducts")
    @ApiOperation(value="pad架模不良品数")
    @UserOperationLog("pad架模不良品数")
    public ResponseMessage defectiveProducts (Object obj){
        return ResponseMessage.ok(padMoldService.defectiveProducts(obj));
    }
    /**
     * 提报异常
     */
    @RequestMapping("/reportingAnomalies")
    @ApiOperation(value="pad架模提报异常")
    @UserOperationLog("pad架模提报异常")
    public ResponseMessage reportingAnomalies(Object obj){
        return ResponseMessage.ok(padMoldService.reportingAnomalies(obj));
    }
    /**
     * 作业输入
     */
    @RequestMapping("/jobInput")
    @ApiOperation(value="pad架模作业输入")
    @UserOperationLog("pad架模作业输入")
    public ResponseMessage jobInput(Object obj){
        return ResponseMessage.ok(padMoldService.jobInput(obj));
    }

    /**
     * 作业指导
     */
    @RequestMapping("/homeworkGuidance")
    @ApiOperation(value="pad架模作业指导")
    @UserOperationLog("pad架模作业指导")
    public ResponseMessage homeworkGuidance(Object obj){
        return ResponseMessage.ok(padMoldService.homeworkGuidance(obj));
    }

    /**
     * 操作历史
     */
    @RequestMapping("/operationHistory")
    @ApiOperation(value="pad架模操作历史")
    @UserOperationLog("pad架模操作历史")
    public ResponseMessage operationHistory (Object obj){
        return ResponseMessage.ok(padMoldService.operationHistory(obj));
    }
}
