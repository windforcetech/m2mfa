package com.m2micro.m2mfa.pad.controller;

import com.m2micro.framework.commons.annotation.UserOperationLog;
import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.m2mfa.pad.model.PadPara;
import com.m2micro.m2mfa.pad.service.PadMachineService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: liaotao
 * @Date: 2019/1/2 13:33
 * @Description:
 */
@RestController
@RequestMapping("/pad/padMachine")
@Api(value="pad调机  前端控制器")
public class PadMachineController {
    @Autowired
    PadMachineService padMachineService;

    /**
     * 上工
     */
    @RequestMapping("/startWork")
    @ApiOperation(value="pad调机上工")
    @UserOperationLog("pad调机上工")
    public ResponseMessage startWork(PadPara obj){
        return ResponseMessage.ok(padMachineService.startWork(obj));
    }

    /**
     * 下工
     */
    @RequestMapping("/stopWork")
    @ApiOperation(value="pad调机下工")
    @UserOperationLog("pad调机下工")
    public ResponseMessage stopWork(Object obj){
        return ResponseMessage.ok(padMachineService.stopWork(obj));
    }

    /**
     * 结束作业
     */
    @RequestMapping("/finishHomework")
    @ApiOperation(value="pad调机结束作业")
    @UserOperationLog("pad调机结束作业")
    public ResponseMessage finishHomework(Object obj){
        return ResponseMessage.ok(padMachineService.finishHomework(obj));
    }
    /**
     * 不良品数
     */
    @RequestMapping("/defectiveProducts")
    @ApiOperation(value="pad调机不良品数")
    @UserOperationLog("pad调机不良品数")
    public ResponseMessage defectiveProducts (Object obj){
        return ResponseMessage.ok(padMachineService.defectiveProducts(obj));
    }
    /**
     * 提报异常
     */
    @RequestMapping("/reportingAnomalies")
    @ApiOperation(value="pad调机提报异常")
    @UserOperationLog("pad调机提报异常")
    public ResponseMessage reportingAnomalies(Object obj){
        return ResponseMessage.ok(padMachineService.reportingAnomalies(obj));
    }
    /**
     * 作业输入
     */
    @RequestMapping("/jobInput")
    @ApiOperation(value="pad调机作业输入")
    @UserOperationLog("pad调机作业输入")
    public ResponseMessage jobInput(Object obj){
        return ResponseMessage.ok(padMachineService.jobInput(obj));
    }

    /**
     * 作业指导
     */
    @RequestMapping("/homeworkGuidance")
    @ApiOperation(value="pad调机作业指导")
    @UserOperationLog("pad调机作业指导")
    public ResponseMessage homeworkGuidance(Object obj){
        return ResponseMessage.ok(padMachineService.homeworkGuidance(obj));
    }

    /**
     * 操作历史
     */
    @RequestMapping("/operationHistory")
    @ApiOperation(value="pad调机操作历史")
    @UserOperationLog("pad调机操作历史")
    public ResponseMessage operationHistory (Object obj){
        return ResponseMessage.ok(padMachineService.operationHistory(obj));
    }
}
