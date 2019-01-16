package com.m2micro.m2mfa.pad.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.framework.commons.annotation.UserOperationLog;
import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.m2mfa.pad.model.PadPara;
import com.m2micro.m2mfa.pad.service.PadBootstrapService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: liaotao
 * @Date: 2019/1/2 13:35
 * @Description:
 */
@RestController
@RequestMapping("/pad/padBootstrap")
@Api(value="pad开机  前端控制器")
@Authorize(Authorize.authorizeType.AllowAll)
public class PadBootstrapController {
    @Autowired
    PadBootstrapService padBootstrapService;

    /**
     * 上工
     */
    @RequestMapping("/startWork")
    @ApiOperation(value="pad开机上工")
    @UserOperationLog("pad开机上工")
    public ResponseMessage startWork(PadPara obj){
        return ResponseMessage.ok(padBootstrapService.startWork(obj));
    }

    /**
     * 下工
     */
    @RequestMapping("/stopWork")
    @ApiOperation(value="pad开机下工")
    @UserOperationLog("pad开机下工")
    public ResponseMessage stopWork(Object obj){
        return ResponseMessage.ok(padBootstrapService.stopWork(obj));
    }

    /**
     * 结束作业
     */
    @RequestMapping("/finishHomework")
    @ApiOperation(value="pad开机结束作业")
    @UserOperationLog("pad开机结束作业")
    public ResponseMessage finishHomework(Object obj){
        return ResponseMessage.ok(padBootstrapService.finishHomework(obj));
    }
    /**
     * 不良品数
     */
    @RequestMapping("/defectiveProducts")
    @ApiOperation(value="pad开机不良品数")
    @UserOperationLog("pad开机不良品数")
    public ResponseMessage defectiveProducts (Object obj){
        return ResponseMessage.ok(padBootstrapService.defectiveProducts(obj));
    }
    /**
     * 提报异常
     */
    @RequestMapping("/reportingAnomalies")
    @ApiOperation(value="pad开机提报异常")
    @UserOperationLog("pad开机提报异常")
    public ResponseMessage reportingAnomalies(Object obj){
        return ResponseMessage.ok(padBootstrapService.reportingAnomalies(obj));
    }
    /**
     * 作业输入
     */
    @RequestMapping("/jobInput")
    @ApiOperation(value="pad开机作业输入")
    @UserOperationLog("pad开机作业输入")
    public ResponseMessage jobInput(Object obj){
        return ResponseMessage.ok(padBootstrapService.jobInput(obj));
    }

    /**
     * 作业指导
     */
    @RequestMapping("/homeworkGuidance")
    @ApiOperation(value="pad开机作业指导")
    @UserOperationLog("pad开机作业指导")
    public ResponseMessage homeworkGuidance(Object obj){
        return ResponseMessage.ok(padBootstrapService.homeworkGuidance(obj));
    }

    /**
     * 操作历史
     */
    @RequestMapping("/operationHistory")
    @ApiOperation(value="pad开机操作历史")
    @UserOperationLog("pad开机操作历史")
    public ResponseMessage operationHistory (Object obj){
        return ResponseMessage.ok(padBootstrapService.operationHistory(obj));
    }
}
